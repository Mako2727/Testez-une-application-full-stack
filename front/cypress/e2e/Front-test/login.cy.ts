/// <reference types="cypress" />
import { FRONT_URL } from '../../support/urls';

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular', () => {
    cy.visit('http://localhost:4200/login');

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    // Vérifie que tu es bien connecté (ex. présence d’un élément de dashboard)
    cy.contains('Rentals available').should('exist');
  });
});