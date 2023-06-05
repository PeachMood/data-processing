const { isHttpError, InternalServerError } = require('http-errors');

const errorHandler = (err, req, res, next) => {
  let error = err;
  if (!isHttpError(err)) {
    error = new InternalServerError(`Ошибка сервера: ${err.message}`);
  }
  res.status(error.status);
  res.json({ message: error.message });
};

module.exports = errorHandler;
