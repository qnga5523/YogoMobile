const express = require("express");
const Course = require("../models/Course");
const ClassInstance = require("../models/ClassInstance");
const Cart = require("../models/Cart");
const Booking = require("../models/Booking");
const router = express.Router();
const mongoose = require("mongoose");
router.post("/uploadCoursesWithInstances", async (req, res) => {
  const { courses, classInstances } = req.body;
  try {
    console.log("Courses received:", courses);
    console.log("Class Instances received:", classInstances);
    const courseInsertions = await Course.insertMany(courses);
    const courseIdMap = courseInsertions.reduce((acc, course) => {
      acc[course.id] = course._id;
      return acc;
    }, {});
    console.log("Course ID Map:", courseIdMap);
    const updatedInstances = classInstances
      .map((instance) => {
        const mappedCourseId = courseIdMap[instance.courseId];
        if (!mappedCourseId) {
          console.error(`No mapping found for courseId ${instance.courseId}`);
          return null;
        }
        return { ...instance, courseId: mappedCourseId };
      })
      .filter((instance) => instance !== null);
    await ClassInstance.insertMany(updatedInstances);
    res
      .status(200)
      .json({ message: "Courses and Class Instances uploaded successfully!" });
  } catch (error) {
    console.error("Error inserting data:", error);
    res.status(500).json({ error: "Failed to upload data" });
  }
});
router.get("/", async (req, res) => {
  try {
    const classInstances = await ClassInstance.find().populate("courseId");
    res.json(classInstances);
  } catch (error) {
    console.error("Error fetching class instances:", error);
    res.status(500).json({ error: "Failed to fetch class instances" });
  }
});
router.get("/search", async (req, res) => {
  const { dayOfWeek, time } = req.query;
  try {
    const courses = await Course.find({
      ...(dayOfWeek && { dayOfWeek }),
      ...(time && { time }),
    });
    const courseIds = courses.map((course) => course._id);
    const classInstances = await ClassInstance.find({
      courseId: { $in: courseIds },
    }).populate("courseId");
    res.json(classInstances);
  } catch (error) {
    console.error("Error searching class instances:", error);
    res.status(500).json({ error: "Failed to search class instances" });
  }
});
router.get("/courses", async (req, res) => {
  try {
    const courses = await Course.find();
    res.json(courses);
  } catch (error) {
    console.error("Error fetching courses:", error);
    res.status(500).json({ error: "Server error" });
  }
});
router.post("/add", async (req, res) => {
  const { userId, classId } = req.body;
  try {
    const userObjectId = mongoose.Types.ObjectId(userId);
    let cart = await Cart.findOne({ userId: userObjectId });
    if (!cart) {
      cart = new Cart({ userId: userObjectId, classes: [classId] });
    } else {
      if (!cart.classes.includes(classId)) {
        cart.classes.push(classId);
      }
    }
    await cart.save();
    res.status(200).json({ message: "Class added to cart", cart });
  } catch (error) {
    console.error("Error adding to cart:", error);
    res.status(500).json({ error: "Failed to add class to cart" });
  }
});
router.get("/cart/:userId", async (req, res) => {
  const { userId } = req.params;
  try {
    const userObjectId = mongoose.Types.ObjectId(userId);
    const cart = await Cart.findOne({ userId: userObjectId }).populate(
      "classes"
    );
    if (!cart) {
      return res.status(404).json({ message: "Cart not found" });
    }
    res.status(200).json(cart);
  } catch (error) {
    console.error("Error fetching cart:", error);
    res.status(500).json({ error: "Failed to fetch cart" });
  }
});
router.post("/cart/confirm", async (req, res) => {
  const { userId, email, cart } = req.body;
  if (
    !mongoose.Types.ObjectId.isValid(userId) ||
    !email ||
    !cart ||
    cart.some((id) => !mongoose.Types.ObjectId.isValid(id))
  ) {
    return res
      .status(400)
      .json({ error: "Invalid userId or cart item ID format." });
  }
  try {
    const userObjectId = new mongoose.Types.ObjectId(userId);
    const booking = new Booking({
      userId: userObjectId,
      email,
      classes: cart.map((classId) => new mongoose.Types.ObjectId(classId)),
      bookingDate: new Date(),
    });
    await booking.save();
    await Cart.deleteOne({ userId: userObjectId });
    res.status(201).json({ message: "Booking confirmed", booking });
  } catch (error) {
    console.error("Error confirming booking:", error);
    res.status(500).json({ error: "Failed to confirm booking" });
  }
});
router.get("/bookings/:userId", async (req, res) => {
  const { userId } = req.params;
  try {
    if (!mongoose.Types.ObjectId.isValid(userId)) {
      return res.status(400).json({ error: "Invalid userId format." });
    }
    const bookings = await Booking.find({
      userId: new mongoose.Types.ObjectId(userId),
    }).populate({
      path: "classes",
      populate: {
        path: "courseId",
        select: "name dayOfWeek time",
      },
    });
    if (!bookings) {
      return res.status(404).json({ error: "No bookings found." });
    }
    res.status(200).json(bookings);
  } catch (error) {
    console.error("Error fetching bookings:", error);
    res.status(500).json({ error: "Failed to fetch bookings" });
  }
});

module.exports = router;
