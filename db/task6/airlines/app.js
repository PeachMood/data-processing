const express = require('express');

const appConfig = require('./configs/appConfig');

const requestLogger = require('./middlewares/loggers/requestLogger');
const errorLogger = require('./middlewares/loggers/errorLogger.js');
const errorHandler = require('./middlewares/errorHandler.js');

const app = express();

app.use(express.json());
app.use(requestLogger)
app.use(errorLogger);
app.use(errorHandler);

app.listen(appConfig.port);
