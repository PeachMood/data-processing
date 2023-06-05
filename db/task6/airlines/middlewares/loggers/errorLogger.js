const winston = require('winston');
const expressWinston = require('express-winston');

const errorLogger = expressWinston.errorLogger({
  transports: [
    new winston.transports.File({ filename: 'logs/error.log' }),
  ],
  format: winston.format.json(),
});

module.exports = errorLogger;
