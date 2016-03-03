'use strict';

let mongoose     = require('mongoose');
let Schema       = mongoose.Schema;

let UserSchema   = new Schema(
{
    lastName : String,
    firstName : String,
    email : {type : String, required : true, unique : true}
});

module.exports = mongoose.model('User', UserSchema);
