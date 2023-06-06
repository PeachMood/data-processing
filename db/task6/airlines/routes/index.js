const express = require('express');

const router = express.Router();

const citiesRouter = require('./cities');
const airportsRouter = require('./airports');
const bookingsRouter = require('./bookings');
const flightsRouter = require('./flights');

router.use('/cities', citiesRouter);
router.use('/airports', airportsRouter);
router.use('/bookings', bookingsRouter);
router.use('/flights', flightsRouter);

module.exports = router;
