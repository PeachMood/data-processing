const { NotFound } = require('http-errors');

const Airport = require('../models/Airport');

const getAirports = async (req, res, next) => {
  try {
    const { limit, page } = req.query;
    const airports = await Airport.getAirports(limit, page);
    res.json(airports);
  } catch (error) {
    next(error);
  }
}

const getSchedulesByType = async (req, res, next) => {
  try {
    const { airportCode } = req.params;
    const { type } = req.query;

    let schedules;
    if (type === 'inbound') {
      schedules = await Airport.getInboundSchedules(airportCode);
    } else {
      schedules = await Airport.getOutboundSchedules(airportCode);
    }

    if (schedules.length === 0) {
      throw NotFound('No schedules were found for the specified airport');
    }
    res.json(schedules);
  } catch (error) {
    next(error);
  }
}


module.exports = { getAirports, getSchedulesByType };
