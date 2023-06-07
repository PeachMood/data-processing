const supertest = require('supertest');
const app = require('../app.js');
const request = supertest(app);

describe('Testing endpoints for cities resource', () => {
  it('GET "/cities?limit=10&page=1" should return array of 10 cities and correct status', async () => {
    return request.get('/cities?limit=10&page=1').then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array)
      expect(response.body).toHaveLength(10);
    })
  });

  it('GET "/cities/Moscow/airports" should return non-empty array of airports and correct status', async () => {
    return request.get('/cities/Moscow/airports').then((response) => {
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toMatch('application/json');
      expect(response.body).toBeInstanceOf(Array)
      expect(response.body).not.toHaveLength(0);
    })
  });
});
