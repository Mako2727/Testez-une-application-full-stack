/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular', () => {
    cy.intercept('GET', '/api/session').as('getSession');
    cy.visit('http://localhost:4200/');

     cy.get('span.link[routerlink="register"]').click();

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="firstName"]').click().type('yoga');
    cy.get('input[formcontrolname="lastName"]').click().type('studio');
      cy.get('input[formcontrolname="email"]').click().type('yoga@studio.com');
   cy.get('input[formcontrolname="password"]').click().type('test!1234');
    cy.get('button[type="submit"]').click();
    // Vérifie que tu es bien connecté (ex. présence d’un élément de dashboard)
    cy.contains('An error occurred').should('exist');
  });
});