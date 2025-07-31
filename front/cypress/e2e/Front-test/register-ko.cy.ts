/// <reference types="cypress" />
describe('Test register via interface Angular', () => {
  beforeEach(() => {
    // Interception POST register avec une réponse d'erreur simulée
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Erreur lors de l\'inscription' }
    }).as('register');
  });

  it('doit afficher une erreur lors de l\'inscription', () => {
    cy.visit('http://localhost:4200/');

    cy.get('span.link[routerlink="register"]').click();

    cy.get('input[formcontrolname="firstName"]').click().type('yoga');
    cy.get('input[formcontrolname="lastName"]').click().type('studio');
    cy.get('input[formcontrolname="email"]').click().type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').click().type('test!1234');
    cy.get('button[type="submit"]').click();

    // On attend que la requête register mockée soit terminée
    cy.wait('@register');

    // Vérifie que le message d'erreur s'affiche
    cy.contains('An error occurred').should('exist');
  });
});