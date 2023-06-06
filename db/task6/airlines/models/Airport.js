const { db } = require('../configs/dbConfig');

const GET_AIRPORTS_QUERY =
  `
    SELECT
      airport_code AS code,
      airport_name AS name,
      city,
      coordinates[0] AS longitude,
      coordinates[1] AS latitude,
      timezone
    FROM bookings.airports
  `;

class Airport {
  static async getAirports(limit = 10, page = 1) {
    const QUERY = `${GET_AIRPORTS_QUERY} LIMIT ${limit} OFFSET(${page} - 1) * ${limit};`;
    const { rows } = await db.query(QUERY);
    const airports = rows ?? [];
    return airports;
  }

  static async getAirportsByCity(cityName) {
    const QUERY = `${GET_AIRPORTS_QUERY} WHERE city = '${cityName}';`;
    const { rows } = await db.query(QUERY);
    const airports = rows ?? [];
    return airports;
  }

  static async getInboundSchedules(airportCode) {
    const QUERY =
      `
        SELECT DISTINCT ON(f.flight_no)
          f.flight_no AS "flightNo",
          f.scheduled_arrival:: timestamp::time AS "arrivalTime",
          r.days_of_week AS "daysOfWeek",
          r.departure_city AS "origin"
        FROM bookings.flights AS f
        INNER JOIN bookings.routes AS r ON f.flight_no = r.flight_no
        WHERE f.arrival_airport = '${airportCode}' AND f.status != 'Arrived'
        ORDER BY f.flight_no;
      `;

    const { rows } = await db.query(QUERY);
    const schedules = rows ?? [];
    return schedules;
  }

  static async getOutboundSchedules(airportCode) {
    const QUERY =
      `
        SELECT DISTINCT ON(f.flight_no)
          f.flight_no AS "flightNo",
          f.scheduled_departure:: timestamp::time AS "departureTime",
          r.days_of_week AS "daysOfWeek",
          r.arrival_city AS "destination"
        FROM bookings.flights AS f
        INNER JOIN bookings.routes AS r ON f.flight_no = r.flight_no
        WHERE f.departure_airport = '${airportCode}' AND f.status != 'Arrived'
        ORDER BY f.flight_no;
      `;
    const { rows } = await db.query(QUERY);
    const schedules = rows ?? [];
    return schedules;
  }
}

module.exports = Airport;

