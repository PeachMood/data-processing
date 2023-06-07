const Flight = require('../models/Flight');

const checkIn = async (req, res, next) => {
  try {
    const { ticketNo, flightId } = req.body;
    const boardingPass = await Flight.checkIn({ ticketNo, flightId });
    res.status(201);
    res.json(boardingPass);
  } catch (error) {
    next(error);
  }
}

module.exports = { checkIn };
