const { isHttpError, BadRequest, InternalServerError } = require('http-errors');
const { isCelebrateError, } = require('celebrate');

const getErrorMessage = (error) => {
  for (const [segment, joiError] of error.details.entries()) {
    return joiError.message;
  }
};

const errorHandler = (err, req, res, next) => {
  let response = err;

  if (isCelebrateError(err)) {
    response = new BadRequest(getErrorMessage(err));
  } else if (!isHttpError(err)) {
    response = new InternalServerError(`Ошибка сервера: ${err.message}`);
  }
  res.status(response.status);
  res.json({ message: response.message });
};

module.exports = errorHandler;
