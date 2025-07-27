describe('Test login via interface Angular', () => {
  it('doit se connecter via le formulaire Angular, créer, voir détail et supprimer une session', () => {
    const uniqueSession = `test_${Date.now()}`;

    // Mock login POST
    cy.intercept('POST', 'http://localhost:9090/api/auth/login', {
      statusCode: 200,
      body: { token: 'fake-jwt-token', userId: 1 }
    }).as('login');

    // Mock get user connecté
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

    // Avant création, liste sessions vide
    cy.intercept('GET', 'http://localhost:9090/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessions');

    // Création de session POST
    cy.intercept('POST', 'http://localhost:9090/api/session', (req) => {
      expect(req.body.name).to.equal(uniqueSession);
      req.reply({
        statusCode: 201,
        body: {
          id: 123,
          name: uniqueSession,
          date: '2026-07-18',
          teacher: { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' },
          description: 'Voici le texte de description.'
        }
      });
    }).as('createSession');

    // Après création, liste sessions avec la session créée
    cy.intercept('GET', 'http://localhost:9090/api/session', {
      statusCode: 200,
      body: [{
        id: 123,
        name: uniqueSession,
        date: '2026-07-18',
        teacher: { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' },
        description: 'Voici le texte de description.'
      }]
    }).as('getSessionsAfterCreate');

    // Détail session GET
    cy.intercept('GET', 'http://localhost:9090/api/session/123', {
      statusCode: 200,
      body: {
        id: 123,
        name: uniqueSession,
        date: '2026-07-18',
        teacher: { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' },
        description: 'Voici le texte de description.'
      }
    }).as('getSessionDetail');

    // Suppression session DELETE
    cy.intercept('DELETE', 'http://localhost:9090/api/session/123', {
      statusCode: 204,
      body: {}
    }).as('deleteSession');

    // Après suppression, liste sessions vide à nouveau
    cy.intercept('GET', 'http://localhost:9090/api/session', {
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
    cy.wait('@getUser');

    cy.url().should('include', '/sessions');

    // Bouton créer session
    cy.get('button[routerlink="create"]').should('be.visible').click();

    cy.contains('Create session').should('exist');

    // Formulaire création session
    cy.get('input[formcontrolname="name"]').type(uniqueSession);
    cy.get('input[formcontrolname="date"]').invoke('val', '2026-07-18').trigger('input');
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formcontrolname="description"]').type('Voici le texte de description.', { force: true });
    cy.get('button[type="submit"]').contains('Save').click();

    cy.wait('@createSession');
    cy.wait('@getSessionsAfterCreate');

    cy.contains(uniqueSession).should('exist');

    // Cliquer sur le détail de la session
    cy.contains('mat-card-title', uniqueSession)
      .closest('mat-card')
      .contains('span.ml1', 'Detail')
      .click();

    cy.wait('@getSessionDetail');

    // Supprimer la session
    cy.get('button').contains('Delete').click();

    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');

    // La session ne doit plus exister
    cy.contains('mat-card-title', uniqueSession).should('not.exist');
  });
});