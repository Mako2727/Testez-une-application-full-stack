/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular, créer une session, et vérifier son affichage', () => {
    const uniqueSession = `test_${Date.now()}`;
    let sessionDeleted = false; 

    // Mock du login POST avec URL relative
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

 cy.intercept('GET', '/api/session', (req) => {
      if (sessionDeleted) {
        req.reply({
          statusCode: 200,
          body: [] // plus aucune session après suppression
        });
      } else {
        req.reply({
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
        });
      }
    }).as('getSessions');

    // Mock création de session POST
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

 // Détail de la session
    cy.intercept('GET', '/api/session/123', {
      statusCode: 200,
      body: {
        id: 29,
        name: uniqueSession,
        date: '2026-07-18',
        teacher_id: 2,
        description: 'Voici le texte de description.',
        users: [],
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
    }).as('getSessionDetail');

        cy.intercept('GET', '/api/teacher/2', {
      statusCode: 200,
      body: {
        id: 2,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: '2025-07-13T23:23:23',
        updatedAt: '2025-07-13T23:23:23'
      }
    }).as('getTeacherDetail');

 cy.intercept('DELETE', '/api/session/123', (req) => {
      sessionDeleted = true; // <-- important !
      req.reply({
        statusCode: 204,
        body: {}
      });
    }).as('deleteSession');

  

    // Visite relative en fonction du baseUrl (ex: http://localhost:4200)
    cy.visit('/login');

    // Remplissage du formulaire login
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    //cy.wait('@getUser');

    // On arrive sur /sessions
    cy.url().should('include', '/sessions');

    cy.get('button[routerlink="create"]').should('be.visible').click();

    cy.contains('Create session').should('exist');

    cy.wait('@getTeachers');
    // Formulaire de création de session
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');

    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.wait(500);
cy.get('body').find('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait('@createSession');

    // Après création, on re-récupère la liste
    cy.wait('@getSessions');

    // Vérifie que la session unique apparait bien dans la liste
    cy.contains(uniqueSession).should('exist');

      // Détail
    cy.contains('mat-card-title', uniqueSession)
      .closest('mat-card')
      .contains('span.ml1', 'Detail')
      .click();

      cy.wait('@getSessionDetail');
       cy.wait('@getTeacherDetail');

  // Suppression
    cy.get('button').contains('Delete').click();
    cy.wait('@deleteSession');

     cy.wait('@getSessions');

  // Vérification suppression
    cy.contains('mat-card-title', uniqueSession).should('not.exist');


  });
});