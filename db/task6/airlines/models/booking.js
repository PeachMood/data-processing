const { db } = require('../configs/dbConfig');

const createBooking = async (booking) => {
  try {
    const client = await pool.connect();
    await client.query('BEGIN');
  } catch (error) {
    console.log(error)
  }
};
