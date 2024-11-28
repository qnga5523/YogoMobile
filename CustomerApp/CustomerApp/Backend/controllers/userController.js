const User = require("../models/User");
const jwt = require("jsonwebtoken");
exports.register = async (req, res) => {
  const { username, email, password } = req.body;
  try {
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: "Email already in use" });
    }
    const user = new User({ username, email, password });
    await user.save();
    res.status(201).json({ message: "User registered successfully" });
  } catch (error) {
    console.error("Registration Error:", error);
    res.status(500).json({ error: error.message });
  }
};
exports.login = async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await User.findOne({ email });
    if (!user || user.password !== password) {
      return res.status(400).json({ message: "Invalid email or password" });
    }
    const token = jwt.sign({ userId: user._id }, "your_jwt_secret", {
      expiresIn: "24h",
    });
    res.json({ token, userId: user._id });
  } catch (error) {
    console.error("Login Error:", error);
    res.status(500).json({ error: error.message });
  }
};
