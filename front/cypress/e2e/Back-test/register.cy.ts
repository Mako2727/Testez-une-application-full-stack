describe('Register user as admin with preset token', () => {
  const adminToken = 'ton_token_admin_ici';

  it('should register a new user with admin token and get success message', () => {
    const uniqueEmail = `test_${Date.now()}@123.fr`;
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/auth/register',
     
      body: {
        email: uniqueEmail,
        password: "test123",
        firstName:"marius28",
        lastName:"fdfd"
      },
      failOnStatusCode: false
    }).then((response) => {
      
      expect(response.body).to.have.property('message', 'User registered successfully!');
    });
  });
});