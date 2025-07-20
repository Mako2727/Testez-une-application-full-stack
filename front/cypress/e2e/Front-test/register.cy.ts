/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular', () => {

    const uniqueEmail = `test_${Date.now()}@123.fr`;
    cy.intercept('GET', '/api/session').as('getSession');
    cy.visit('http://localhost:4200/');

     cy.get('span.link[routerlink="register"]').click();

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="firstName"]').click().type('yoga');
    cy.get('input[formcontrolname="lastName"]').click().type('studio');
      cy.get('input[formcontrolname="email"]').click().type(uniqueEmail);
   cy.get('input[formcontrolname="password"]').click().type('test!1234');
    cy.get('button[type="submit"]').click();
    // je verifie que je reviens bien sur la page login 

     // Ici on vérifie que l’URL contient /admin
    cy.url().should('include', '/login');
  });
});