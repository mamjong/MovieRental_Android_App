// API version 1
var express = require('express');
var router = express.Router();
var moment = require('moment');
var jwt = require('jwt-simple');
var bcrypt = require('bcrypt');

var bodyParser = require('body-parser');
router.use(bodyParser.json()); // support json encoded bodies
router.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

var path = require('path');
var pool = require('../db/db_connector');
var config = require('../config.json');

function encodeToken(username) {
    const payload = {
        exp: moment().add(10, 'days').unix(),
        iat: moment().unix(),
        sub: username
    };
    return jwt.encode(payload, config.secretkey);
}

function decodeToken(token, cb) {

    try {
        const payload = jwt.decode(token, config.secretkey);

        //check if token has expired
        const now = moment().unix();

        if (now > payload.exp) {
            console.log('Token has expired.')
        }

        //callback
        cb(null, payload);

    } catch (err) {
        //callback
        cb(err, null);
    }
}

router.post('/login', function(req, res) {

    var username = req.body.username || '';
    var password = req.body.password || '';

    if (username && password) {
        query_str = 'SELECT * FROM customer WHERE username = "' + username + '";';


        pool.getConnection(function (err, connection) {
            if (err) {
                throw err
            }
            connection.query(query_str, function (err, rows, fields) {
                connection.release();
                if (err) {
                    throw err
                }
                if (rows[0]) {
                    if (rows[0].hasOwnProperty('username') && rows[0].hasOwnProperty('password')) {
                        var hash = rows[0].password;
                        if (bcrypt.compareSync(password, hash)){
                            res.status(200).json({"token" : encodeToken(username), "id":rows[0].customer_id});
                        } else {
                            res.json({error:"Invalid password"});
                        }
                    } else {
                        res.json({error: "Please enter a valid username and password"});
                    }
                } else {
                    res.json({error: "Please enter a valid username and password"});
                }

            });
        });
    } else {
        res.json({error: "Please enter a valid username and password"});
    }

});

router.post('/register', function(req, res, next){
    var username = req.body.username;
    var password = req.body.password;

    var hash = bcrypt.hashSync(password, 10);

    var query_str = {

        sql: 'INSERT INTO `customer`(username, password) VALUES (?,?)',
        values: [username, hash],

        timeout: 2000
    };

    pool.getConnection(function (err, connection) {
        if (err) {
            throw err
        }
        connection.query(query_str, function (err, rows, fields) {
            connection.release();
            if (err) {
                console.log(err);
            }
            res.status(200).json(rows);
        });
    });
});

router.get('/rentals/:customerId', function(req, res) {
    var customerId = req.params.customerId;
    var query = 'SELECT * FROM `rental` inner join inventory on rental.inventory_id = inventory.inventory_id inner join film on film.film_id = inventory.film_id WHERE rental.customer_id =' + customerId +';'

    pool.getConnection(function (err, connection) {
        if (err) {throw err}
        connection.query(query, function (err, rows, fields) {
            connection.release();
            if (err) {throw err}
            res.status(200).json(rows)
        })
    })
});




router.get('/films', function (req, res) {
    var offset = req.query.offset || "";
    var count = req.query.count || "";

    console.log(offset + count);

    var query = 'SELECT * FROM film ORDER BY film_id LIMIT ' + count + ' OFFSET ' + offset + ';';

    pool.getConnection((function (err, connection) {
        if(err){
            throw err
        }connection.query(query, function (err, rows, fields) {
            connection.release();
            if(err){
                throw err
            }
            res.status(200).json(rows);
        });
    }));
});

router.get('/filmid/:filmid', function (req, res) {
    var filmid = req.params.filmid || "";




    var query = "SELECT * FROM `film` inner join inventory on film.film_id = inventory.film_id inner join rental on rental.inventory_id = inventory.inventory_id where film.film_id = " + filmid+ ";" ;

    pool.getConnection((function (err, connection) {
        if(err){
            throw err
        }connection.query(query, function (err, rows, fields) {
            connection.release();
            if(err){
                throw err
            }
            res.status(200).json(rows);
        });
    }));
});

router.all('*', function(req, res, next) {
    var token = (req.header('X-Access-Token')) || '';

    decodeToken(token, function (err, payload) {
        if (err) {
            console.log('Error handler: ' + err.message);
            res.status((err.status || 401 )).json({error: new Error("Not authorised").message});
        } else {
            next();
        }
    });
});

router.post('/rentals/:customerId/:inventoryId', function (req, res) {
    var customerId = req.params.customerId;
    var inventoryId = req.params.inventoryId;

    var rentalDate = req.body.RentalDate;
    var returnDate = req.body.ReturnDate;
    var staffId = req.body.StaffId;

    var query = {
        sql : 'INSERT INTO `rental`(rental_date, inventory_id, customer_id, return_date, staff_id) VALUES (?, ?, ?, ?, ?)',
        values : [rentalDate, inventoryId, customerId, returnDate, staffId],
        timeout : 2000
    };
    res.contentType("application/json");

    pool.getConnection(function (err, connection) {
        if (err) {throw err}
        connection.query(query, function (err, rows, fields) {
            connection.release();
            if (err) {throw err}
            res.status(200).json(rows);
        })
    })
});

router.put('/rentals/:customerId/:inventoryId', function (req, res) {
    var customerId = req.params.customerId;
    var inventoryId = req.params.inventoryId;

    var rentalDate = req.body.RentalDate;
    var returnDate = req.body.ReturnDate;
    var staffId = req.body.StaffId;

    var query = {
        sql: 'UPDATE `rental` SET rental_date = ?, return_date = ?, staff_id = ? ' +
        'WHERE customer_id = ' + customerId + ' AND inventory_id = ' + inventoryId + ';',
        values: [rentalDate, returnDate, staffId],
        timeout: 2000
    };
    res.contentType("application/json");

    pool.getConnection(function (err, connection) {
        if (err) {
            throw err
        }
        connection.query(query, function (err, rows, fields) {
            connection.release();
            if (err) {
                throw err
            }
            res.status(200).json(rows);
        })
    })
});

router.delete('/rental', function (req, res) {
    var customerid = req.query.customerId || "";
    var inventoryid = req.query.inventoryId || "";

    var query = "DELETE FROM rental WHERE customer_id = " + customerid + " AND inventory_id = " + inventoryid +" ;";

    pool.getConnection((function (err, connection) {
        if(err){
            throw err
        }connection.query(query, function (err, rows, fields) {
            connection.release();
            if(err){
                throw err
            }
            res.status(200).json(rows);
        });
    }));
});

module.exports = router;
