const { celebrate, Joi } = require('celebrate');

const isPaginationQuery = celebrate({
  query: {
    limit: Joi.number().min(1).max(100).default(10),
    page: Joi.number().min(1).default(1)
  }
});

const areScheduleParams = celebrate({
  params: Joi.object().keys({
    airportCode: Joi.string().required(),
  }),
  query: {
    type: Joi.string().valid('inbound', 'outbound').required()
  }
});

module.exports = { isPaginationQuery, areScheduleParams };
