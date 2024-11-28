import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  FlatList,
  StyleSheet,
} from "react-native";
import axios from "axios";
import { ClassInstance } from "../../type";
import { useCart } from "../../CartContext";

const ClassInstanceList = () => {
  const [classInstances, setClassInstances] = useState<ClassInstance[]>([]);
  const [dayOfWeek, setDayOfWeek] = useState<string>("");
  const [time, setTime] = useState<string>("");
  const { addToCart } = useCart();

  useEffect(() => {
    fetchClassInstances();
  }, []);

  const fetchClassInstances = async () => {
    try {
      const response = await axios.get<ClassInstance[]>(
        "http://192.168.1.7:3000/api/"
      );
      setClassInstances(response.data);
    } catch (error) {
      console.error("Error fetching class instances:", error);
    }
  };

  const searchClassInstances = async () => {
    try {
      const query = `?${dayOfWeek ? `dayOfWeek=${dayOfWeek}&` : ""}${
        time ? `time=${time}` : ""
      }`;
      const response = await axios.get<ClassInstance[]>(
        `http://192.168.1.7:3000/api/search${query}`
      );
      setClassInstances(response.data);
    } catch (error) {
      console.error("Error searching class instances:", error);
    }
  };

  const renderClass = ({ item }) => (
    <View style={styles.classInstanceItem}>
      <Text style={styles.classInstanceTitle}>{item.courseId.name}</Text>
      <Text style={styles.classDetail}>Day: {item.courseId.dayOfWeek}</Text>
      <Text style={styles.classDetail}>Time: {item.courseId.time}</Text>
      <Text style={styles.classDetail}>Date: {item.date}</Text>
      <Text style={styles.classDetail}>Teacher: {item.teacher}</Text>
      <Text style={styles.classDetail}>Comments: {item.comments}</Text>
      <TouchableOpacity
        style={styles.addButton}
        onPress={() => addToCart(item)}
      >
        <Text style={styles.addButtonText}>Add to Cart</Text>
      </TouchableOpacity>
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Available Class Instances</Text>

      <TextInput
        style={styles.input}
        placeholder="Day of the Week (e.g., Monday)"
        value={dayOfWeek}
        onChangeText={setDayOfWeek}
      />
      <TextInput
        style={styles.input}
        placeholder="Time (e.g., 10:00 AM)"
        value={time}
        onChangeText={setTime}
      />
      <TouchableOpacity
        style={styles.searchButton}
        onPress={searchClassInstances}
      >
        <Text style={styles.searchButtonText}>Search Classes</Text>
      </TouchableOpacity>

      <FlatList
        data={classInstances}
        keyExtractor={(item, index) => `${item._id}-${index}`}
        renderItem={renderClass}
        ListEmptyComponent={
          <Text style={styles.emptyMessage}>No classes available</Text>
        }
      />
    </View>
  );
};

export default ClassInstanceList;

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
    marginBottom: 20,
    textAlign: "center",
  },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 12,
    marginBottom: 10,
    borderRadius: 8,
    backgroundColor: "#fff",
  },
  searchButton: {
    backgroundColor: "#4CAF50",
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderRadius: 8,
    alignItems: "center",
    marginBottom: 15,
  },
  searchButtonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
  classInstanceItem: {
    backgroundColor: "#fff",
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2,
  },
  classInstanceTitle: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 5,
  },
  classDetail: {
    fontSize: 16,
    color: "#555",
  },
  addButton: {
    backgroundColor: "#6200ee",
    paddingVertical: 10,
    borderRadius: 5,
    marginTop: 15,
    alignItems: "center",
  },
  addButtonText: {
    color: "#fff",
    fontWeight: "600",
    fontSize: 16,
  },
  emptyMessage: {
    textAlign: "center",
    fontSize: 16,
    color: "#777",
    marginTop: 20,
    fontStyle: "italic",
  },
});
