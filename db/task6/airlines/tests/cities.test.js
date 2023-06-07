const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for cities resource', () => {
  it('GET "/cities?limit={limit}&page={page}" should return for correct parameters array of cities and correct status', async () => {
    const limit = 10;
    const page = 1;

    return request.get(`/cities?limit=${limit}&page=${page}`).then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array)
      expect(response.body).toHaveLength(limit);
    })
  });

  it('GET "/cities?limit={limit}&page={page}" should return for incorrect parameters BadRequest error with message', async () => {
    const limit = 10000;
    const page = -20;

    return request.get(`/cities?limit=${limit}&page=${page}`).then((response) => {
      expect(response.status).toBe(400);
      expect(response.body.message).toBeDefined();
    })
  });

  it('GET "/cities/{cityName}/airports" should return for correct city name non-empty array of airports and correct status', async () => {
    const cityName = 'Moscow';

    return request.get(`/cities/${cityName}/airports`).then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array)
      expect(response.body).not.toHaveLength(0);
    })
  });

  it('GET "/cities/{cityName}/airports" should return for non-existing city name NotFound error with message', async () => {
    const cityName = 'non-existing';

    return request.get(`/cities/${cityName}/airports`).then((response) => {
      expect(response.status).toBe(404);
      expect(response.body.message).toBeDefined();
    })
  });
});
