const { db } = require('../configs/dbConfig');

class City {
  static async getCities(limit = 10, page = 1) {
    const QUERY = `SELECT city FROM bookings.airports LIMIT ${limit} OFFSET (${page} - 1) * ${limit};`;
    const { rows } = await db.query(QUERY);
    const cities = rows?.map(row => row.city) ?? [];
    return cities;
  }
}

module.exports = City;
