const { db } = require('../configs/dbConfig');

const GET_AIRPORTS_QUERY =
  ```
    SELECT
      airport_code AS code,
      airport_code AS name,
      city,
      coordinates[0] AS longitude,
      coordinates[1] AS latitude,
      timezone
    FROM bookings.airports
  ```;

const getAirports = async (limit = 10, page = 1) => {
  const QUERY = `${GET_AIRPORTS_QUERY} LIMIT ${limit} OFFSET ${(page - 1) * limit};`;
  const { rows } = await db.query(QUERY);
  const airports = rows ?? [];
  return airports;
}

const getAirportsByCity = async (cityName) => {
  const QUERY = `${GET_AIRPORTS_QUERY} WHERE city=${cityName}`;
  const { rows } = await db.query(QUERY);
  const airports = rows ?? [];
  return airports;
}

module.exports = { getAirports, getAirportsByCity };

