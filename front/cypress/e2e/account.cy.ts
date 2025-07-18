/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular', () => {
    cy.intercept('GET', '/api/session').as('getSession');
    cy.visit('http://localhost:4200/login');

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait(1000); // à adapter selon ta latence
    cy.get('span[routerlink="me"]').should('be.visible').click();

    // Vérifie que tu es bien connecté (ex. présence d’un élément de dashboard)
    cy.contains('yoga@studio.com').should('exist');
  });
});