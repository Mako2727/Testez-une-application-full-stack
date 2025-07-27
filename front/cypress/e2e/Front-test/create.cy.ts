/// <reference types="cypress" />

describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular, créer une session, et vérifier son affichage', () => {
    const uniqueSession = `test_${Date.now()}`;

    // Mock du login POST
    cy.intercept('POST', 'http://localhost:9090/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        userId: 1
      }
    }).as('login');

    // Mock récupération user connecté
    cy.intercept('GET', 'http://localhost:9090/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'yoga@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
        password: '',
        createdAt: '2023-01-01T00:00:00.000Z',
        updatedAt: '2023-01-02T00:00:00.000Z'
      }
    }).as('getUser');

    // Mock récupération liste sessions avant création
    cy.intercept('GET', 'http://localhost:9090/api/session', {
      statusCode: 200,
      body: [] // Pas de session au début
    }).as('getSessions');

    // Mock création de session POST
    cy.intercept('POST', 'http://localhost:9090/api/session', (req) => {
      // On peut vérifier les données envoyées si besoin
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

    // Mock récupération sessions après création (avec la session créée)
    cy.intercept('GET', 'http://localhost:9090/api/session', {
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

    cy.visit('http://localhost:4200/login');

    // Remplissage du formulaire login
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    cy.wait('@getUser');

    // On arrive sur /sessions
    cy.url().should('include', '/sessions');

    cy.get('button[routerlink="create"]').should('be.visible').click();

    cy.contains('Create session').should('exist');

    // Formulaire de création de session
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');

    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();

    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait('@createSession');

    // Après création, on re-récupère la liste
    cy.wait('@getSessionsAfterCreate');

    // Vérifie que la session unique apparait bien dans la liste
    cy.contains(uniqueSession).should('exist');
  });
});