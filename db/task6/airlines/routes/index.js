const express = require('express');

const router = express.Router();

const citiesRouter = require('./cities');

router.use('/cities', citiesRouter);

module.exports = router;
