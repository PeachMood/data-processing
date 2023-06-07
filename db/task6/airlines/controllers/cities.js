const { NotFound } = require('http-errors');

const City = require('../models/City');
const Airport = require('../models/Airport');

const getCities = async (req, res, next) => {
  try {
    const { limit, page } = req.query;
    const cities = await City.getCities(limit, page);
    res.json(cities);
  } catch (error) {
    next(error);
  }
}

const getAirportsByCity = async (req, res, next) => {
  try {
    const { cityName } = req.params;
    const airports = await Airport.getAirportsByCity(cityName);
    if (airports.length === 0) {
      throw NotFound('No cities were found for the specified city');
    }
    res.json(airports);
  } catch (error) {
    next(error);
  }
}

module.exports = { getCities, getAirportsByCity };
