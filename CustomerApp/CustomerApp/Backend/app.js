const mongoose = require("mongoose");
const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const yogaRoutes = require("./routes/yogaRoutes");
const userRoutes = require("./routes/userRoutes");
require("dotenv").config();
const cors = require("cors");

app.use(cors());
app.use(bodyParser.json());
app.use(express.json());
app.use("/api", yogaRoutes);
app.use("/api/users", userRoutes);

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send("Something went wrong!");
});
mongoose
  .connect(process.env.MONGODB_URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => console.log("Connected to MongoDB"))
  .catch((err) => {
    console.error("MongoDB connection error:", err);
    process.exit(1);
  });

const PORT = process.env.PORT || 3000;
const HOST = "0.0.0.0";
app.listen(PORT, () => {
  console.log(`Server running at http://${HOST}:${PORT}`);
});
