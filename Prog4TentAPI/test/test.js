
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../server.js');
var should = chai.should();
var moment = require('moment');

chai.use(chaiHttp);

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

var now = moment().format("YYYY-MM-DD HH:MM:SS");

describe('rental POST', function () {
    var token = "";
    before(function () {
        chai.request(server)
            .post('/api/v1/login')
            .send({"username": "Mark", "password": "12345"})
            .end(function (err, res) {
                var result = JSON.parse(res.text);
                token = result.token;
                console.log(token);
                done();
            });
    });
    it('Test POST /api/v1/rentals/:customerId/:inventoryId', function (done) {
        chai.request(server)
            .post('/api/v1/rentals/2/10')
            .set("X-Access-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTgzMTU0MDIsImlhdCI6MTQ5NzQ1MTQwMiwic3ViIjoiTWFyayJ9.CoFN01eiR65XDwE9yoI-2FcwJdXkBqDEetBxHs_rcc4")
            .send({"RentalDate": now, "ReturnDate": "2005-06-25 22:53:30", "StaffId": "5"})
            .end(function (err, res) {
                res.should.have.status(200);
                res.body.should.be.a('object');
                done();
            });
    });
});

describe('rental PUT', function () {
    it('Test PUT /api/v1/rentals/:customerId/:inventoryId', function (done) {
        chai.request(server)
            .get('/api/v1/rentals/2/3')
            .end(function (err, res) {
                res.should.have.status(200);
                res.body.should.be.a('array');
                done();
            });
    });
});
