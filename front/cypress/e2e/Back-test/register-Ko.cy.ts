    
describe('Register user as admin with preset token', () => {

  it('should register a new user with admin token and get success message', () => {
    
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/auth/register',
     
      body: {
        email: "marius29@123.fr",
        password: "test123",
        firstName:"marius28",
        lastName:"fdfd"
      },
      failOnStatusCode: false
    }).then((response) => {
      
      expect(response.body).to.have.property('message', 'Error: Email is already taken!');
    });
  });
});
