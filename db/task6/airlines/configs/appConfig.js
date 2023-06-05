require('dotenv').config();

const { NODE_ENV, PORT } = process.env;

const appConfig = {
  mode: NODE_ENV || 'development',
  port: PORT || 3000,
};

module.exports = appConfig;
