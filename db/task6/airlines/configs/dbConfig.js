require('dotenv').config();
const Pool = require('pg').Pool;

const { DB_NAME, DB_HOST, DB_PORT, DB_USER, DB_PASSWORD } = process.env;

const pool = new Pool({
  database: DB_NAME,
  host: DB_HOST || 'localhost',
  port: DB_PORT || 5432,
  user: DB_USER,
  password: DB_PASSWORD
});

module.exports.db = pool;
