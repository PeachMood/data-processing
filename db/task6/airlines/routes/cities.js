const express = require('express');
const router = express.Router();

const validator = require('../middlewares/validators/citiesValidator');
const controller = require('../controllers/cities');

router.get('/', validator.isPaginationQuery, controller.getCities);
router.get('/:cityName/airports', validator.areCityParams, controller.getAirportsByCity);

module.exports = router;
