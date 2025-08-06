/// <reference types="cypress" />
describe('Test register via interface Angular', () => {
  beforeEach(() => {
    
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'User registered successfully' }
    }).as('register');
  });

  it('doit s’inscrire puis revenir sur la page login', () => {
    const uniqueEmail = `test_${Date.now()}@123.fr`;

    cy.visit('http://localhost:4200/');

    cy.get('span.link[routerlink="register"]').click();

    cy.get('input[formcontrolname="firstName"]').click().type('yoga');
    cy.get('input[formcontrolname="lastName"]').click().type('studio');
    cy.get('input[formcontrolname="email"]').click().type(uniqueEmail);
    cy.get('input[formcontrolname="password"]').click().type('test!1234');

    cy.get('button[type="submit"]').click();

    
    cy.wait('@register');

    
    cy.url().should('include', '/login');
  });
});