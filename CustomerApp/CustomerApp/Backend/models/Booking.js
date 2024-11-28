const mongoose = require("mongoose");
const BookingSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: "User",
  },
  email: {
    type: String,
    required: true,
  },
  classes: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: "ClassInstance",
    },
  ],
  bookingDate: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model("Booking", BookingSchema);
