const mongoose = require("mongoose");

const CourseSchema = new mongoose.Schema({
  id: {
    type: Number,
    required: true,
    unique: true,
  },
  name: { type: String, required: true },
  dayOfWeek: {
    type: String,
    required: true,
    enum: [
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
      "Sunday",
    ],
  },
  time: { type: String, required: true },
  capacity: { type: Number, required: true },
  duration: { type: Number, required: true },
  price: { type: Number, required: true },
  type: { type: String, required: true },
  description: { type: String },
  isSynced: {
    type: Boolean,
  },
});

module.exports = mongoose.model("Course", CourseSchema);
