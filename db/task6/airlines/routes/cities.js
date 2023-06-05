const express = require('express');

const router = express.Router();

router.get('/');
router.get('/:cityName/airports');

module.exports = router;
