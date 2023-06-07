const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for flights resource', () => {
  it('POST "/flights/checkins" should return for correct request created boarding pass with seat number and correct status', async () => {
    const booking = {
      flightNo: 'PG0222',
      flightDate: '2017-08-16',
      fareConditions: 'Economy',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      const booking = response.body;
      const boardingPass = {
        ticketNo: booking.ticketNo,
        flightId: 1844
      };
      request.post('/flights/checkins').send(boardingPass).then(response => {
        expect(response.status).toBe(201);
        expect(response.headers['content-type']).toMatch('application/json');
        expect(response.body).toHaveProperty('boardingNo');
        expect(response.body).toHaveProperty('seatNo');
      });
    });
  });

  it('POST "/flights/checkins" should return BadRequest error with message when trying to re-check in for a flight', async () => {
    const boardingPass = {
      ticketNo: 'f400a56d43a84',
      flightId: 1844
    };
    return request.post('/flights/checkins').send(boardingPass).then(response => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    });
  });

  it('POST "/flights/checkins" should return for non-existing flight NotFound error with message', async () => {
    const booking = {
      flightNo: 'PG0222',
      flightDate: '2017-08-16',
      fareConditions: 'Economy',
      passengerId: '_01',
      passengerName: 'Test user',
    };
    return request.post('/bookings').send(booking).then((response) => {
      const booking = response.body;
      const boardingPass = {
        ticketNo: booking.ticketNo,
        flightId: 9999
      };
      return request.post('/flights/checkins').send(boardingPass).then(response => {
        expect(response.status).toBe(404);
        expect(response.body.message).toBeDefined();
      });
    });
  });
});
