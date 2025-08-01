/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        id: 1
      }
    }).as('login');

    
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        email: 'yoga@studio.com'
      }
    }).as('getUserById');



    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessions');
  });

  it('doit se connecter via le formulaire Angular et naviguer sur /me', () => {
    cy.visit('/login');

    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');

    cy.url().should('include', '/sessions');

    cy.get('span[routerlink="me"]').should('be.visible').click();
   cy.wait('@getUserById');
   
    
  });
});