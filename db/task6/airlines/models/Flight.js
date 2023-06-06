const { NotFound, BadRequest } = require('http-errors');

const { db } = require('../configs/dbConfig');

class Flight {
  static checkBoardingPassDoesNotExist(boardingPass) {
    if (boardingPass) {
      throw new BadRequest('You have already checked in for the specified flight');
    }
  }

  static async getBoardingPass(transaction, booking) {
    const QUERY = `
      SELECT *
      FROM bookings.boarding_passes
      WHERE ticket_no = '${booking.ticketNo}';
    `;
    const result = await transaction.query(QUERY);
    const boardingPass = result?.rows[0];
    return boardingPass;
  }

  static ckeckFlightExists(flight) {
    if (!flight) {
      throw new NotFound('The specified flight for check in was not found');
    }
  }

  static async getFligthById(transaction, booking) {
    const QUERY = `
      SELECT * FROM bookings.flights
      WHERE flight_id=${booking.flightId} AND (status='On Time' OR status='Delayed');
    `;
    const result = await transaction.query(QUERY);
    const flight = result?.rows[0];
    return flight;
  }

  static checkTicketFligth(ticketFlight) {
    if (!ticketFlight) {
      throw new BadRequest('To check in for a flight, it is necessary to provide a ticket')
    }
  }

  static async getTicketFlight(transaction, booking) {
    const QUERY = `
      SELECT flight_id AS "flightId", fare_conditions AS "fareConditions"
      FROM bookings.ticket_flights
      WHERE ticket_no = '${booking.ticketNo}';
    `;
    const result = await transaction.query(QUERY);
    return result?.rows[0];
  }

  static async getAircraft(transaction, ticketFligth) {
    const QUERY = `
      SELECT aircraft_code AS "aircraftCode"
      FROM bookings.flights
      WHERE flight_id = ${ticketFligth.flightId};
    `;
    const result = await transaction.query(QUERY);
    const aircraft = result?.rows[0];
    return aircraft;
  }

  static ckeckSeatsAvailable(seat) {
    if (!seat) {
      throw new BadRequest('The seat is already checked in');
    }
  }

  static async getSeatForBooking(transaction, ticketFlight, aircraft) {
    const QUERY = `
      SELECT seat_no AS "seatNo"
      FROM bookings.seats
      WHERE
        aircraft_code='${aircraft.aircraftCode}' AND
        fare_conditions='${ticketFlight.fareConditions}' AND
        seat_no NOT IN (SELECT seat_no FROM bookings.boarding_passes WHERE flight_id=${ticketFlight.flightId})
      LIMIT 1;
    `;
    const result = await transaction.query(QUERY);
    const seat = result?.rows[0];
    return seat;
  }

  static async generateBoardingPassNo(transaction) {
    const QUERY = `SELECT MAX(boarding_no) FROM boarding_passes;`;
    const result = await transaction.query(QUERY);
    const boardingNo = result?.rows[0].max + 1;
    return boardingNo;
  }

  static async insertBoardingPass(transaction, boardingPass) {
    const QUERY = `
      INSERT INTO boarding_passes (ticket_no, flight_id, boarding_no, seat_no)
      VALUES ('${boardingPass.ticketNo}', ${boardingPass.flightId}, ${boardingPass.boardingNo}, '${boardingPass.seatNo}');
    `;
    await transaction.query(QUERY);
  }

  static async checkIn(booking) {
    const transaction = await db.connect();
    try {
      await transaction.query('BEGIN');
      const flight = await this.getFligthById(transaction, booking);
      this.ckeckFlightExists(flight);
      const foundBoardingPass = await this.getBoardingPass(transaction, booking);
      this.checkBoardingPassDoesNotExist(foundBoardingPass);
      const ticketFlight = await this.getTicketFlight(transaction, booking);
      this.checkTicketFligth(ticketFlight);
      const aircraft = await this.getAircraft(transaction, ticketFlight);
      const seat = await this.getSeatForBooking(transaction, ticketFlight, aircraft);
      this.ckeckSeatsAvailable(seat);
      const boardingNo = await this.generateBoardingPassNo(transaction);

      const boardingPass = {
        boardingNo: boardingNo,
        ticketNo: booking.ticketNo,
        flightId: booking.flightId,
        seatNo: seat.seatNo,
      }
      await this.insertBoardingPass(transaction, boardingPass);

      await transaction.query('COMMIT');
      return boardingPass;
    } catch (error) {
      await transaction.query('ROLLBACK');
      throw error;
    } finally {
      transaction.release();
    }
  }
}


module.exports = Flight;
