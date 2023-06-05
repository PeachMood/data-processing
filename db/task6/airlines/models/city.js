const { db } = require('../configs/dbConfig');

const getCities = async (limit = 10, page = 1) => {
  const QUERY = `SELECT city FROM bookings.airports LIMIT ${limit} OFFSET (${page} - 1) * ${limit};`;
  const { rows } = await db.query(QUERY);
  const cities = rows ?? [];
  return cities;
}

exports.module = { getCities };
