const express = require('express');
const router = express.Router();

const validator = require('../middlewares/validators/bookingsValidator');
const controller = require('../controllers/bookings');

router.post('/', validator.isBookingRequest, controller.createBooking);

module.exports = router;
