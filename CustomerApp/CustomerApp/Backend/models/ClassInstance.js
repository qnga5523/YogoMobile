const mongoose = require("mongoose");
const ClassInstanceSchema = new mongoose.Schema({
  instanceId: {
    type: Number,
    required: true,
    unique: true,
  },
  courseId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Course",
    required: true,
  },
  date: {
    type: String,
    required: true,
  },
  teacher: {
    type: String,
    required: true,
    trim: true,
  },
  comments: {
    type: String,
    default: "",
  },
  isSynced: {
    type: Boolean,
  },
});

module.exports = mongoose.model("ClassInstance", ClassInstanceSchema);
