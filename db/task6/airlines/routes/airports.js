const express = require('express');
const router = express.Router();

const validator = require('../middlewares/validators/airportsValidator');
const controller = require('../controllers/airports');

router.get('/', validator.isPaginationQuery, controller.getAirports);
router.get('/:airportCode/schedules', validator.areScheduleParams, controller.getSchedulesByType);

module.exports = router;
