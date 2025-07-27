describe('Test login mock simple', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        userId: 1
      }
    }).as('login');
  });

  it('devrait envoyer la requête login et réussir', () => {
    cy.visit('http://localhost:4200/login');
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com');
    cy.get('input[formcontrolname="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);

    cy.get('span.link').contains('Logout').click();
cy.wait(1000);
cy.url().should('eq', 'http://localhost:4200/');
  });
});