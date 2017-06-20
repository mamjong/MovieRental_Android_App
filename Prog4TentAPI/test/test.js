
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../server.js');
var should = chai.should();
var moment = require('moment');

chai.use(chaiHttp);

var token = "";
var now = moment().format("YYYY-MM-DD HH:MM:SS");

describe('film request', function () {
    it('Test GET /api/v1/films', function (done) {
        chai.request(server)
            .get('/api/v1/films?offset=20&count=10')
            .end(function (err, res) {
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

describe('rental request', function () {
    it('Test GET /api/v1/rentals/:customerId', function (done) {
        chai.request(server)
            .get('/api/v1/rentals/2')
            .end(function (err, res) {
                res.should.have.status(200);
                res.body.should.be.a('array');
                done();
            });
    });
});

describe('rental POST', function () {
    var token = "";
    before(function (done) {
        chai.request(server)
            .post('/api/v1/login')
            .send({"username": "test", "password": "test"})
            .end(function (err, res) {
                var result = JSON.parse(res.text);
                token = result.token;
                console.log(token);
                done();
            });
    });
    it('Test POST /api/v1/rentals/:customerId/:inventoryId', function (done) {
        chai.request(server)
            .post('/api/v1/rentals/3/10')
            .set("X-Access-Token", token)
            .send({"RentalDate": now, "ReturnDate": "2019-08-19 22:53:30", "StaffId": "15"})
            .end(function (err, res) {
                res.should.have.status(200);
                res.body.should.be.a('object');
                done();
            });
    });
});

describe('rental PUT', function () {
    before(function (done) {
        chai.request(server)
            .post('/api/v1/login')
            .send({"username": "test", "password": "test"})
            .end(function (err, res) {
                var result = JSON.parse(res.text);
                token = result.token;
                console.log(token);
                done();
            });
    });
    it('Test PUT /api/v1/rentals/:customerId/:inventoryId', function (done) {
        chai.request(server)
            .put('/api/v1/rentals/3/20')
            .set("X-Access-Token", token)
            .send({ "RentalDate" : now, "ReturnDate" : "2020-06-24 22:59:23", "StaffId" : "20" })
            .end(function (err, res) {
                res.should.have.status(200);
                res.body.should.be.a('object');
                done();
            });
    });
});

describe('delete rental', function() {
    before(function (done) {
        chai.request(server)
            .post("/api/v1/login")
            .send({"username": "test", "password" : "test"})
            .end(function (err, res) {
                var result = JSON.parse(res.text);
                token = result.token;
                done();
            });
    });
    it('test DELETE/api/v1/rental', function(done) {
        chai.request(server)
            .delete('/api/v1/rental?customerId=3&inventoryId=10')
            .set("X-Access-Token", token)
            .end(function(err, res) {
                res.should.have.status(200);
                res.body.should.be.a('object');
                res.body.should.be.a('object');
                res.body.should.have.property('affectedRows');
                res.body.should.have.property('affectedRows', 1);
                done();
            });
    });
});



