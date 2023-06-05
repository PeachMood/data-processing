const { db } = require('../configs/dbConfig');

const getInboundSchedules = async (airportCode) => {
  const QUERY =
    ```
      SELECT DISTINCT ON(f.flight_no)
        f.flight_no AS flightNo,
        f.scheduled_arrival::timestamp::time AS arrivalTime,
        r.days_of_week AS daysOfWeek,
        r.departure_city AS origin
      FROM bookings.flights AS f
      INNER JOIN bookings.routes AS r ON f.flight_no = r.flight_no
      WHERE f.arrival_airport=${airportCode} and f.status != 'Arrived'
      ORDER BY f.flight_no;
    ```;
  const { rows } = db.query(QUERY);
  const schedules = rows ?? [];
  return schedules;
}

const getOutboundSchedules = async (airportCode) => {
  const QUERY =
    ```
    SELECT DISTINCT ON(f.flight_no)
      f.flight_no AS flightNo,
      f.scheduled_arrival::timestamp::time AS arrivalTime,
      r.days_of_week AS daysOfWeek,
      r.departure_city AS origin
    FROM bookings.flights AS f
    INNER JOIN bookings.routes AS r ON f.flight_no = r.flight_no
    WHERE f.departure_airport=${airportCode} and f.status != 'Arrived'
    ORDER BY f.flight_no;
  ```;
  const { rows } = db.query(QUERY);
  const schedules = rows ?? [];
  return schedules;
}

const getSchedulesByAirport = async (airportCode) => {
  const QUERY =
    ```
      SELECT DISTINCT ON(f.flight_no)
        f.flight_no AS flightNo,
        f.scheduled_arrival::timestamp::time AS arrivalTime,
        f.scheduled_departure::timestamp::time AS departureTime,
        r.departure_city AS origin,
        r.arrival_city AS destination,
        r.days_of_week AS daysOfWeek
      FROM bookings.flights AS f
      INNER JOIN bookings.routes AS r ON f.flight_no = r.flight_no
      WHERE (f.arrival_airport=${airportCode} OR f.departure_airport=${airportCode}) and f.status != 'Arrived'
      ORDER BY f.flight_no;
    ```;
  const { rows } = db.query(QUERY);
  const schedules = rows ?? [];
  return schedules;
}

const getSchedulesByType = async (airportCode, type) => {
  if (type === 'inbound') {
    return getInboundSchedules(airportCode);
  }
  if (type === 'outbound') {
    return getOutboundSchedules(airportCode);
  }
  return getSchedulesByAirport(airportCode);
}

exports.module = { getSchedulesByType };
