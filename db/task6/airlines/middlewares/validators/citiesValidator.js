const { celebrate, Joi } = require('celebrate');

const isPaginationQuery = celebrate({
  query: {
    limit: Joi.number().min(1).max(100).default(10),
    page: Joi.number().min(1).default(1)
  }
});

const areCityParams = celebrate({
  params: Joi.object().keys({
    cityName: Joi.string().required(),
  }),
});

module.exports = { isPaginationQuery, areCityParams };
