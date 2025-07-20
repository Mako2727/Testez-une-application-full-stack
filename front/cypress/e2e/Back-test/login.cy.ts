/// <reference types="cypress" />
import { API_URL } from '../../support/urls';

describe('API Test - Login', () => {
  it('should return a JWT token when credentials are correct', () => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/auth/login',
      body: {
        email: 'yoga@studio.com',
        password: 'test!1234'
      },
      failOnStatusCode: false // pour gérer les erreurs HTTP
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('token');
      expect(response.body.token).to.be.a('string');
    });
  });
});