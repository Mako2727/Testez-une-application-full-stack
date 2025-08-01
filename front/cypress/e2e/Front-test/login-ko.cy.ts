/// <reference types="cypress" />
describe('Login spec - échec de connexion', () => {
  it('Login échoue avec mauvais identifiants', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        path: '/api/auth/login',
        error: 'Unauthorized',
        message: 'Bad credentials',
        status: 401
      }
    }).as('loginFail')

    cy.get('input[formControlName=email]').type("test@test.com")
    cy.get('input[formControlName=password]').type(`${"azerty"}{enter}{enter}`)

    // Vérifie que l'utilisateur reste sur la page de login
    cy.url().should('include', '/login')

  })
})