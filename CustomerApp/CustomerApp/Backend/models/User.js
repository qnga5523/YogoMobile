const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");
const UserSchema = new mongoose.Schema({
  email: { type: String, required: true, unique: true },
  username: {
    type: String,
  },
  password: { type: String, require: true },
});

module.exports = mongoose.model("User", UserSchema);
