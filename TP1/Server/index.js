'use strict';

// Constants
const REQ_OK = 200;
const REQ_BAD = 400;
const REQ_NOT_FOUND = 404;
const REQ_ERROR = 500;

const SERVER_PORT = 9980;

const SUC_OK = 1;
const SUC_ERR = 0;
const SUC_INVPARAM = -1;
const SUC_NOTFOUND = -2;
const SUC_EXIST = -3;

// Variables

let ShoppingList = require('./shoppingList.js');

let express = require('express');
let app = express();

let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/shopping-api');

let isMongooseId = require('mongoose').Types.ObjectId.isValid;

let bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

function resReturn(res, statusCode, success, data, message) {
  res.status(statusCode).send({
    success: success,
    data: data,
    message: message
  });
}

// Routes

app.get('/lists', (req, res) => {
  ShoppingList.find((err, result) => {
    if (err) {
      resReturn(res, REQ_ERROR, SUC_ERR, err);
    } else {
      resReturn(res, REQ_OK, SUC_OK, result);
    }
  });
});

app.get('/lists/:id', (req, res) => {
  if (isMongooseId(req.params.id)) {
    ShoppingList.findById(req.params.id, (err, sl) => {
      if (err) {
        resReturn(res, REQ_ERROR, SUC_ERR, err);
      } else {
        resReturn(res, REQ_OK, SUC_OK, sl,
          'ShoppingList found');
      }
    });
  } else {
    ShoppingList.findOne({
      name: req.params.id
    }, (err, sl) => {
      if (err) {
        resReturn(res, REQ_ERROR, SUC_ERR, err);
      } else if (sl !== undefined && sl !== null) {
        resReturn(res, REQ_OK, SUC_OK, sl,
          'ShoppingList found');
      } else {
        resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, sl,
          'ShoppingList not found');
      }
    });
  }
});

app.post('/lists/:name', (req, res) => {
  ShoppingList.findOne({
    name: req.params.name
  }, (err, sl) => {
    if (err || sl === null) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);

      return;
    }

    if (!('name' in req.body)) {
      resReturn(res, REQ_BAD, SUC_INVPARAM, req.body,
        'Error : missing name parameter in item');

      return;
    }

    for (let i of sl.items) {
      if (i.name === req.body.name) {
        resReturn(res, REQ_ERROR, SUC_EXIST, req.body,
          'Error : item with this name already exists'
        );

        return;
      }
    }

    if (req.body.quantity === undefined ||
      req.body.quantity === 0) {
      req.body.quantity = 1;
    }

    if (req.body.price === undefined) {
      req.body.price = 0;
    }

    let item = {
      name: req.body.name,
      quantity: req.body.quantity,
      price: req.body.price
    };

    sl.items.push(item);

    sl.save((err) => {
      if (err) {
        resReturn(res, REQ_ERROR, SUC_ERR, err);
      } else {
        resReturn(res, REQ_OK, SUC_OK, sl,
          'ShoppingList updated');
      }
    });
  });
});

app.put('/lists/:name', (req, res) => {
  ShoppingList.findOne({
    name: req.params.name
  }, (err, sl) => {
    if (err || sl === null) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);

      return;
    }

    for (let i of sl.items) {
      if (i.name === req.body.name) {
        if ('price' in req.body) {
          i.price = req.body.price;
        }
        if ('quantity' in req.body) {
          i.quantity = req.body.quantity;
        }

        break;
      }
    }

    sl.save((err) => {
      if (err) {
        resReturn(res, REQ_ERROR, SUC_ERR, err);
      } else {
        resReturn(res, REQ_OK, SUC_OK, sl,
          'ShoppingList updated');
      }
    });
  });
});

app.delete('/lists/:name', (req, res) => {
  ShoppingList.findOne({
    name: req.params.name
  }, (err, sl) => {
    if (err || sl === null) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);
      return;
    }

    let itemFound = false;

    for (let i = 0; i < sl.items.length; i++) {
      if (sl.items[i].name === req.body.name) {
        sl.items.splice(i, 1);
        itemFound = true;

        break;
      }
    }

    if (!itemFound) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, req.body
        .item,
        'Error : item with this id not found'
      );
      return;
    }

    sl.save((err) => {
      if (err) {
        resReturn(res, REQ_ERROR, SUC_ERR, err);
      } else {
        resReturn(res, REQ_OK, SUC_OK, sl,
          'ShoppingList updated');
      }
    });
  });
});

app.post('/lists', (req, res) => {
  let sl = new ShoppingList();


  if ('name' in req.body) {
    sl.name = req.body.name;
  } else {
    resReturn(res, REQ_BAD, SUC_INVPARAM, req.body,
      'Error : missing name parameter in body');

    return;
  }

  sl.save((err) => {
    if (err) {
      resReturn(res, REQ_ERROR, SUC_ERR, err);
    } else {
      resReturn(res, REQ_OK, SUC_OK, sl,
        'ShoppingList added');
    }
  });
});

app.listen(SERVER_PORT);
