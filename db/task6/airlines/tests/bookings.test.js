const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for bookings resource', () => {
  it('POST "/bookings" should return for correct response created booking with ticket number and correct status', async () => {
    const booking = {
      flightNo: 'PG0222',
      flightDate: '2017-08-20',
      fareConditions: 'Economy',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      expect(response.status).toBe(201);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toHaveProperty('bookRef');
      expect(response.body).toHaveProperty('ticketNo');
    })
  });

  it('POST "/bookings" should return for incorrect request body BadRequest error with message', async () => {
    const booking = {
      flightNo: null,
      flightDate: null
    };
    return request.post('/bookings').send(booking).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('POST "/bookings" should return for arrived flight BadRequest error with message', async () => {
    const booking = {
      flightNo: 'PG0403',
      flightDate: '2017-06-13',
      fareConditions: 'Economy',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('POST "/bookings" should return BadRequest error with message if there are no available seats for the flight', async () => {
    const booking = {
      flightNo: 'PG0222',
      flightDate: '2017-08-20',
      fareConditions: 'Business',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('POST "/bookings" should return for non-existing flight NotFound error with message', async () => {
    const booking = {
      flightNo: '000000',
      flightDate: '2017-08-20',
      fareConditions: 'Economy',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      expect(response.status).toBe(404);
      expect(response.body.message).toBeDefined();
    })
  });
});
