var mysql = require('mysql');
var config = require('../config.json');

var pool = mysql.createPool({
    connectionLimit : 25,
    host        :   config.server,
    user        :   config.username,
    password    :   config.password,
    database    :   config.dbname
});

module.exports = pool;