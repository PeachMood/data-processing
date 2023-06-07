const express = require('express');

const requestLogger = require('./middlewares/loggers/requestLogger');
const errorLogger = require('./middlewares/loggers/errorLogger.js');
const router = require('./routes');
const errorHandler = require('./middlewares/errorHandler.js');

const app = express();

app.use(express.json());
app.use(requestLogger);
app.use(router);
app.use(errorLogger);
app.use(errorHandler);

module.exports = app;
