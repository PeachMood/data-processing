const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for bookings resource', () => {
  it('POST "/bookings" should return created booking with ticket number and correct status', async () => {
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
});
