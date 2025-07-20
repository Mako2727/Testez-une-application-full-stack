describe('API - Admin Login', () => {
  it('should login admin and verify admin=true in response', () => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/auth/login',
      body: {
        email: 'yoga@studio.com',  // à adapter
        password: 'test!1234'        // à adapter
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      
      // Vérifie que le token est présent
      expect(response.body).to.have.property('token').and.to.be.a('string');
      
      // Vérifie que admin=true dans la réponse (adapté à ta structure JSON)
      expect(response.body).to.have.property('admin', true);
    });
  });
});