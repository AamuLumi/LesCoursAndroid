'use strict';

let mongoose     = require('mongoose');
let Schema       = mongoose.Schema;

let ShoppingListSchema   = new Schema(
{
    name : {type : String, required: true, unique : true},
    items : [{
      name : String,
      quantity : {type : Number, default : 0},
      price : {type : Number, default : 0}
    }]
});

module.exports = mongoose.model('ShoppingList', ShoppingListSchema);
