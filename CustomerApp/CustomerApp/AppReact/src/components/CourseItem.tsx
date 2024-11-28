import React, { useState, useEffect } from "react";
import { View, Text, FlatList, StyleSheet } from "react-native";
import axios from "axios";
import { Course } from "../../type";

const CourseList = () => {
  const [courses, setCourses] = useState<Course[]>([]);

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await axios.get<Course[]>(
        "http://192.168.1.7:3000/api/courses"
      );
      setCourses(response.data);
    } catch (error) {
      console.error("Error fetching courses:", error);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.header}>List Courses</Text>

      <FlatList
        data={courses}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <View style={styles.courseItem}>
            <Text style={styles.courseTitle}>{item.name}</Text>
            <Text style={styles.courseDetail}>Day: {item.dayOfWeek}</Text>
            <Text style={styles.courseDetail}>Time: {item.time}</Text>
            <Text style={styles.courseDetail}>Type: {item.type}</Text>
            <Text style={styles.courseDetail}>Price: ${item.price}</Text>
            <Text style={styles.courseDescription}>{item.description}</Text>
          </View>
        )}
      />
    </View>
  );
};

export default CourseList;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "#f5f5f5",
  },
  header: {
    fontSize: 26,
    fontWeight: "bold",
    color: "#333",
    textAlign: "center",
    marginBottom: 20,
  },
  courseItem: {
    backgroundColor: "#fff",
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  courseTitle: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 5,
  },
  courseDetail: {
    fontSize: 16,
    color: "#555",
    marginBottom: 2,
  },
  courseDescription: {
    fontSize: 15,
    color: "#666",
    fontStyle: "italic",
    marginTop: 8,
  },
});
