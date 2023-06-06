const { celebrate, Joi } = require('celebrate');

const isBookingRequest = celebrate({
  body: Joi.object().keys({
    flightDate: Joi.string().required(),
    flightNo: Joi.string().length(6).required(),
    fareConditions: Joi.string().valid('Economy', 'Comfort', 'Business').required(),
    passengerId: Joi.string().required(),
    passengerName: Joi.string().required()
  })
});

module.exports = { isBookingRequest };
