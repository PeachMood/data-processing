const Booking = require('../models/Booking');

const createBooking = async (req, res, next) => {
  try {
    const { flightNo, flightDate, fareConditions, passengerId, passengerName } = req.body;
    const booking = await Booking.createBooking({ flightNo, flightDate, fareConditions, passengerId, passengerName });
    res.json(booking);
  } catch (error) {
    next(error);
  }
}

module.exports = { createBooking };
