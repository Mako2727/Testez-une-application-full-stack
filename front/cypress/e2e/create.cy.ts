/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular, créer une session, et vérifier son affichage', () => {
    const uniqueSession = `test_${Date.now()}`;

   
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
    token: 'fake-jwt-token',
    type: 'Bearer',
    id: 48,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true
  }
    }).as('login');

cy.intercept('GET', '**/api/user/1', {
  statusCode: 200,
  body: {
    id: 1,
    email: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
    password: '',
    createdAt: '2023-01-01T00:00:00.000Z',
    updatedAt: '2023-01-02T00:00:00.000Z'
  }
}).as('getUser');

 
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessions');

   
    cy.intercept('POST', '/api/session', (req) => {
      expect(req.body.name).to.equal(uniqueSession);
      req.reply({
        statusCode: 201,
        body: {
          id: 123,
          name: uniqueSession,
          date: '2026-07-18',
          teacher: {
            id: 2,
            firstName: 'Hélène',
            lastName: 'THIERCELIN'
          },
          description: 'Voici le texte de description.'
        }
      });
    }).as('createSession');

   
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [{
        id: 123,
        name: uniqueSession,
        date: '2026-07-18',
        teacher: {
          id: 2,
          firstName: 'Hélène',
          lastName: 'THIERCELIN'
        },
        description: 'Voici le texte de description.'
      }]
    }).as('getSessionsAfterCreate');

    cy.intercept('GET', '/api/teacher', {
  statusCode: 200,
  body: [
    {
      id: 1,
      lastName: "DELAHAYE",
      firstName: "Margot",
      createdAt: "2025-07-13T23:23:23",
      updatedAt: "2025-07-13T23:23:23"
    },
    {
      id: 2,
      lastName: "THIERCELIN",
      firstName: "Hélène",
      createdAt: "2025-07-13T23:23:23",
      updatedAt: "2025-07-13T23:23:23"
    }
  ]
}).as('getTeachers');

   
    cy.visit('/login');

   
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    

    
    cy.url().should('include', '/sessions');

    cy.get('button[routerlink="create"]').should('be.visible').click();

    cy.contains('Create session').should('exist');

    cy.wait('@getTeachers');
    
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');

    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.wait(500);
cy.get('body').find('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait('@createSession');

   
    cy.wait('@getSessionsAfterCreate');

    
    cy.contains(uniqueSession).should('exist');
  });
});