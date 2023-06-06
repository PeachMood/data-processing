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
    res.json(airports);
  } catch (error) {
    next(error);
  }
}

module.exports = { getCities, getAirportsByCity };
