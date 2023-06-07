const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for airports resource', () => {
  it('GET "/airports?limit=10&page=1" should return array of 10 airports and correct status', async () => {
    return request.get('/airports?limit=10&page=1').then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array);
      expect(response.body).toHaveLength(10);
    })
  });

  it('GET "/airports/SVO/schedules?type=inbound" should return array of inbound schedules and correct status', async () => {
    return request.get('/airports/SVO/schedules?type=inbound').then((response) => {
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

  it('GET "/airports/SVO/schedules?type=outbound" should return array of outbound schedules and correct status', async () => {
    return request.get('/airports/SVO/schedules?type=outbound').then((response) => {
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
});
