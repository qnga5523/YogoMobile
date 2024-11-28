import React from "react";
import { View, TouchableOpacity, Text, StyleSheet } from "react-native";
import { useNavigation } from "@react-navigation/native";
export const Home = () => {
  const navigation = useNavigation();
  const CustomButton = ({ title, onPress }) => (
    <TouchableOpacity style={styles.button} onPress={onPress}>
      <Text style={styles.buttonText}>{title}</Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Welcome to Yoga Booking App</Text>
      <CustomButton
        title="View Class Instances"
        onPress={() => navigation.navigate("ClassInstanceList")}
      />
      <CustomButton
        title="View List Course"
        onPress={() => navigation.navigate("CourseList")}
      />
      <CustomButton
        title="View Cart"
        onPress={() => navigation.navigate("BookingCart")}
      />
      <CustomButton
        title="View History"
        onPress={() => navigation.navigate("BookingHistory")}
      />
    </View>
  );
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
    backgroundColor: "#f5f5f5",
  },
  header: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 30,
    textAlign: "center",
  },
  button: {
    backgroundColor: "#6200ee",
    paddingVertical: 15,
    paddingHorizontal: 20,
    borderRadius: 8,
    marginVertical: 10,
    width: "80%",
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 3,
    elevation: 5,
  },
  buttonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
});

export default Home;
