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
  ShoppingList.findById(req.params.id, (err, sl) => {
    if (err) {
      resReturn(res, REQ_ERROR, SUC_ERR, err);
    } else {
      resReturn(res, REQ_OK, SUC_OK, sl,
        'ShoppingList found');
    }
  });
});

app.post('/lists/:id', (req, res) => {
  ShoppingList.findById(req.params.id, (err, sl) => {
    if (err) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);

      return;
    }

    if ('item' in req.body) {
      if (!('name' in req.body.item)) {
        resReturn(res, REQ_BAD, SUC_INVPARAM, req.body.item,
          'Error : missing name parameter in item');

        return;
      }

      for (let i of sl.items) {
        if (i.name === req.body.item.name) {
          resReturn(res, REQ_ERROR, SUC_EXIST, req.body.item,
            'Error : item with this name already exists'
          );

          return;
        }
      }

      if (req.body.item.quantity === undefined ||
        req.body.item.quantity === 0) {
        req.body.item.quantity = 1;
      }

      sl.items.push(req.body.item);
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

app.put('/lists/:id', (req, res) => {
  ShoppingList.findById(req.params.id, (err, sl) => {
    if (err) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);

      return;
    }

    if ('item' in req.body) {
      for (let i of sl.items) {
        if (i.id === req.body.item.id || i.name ===
          req.body.item.name) {
          if ('name' in req.body.item) {
            i.name = req.body.item.name;
          }
          if ('price' in req.body.item) {
            i.price = req.body.item.price;
          }
          if ('quantity' in req.body.item) {
            i.quantity = req.body.item.quantity;
          }
        }
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

app.delete('/lists/:id', (req, res) => {
  ShoppingList.findById(req.params.id, (err, sl) => {
    if (err) {
      resReturn(res, REQ_NOT_FOUND, SUC_NOTFOUND, err);
      return;
    }

    let itemFound = false;

    if ('item' in req.body) {
      for (let i = 0; i < sl.items.length; i++) {
        if (sl.items[i].id === req.body.item.id) {
          sl.items.splice(i, 1);
          itemFound = true;

          break;
        } else if (sl.items[i].name === req.body.item.name) {
          sl.items.splice(i, 1);
          itemFound = true;

          break;
        }
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