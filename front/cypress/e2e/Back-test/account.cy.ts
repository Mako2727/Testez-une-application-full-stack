describe('Account API Sequence', () => {
  const baseUrl = 'http://localhost:9090';
  const loginCredentials = {
    email: 'yoga@studio.com',
    password: 'test!1234'
  };

  let token = '';

  it('should login, fetch user info, and verify email', () => {
    // Étape 1 : login
    cy.request({
      method: 'POST',
      url: `${baseUrl}/api/auth/login`,
      body: loginCredentials,
      failOnStatusCode: false
    }).then((res) => {
      expect(res.status).to.eq(200);
      expect(res.body).to.have.property('token');

      token = res.body.token;

      // Étape 2 : requête GET user/1 avec le token
      cy.request({
        method: 'GET',
        url: `${baseUrl}/api/user/1`,
        headers: {
          Authorization: `Bearer ${token}`
        }
      }).then((res2) => {
        expect(res2.status).to.eq(200);
        expect(res2.body).to.have.property('email', loginCredentials.email);
      });
    });
  });
});