'use strict';

// Constants
const REQ_OK = 200;
const REQ_ERROR = 500;

const SERVER_PORT = 9980;

// Variables

let User = require('./user.js');

let express = require('express');
let app = express();

let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/newsletter-api');

let bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

// Routes

app.get('/users', (req, res) => {
  User.find((err, result) => {
    if (err) {
      res.status(REQ_ERROR).send({
        success: 0,
        data: err
      });
    } else {
      res.status(REQ_OK).send({
        success: 1,
        data: result
      });
    }
  });
});

app.post('/users', (req, res) => {
  let user = new User();

  if ('firstName' in req.body) {
    user.firstName = req.body.firstName;
  }
  if ('lastName' in req.body) {
    user.lastName = req.body.lastName;
  }
  if ('email' in req.body) {
    user.email = req.body.email;
  }

  user.save((err) => {
    if (err) {
      res.status(REQ_ERROR).send({
        success: 0,
        data: err
      });
    } else {
      res.status(REQ_OK).send({
        success: 1,
        data: null,
        message : 'User added'
      });
    }
  });
});

app.listen(SERVER_PORT);
