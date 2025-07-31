/// <reference types="cypress" />
describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular, créer, voir détail et supprimer une session', () => {
    const uniqueSession = `test_${Date.now()}`;

    // Mock login POST
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

    // Mock get user connecté
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

 

cy.intercept('POST', '/api/session', (req) => {
 
  expect(req.body).to.have.property('date');
  expect(req.body).to.have.property('teacher_id');
  expect(req.body).to.have.property('description');
 expect(req.body).to.have.property('name');


  req.reply({
    statusCode: 201,
    body: {
      id: 29,
      ...req.body,
      users: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }
  });
}).as('createSession');

    // Après création, liste sessions avec la session créée
const sessionsMock = [
  {
    id: 29,
    name: uniqueSession,
    date: '2025-08-03T00:00:00.000+00:00',
    teacher_id: 2,
    description: 'hgghgfh',
    users: [],
    createdAt: '2025-07-31T01:11:08',
    updatedAt: '2025-07-31T01:11:08'
  },
  {
    id: 30,
    name: 'hghf',
    date: '2025-08-03T00:00:00.000+00:00',
    teacher_id: 2,
    description: 'gfhfg',
    users: [],
    createdAt: '2025-07-31T01:17:52',
    updatedAt: '2025-07-31T01:17:52'
  }
];



cy.intercept('GET', '**/api/session', {
  statusCode: 200,
  body: sessionsMock
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

cy.intercept('GET', '/api/session/29', {
  statusCode: 200,
  body: {
    id: 29,
    name: uniqueSession,
    date: '2026-07-18',
    teacher_id: 2,
    description: 'Voici le texte de description.',
    users: [],
    createdAt: '2025-07-31T01:11:07.755',
    updatedAt: '2025-07-31T01:11:07.775'
  }
}).as('getSessionDetail');

    cy.intercept('GET', '/api/teacher/2', {
  statusCode: 200,
  body: {
    id: 2,
    lastName: "THIERCELIN",
    firstName: "Hélène",
    createdAt: "2025-07-13T23:23:23",
    updatedAt: "2025-07-13T23:23:23"
  }
}).as('getTeacherDetail');

    // Suppression session DELETE
    cy.intercept('DELETE', '/api/session/29', {
      statusCode: 204,
      body: {}
    }).as('deleteSession');

    // Après suppression, liste sessions vide à nouveau
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessionsAfterDelete');

    // --- Test UI ---

    cy.visit('http://localhost:4200/login');

    // Login
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    //cy.wait('@getUser');

    cy.url().should('include', '/sessions');

    // Bouton créer session
    cy.get('button[routerlink="create"]').should('be.visible').click();

    cy.contains('Create session').should('exist');

 cy.wait('@getTeachers');
    // Formulaire de création de session
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');

    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.wait(500);
cy.get('body').find('mat-option').contains('Hélène THIERCELIN').click();

    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait(500);
    cy.wait('@createSession');
    cy.wait(500);

 
    cy.wait('@getSessionsAfterCreate');

    cy.contains(uniqueSession).should('exist');

    // Cliquer sur le détail de la session
    cy.contains('mat-card-title', uniqueSession)
      .closest('mat-card')
      .contains('span.ml1', 'Detail')
      .click();


cy.wait('@getTeacherDetail');
    cy.wait('@getSessionDetail');
    cy.wait(500);

    // Supprimer la session
    cy.get('button').contains('Delete').click();

    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');

    // La session ne doit plus exister
    cy.contains('mat-card-title', uniqueSession).should('not.exist');
  });
});