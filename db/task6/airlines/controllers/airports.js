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

    if (type === 'inbound') {
      const schedules = await Airport.getInboundSchedules(airportCode);
      res.json(schedules);
    } else {
      const schedules = await Airport.getOutboundSchedules(airportCode);
      res.json(schedules);
    }
  } catch (error) {
    next(error);
  }
}


module.exports = { getAirports, getSchedulesByType };
