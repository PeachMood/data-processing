const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for airports resource', () => {
  it('GET "/airports?limit={limit}&page={page}" should return for correct parameters array of airports and correct status', async () => {
    const limit = 10;
    const page = 1;

    return request.get(`/airports?limit=${limit}&page=${page}`).then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array);
      expect(response.body).toHaveLength(limit);
    })
  });

  it('GET "/airports?limit={limit}&page={page}" should return for incorrect parameters BadRequest error with messaage', async () => {
    const limit = 10000;
    const page = -20;

    return request.get(`/airports?limit=${limit}&page=${page}`).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('GET "/airports/{airportCode}/schedules?type=inbound" should return for correct airport code array of inbound schedules and correct status', async () => {
    const airportCode = 'SVO';
    const type = 'inbound';

    return request.get(`/airports/${airportCode}/schedules?type=${type}`).then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array);
      expect(response.body).not.toHaveLength(0);
      response.body.map(schedule => {
        expect(schedule).toHaveProperty('arrivalTime');
        expect(schedule).toHaveProperty('origin');
      });
    })
  });

  it('GET "/airports/{airportCode}/schedules?type=outbound" should return for correct airport code array of outbound schedules and correct status', async () => {
    const airportCode = 'SVO';
    const type = 'outbound';

    return request.get(`/airports/${airportCode}/schedules?type=${type}`).then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array);
      expect(response.body).not.toHaveLength(0);
      response.body.map(schedule => {
        expect(schedule).toHaveProperty('departureTime');
        expect(schedule).toHaveProperty('destination');
      });
    })
  });

  it('GET "/airports/{airportCode}/schedules?type={type}" should return for incorrect schedule type BadRequest error with message', async () => {
    const airportCode = 'SVO';
    const type = 'incorrect';

    return request.get(`/airports/${airportCode}/schedules?type=${type}`).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('GET "/airports/{airportCode}/schedules?type={type}" should return for non-existing airport code NotFound error with message', async () => {
    const airportCode = 'non-existing';
    const type = 'inbound';

    return request.get(`/airports/${airportCode}/schedules?type=${type}`).then((response) => {
      expect(response.status).toBe(404);
      expect(response.body.message).toBeDefined();
    })
  });
});
