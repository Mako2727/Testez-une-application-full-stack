describe('Test login via interface Angular', () => {
  beforeEach(() => {
    // Mock du login
    cy.intercept('POST', 'http://localhost:9090/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        userId: 1
      }
    }).as('login');

    // Mock de la requête GET /api/user/1
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

    // Mock des sessions (retourné sur la page /sessions)
    cy.intercept('GET', 'http://localhost:9090/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessions');
  });

  it('doit se connecter via le formulaire Angular et naviguer sur /me', () => {
    cy.visit('http://localhost:4200/login');

// Remplir le formulaire et soumettre
cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
cy.get('input[formcontrolname="password"]').type('test!1234');
cy.get('button[type="submit"]').click();

// Attendre la navigation vers /sessions
cy.url().should('include', '/sessions');

// Cliquer sur le lien vers /me
cy.get('span[routerlink="me"]').should('be.visible').click();



// Vérifier que l’URL est bien /me
cy.url().should('include', '/me');

// Puis vérifier que les infos d’utilisateur s’affichent (exemple)
cy.contains('You are admin').should('exist');
  });
});