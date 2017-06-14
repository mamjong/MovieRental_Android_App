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



