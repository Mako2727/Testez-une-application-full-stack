/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular', () => {
    const uniqueSession = `test_${Date.now()}`;
    cy.intercept('GET', '/api/session').as('getSession');
    cy.visit('http://localhost:4200/login');

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait(1000); // à adapter selon ta latence
    cy.get('button[routerlink="create"]').should('be.visible').click();

    // Vérifie que tu es bien connecté (ex. présence d’un élément de dashboard)
    cy.contains('Create session').should('exist');

    // Adapte les sélecteurs aux noms de tes champs
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');
   
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait(1000); // à adapter selon ta latence
    cy.contains(uniqueSession).should('exist');

    cy.contains('mat-card-title', uniqueSession)
    .closest('mat-card')
    .contains('span.ml1', 'Detail')
    .click();

    cy.wait(1000); 
    cy.get('button')
    .contains('Delete')
    .click();

    cy.contains('mat-card-title', uniqueSession)
    .should('not.exist');

  });
});