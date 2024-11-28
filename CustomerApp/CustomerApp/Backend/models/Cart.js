const mongoose = require("mongoose");

const CartSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true,
  },
  classes: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: "ClassInstance",
      required: true,
    },
  ],
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model("Cart", CartSchema);
