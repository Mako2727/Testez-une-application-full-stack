describe('API - Create Session', () => {
  let token;

  before(() => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/auth/login',
      body: {
        email: 'yoga@studio.com', // à adapter à ton user
        password: 'test!1234'      // idem
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      token = response.body.token;
    });
  });

  it('should create a session', () => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:9090/api/session',
      headers: {
        Authorization: `Bearer ${token}`
      },
      body: {
        name: "test",
        date: "2025-08-10",
        description: "description",
        teacher_id: 2
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('id');
      expect(response.body.name).to.eq("test");
      expect(response.body.date).to.include("2025-08-10");
      expect(response.body.teacher_id).to.eq(2);
      expect(response.body.description).to.eq("description");
      expect(response.body.users).to.be.an('array').that.is.empty;
      expect(response.body).to.have.property('createdAt');
      expect(response.body).to.have.property('updatedAt');
    });
  });
});