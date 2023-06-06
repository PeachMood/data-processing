const { celebrate, Joi } = require('celebrate');

const isCheckinRequest = celebrate({
  body: Joi.object().keys({
    flightId: Joi.number().required(),
    ticketNo: Joi.string().length(13).required()
  })
});

module.exports = { isCheckinRequest };
