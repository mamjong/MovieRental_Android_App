/**
 * Created by Mika Krooswijk on 14-6-2017.
 */

var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../server.js');
var should = chai.should();

chai.use(chaiHttp);

describe('film request', function() {
    it('Test GET /api/v1/films', function(done) {
        chai.request(server)
            .get('/api/v1/films?offset=20&count=10')
            .end(function(err, res) {
                res.should.have.status(200);
                res.body.should.be.a('array');
                done();
            });
    });
});

var token = "";

describe('film request', function() {
    it('Test GET /api/v1/films', function(done) {
        chai.request(server)
            .get('/api/v1/filmid/1')
            .end(function(err, res) {
                res.should.have.status(200);
                res.body.should.be.a('array');
                done();
            });
    });
});

describe('delete rental', function() {

    before(function (done) {
        chai.request(server)
            .post("/api/v1/login")
            .send({"username":"username", "password" : "password"})
            .end(function (err, res) {
                var result = JSON.parse(res.text);
                token = result.token;
                done();
            });
    });


    it('test DELETE/api/v1/rental', function(done) {
        chai.request(server)
            .delete('/api/v1/rental?customerid=408&inventoryid=1711')
            .set("X-Access-Token", token)
            .end(function(err, res) {
                res.should.have.status(200);
                res.body.should.be.a('object');
                done();
            });
    });
});



