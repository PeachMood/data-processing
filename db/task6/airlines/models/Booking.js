const { v4: uuidv4 } = require('uuid');
const { BadRequest, NotFound } = require('http-errors');

const { db } = require('../configs/dbConfig');


class Booking {
  static checkFlightStatus(flight) {
    if (flight.status === 'Arrived') {
      throw new BadRequest('Can not book tickets for a flight that has already arrived');
    }
  }

  static checkFlightInResult(result) {
    if (result?.rowCount === 0) {
      throw new NotFound('Flight not found');
    }
  }

  static async getFlightForBooking(transaction, booking) {
    const QUERY = `
      SELECT flight_id AS "flightId", flight_no AS "flightNo", status, aircraft_code as "aircraftCode"
      FROM bookings.flights
      WHERE flight_no='${booking.flightNo}' AND scheduled_departure::date='${booking.flightDate}'
    `;

    const result = await transaction.query(QUERY);
    this.checkFlightInResult(result);
    return result.rows[0];
  }

  static checkSeatsLeft(count) {
    if (count <= 0) {
      throw new BadRequest('There are no available seats for the flight');
    }
  }

  static async getSeatsInFlight(transaction, booking, flight) {
    const QUERY = `
      SELECT COUNT(*) -
      (SELECT COUNT(*) FROM bookings.ticket_flights WHERE flight_id='${flight.flightId}' AND fare_conditions='${booking.fareConditions}') AS count
      FROM bookings.seats
      WHERE aircraft_code = (SELECT aircraft_code FROM bookings.flights WHERE flight_id='${flight.flightId}' and fare_conditions='${booking.fareConditions}')
    `;

    const result = await transaction.query(QUERY);
    const { count } = result.rows[0];
    return count;
  }

  static async getTicketPrice(transaction, booking, flight) {
    const QUERY = `
      SELECT amount FROM bookings.ticket_flights
      WHERE flight_id='${flight.flightId}' AND fare_conditions='${booking.fareConditions}'
      ORDER BY amount LIMIT 1;
    `;
    const result = await transaction.query(QUERY);
    const { amount } = result.rows[0];
    return amount;
  }

  static async insertBooking(transaction, booking) {
    const QUERY = `
      INSERT INTO bookings.bookings (book_ref, book_date, total_amount)
      VALUES ('${booking.bookRef}', ${booking.bookDate}, ${booking.totalAmount})
      RETURNING book_ref AS "bookRef";
    `;
    const result = await transaction.query(QUERY);
    return result.rows[0].bookRef;
  }

  static async insertTicket(transaction, ticket) {
    const QUERY = `
      INSERT INTO bookings.tickets (ticket_no, book_ref, passenger_id, passenger_name)
      VALUES ('${ticket.ticketNo}', '${ticket.bookRef}', '${ticket.passengerId}', '${ticket.passengerName}')
      RETURNING ticket_no AS "ticketNo";
    `;
    const result = await transaction.query(QUERY);
    return result.rows[0].ticketNo;
  }

  static async insertTicketFlight(transaction, ticketFlight) {
    const QUERY = `
      INSERT INTO bookings.ticket_flights (ticket_no, flight_id, fare_conditions, amount)
      VALUES ('${ticketFlight.ticketNo}', ${ticketFlight.flightId}, '${ticketFlight.fareConditions}', ${ticketFlight.amount});
    `;
    await transaction.query(QUERY);
  }

  static async createBooking(data) {
    const transaction = await db.connect();
    try {
      await transaction.query('BEGIN');

      const flight = await this.getFlightForBooking(transaction, data);
      this.checkFlightStatus(flight);

      const seats = await this.getSeatsInFlight(transaction, data, flight);
      this.checkSeatsLeft(seats);

      const price = await this.getTicketPrice(transaction, data, flight);

      const booking = {
        bookRef: uuidv4().replace(/-/g, '').slice(0, 6),
        bookDate: 'now()',
        totalAmount: price,
      };
      data.bookRef = await this.insertBooking(transaction, booking);

      const ticket = {
        ticketNo: uuidv4().replace(/-/g, '').slice(0, 13),
        bookRef: booking.bookRef,
        passengerId: data.passengerId,
        passengerName: data.passengerName,
      };
      data.ticketNo = await this.insertTicket(transaction, ticket);

      const ticketFlight = {
        ticketNo: data.ticketNo,
        flightId: flight.flightId,
        fareConditions: data.fareConditions,
        amount: price,
      };
      await this.insertTicketFlight(transaction, ticketFlight);

      await transaction.query('COMMIT');
      return data;
    } catch (error) {
      await transaction.query('ROLLBACK');
      throw error;
    } finally {
      transaction.release();
    }
  }
}

module.exports = Booking;
