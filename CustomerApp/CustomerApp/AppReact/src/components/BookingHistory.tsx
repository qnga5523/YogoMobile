import React, { useEffect, useState } from "react";
import { View, Text, FlatList, StyleSheet } from "react-native";
import axios from "axios";
import { useUser } from "../../UserContext";

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const { user } = useUser();

  useEffect(() => {
    const fetchBookings = async () => {
      if (!user.userId) {
        console.error("User ID is missing.");
        return;
      }

      try {
        const response = await axios.get(
          `http://192.168.1.7:3000/api/bookings/${user.userId}`
        );
        setBookings(response.data);
      } catch (error) {
        console.error("Error fetching bookings:", error);
      }
    };

    fetchBookings();
  }, [user.userId]);

  const renderBooking = ({ item }) => (
    <View style={styles.bookingItem}>
      <Text style={styles.bookingDate}>
        Booking Date: {new Date(item.bookingDate).toLocaleDateString()}
      </Text>
      <Text style={styles.classesHeader}>Classes:</Text>
      <FlatList
        data={item.classes}
        keyExtractor={(classItem) => classItem._id}
        renderItem={({ item: classItem }) => (
          <View style={styles.classItem}>
            <Text style={styles.classDetail}>
              Name: {classItem.courseId?.name}
            </Text>
            <Text style={styles.classDetail}>
              Day: {classItem.courseId?.dayOfWeek}
            </Text>
            <Text style={styles.classDetail}>
              Time: {classItem.courseId?.time}
            </Text>
            <Text style={styles.classDetail}>Teacher: {classItem.teacher}</Text>
          </View>
        )}
      />
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Your Booking History</Text>
      <FlatList
        data={bookings}
        keyExtractor={(item) => item._id}
        renderItem={renderBooking}
      />
    </View>
  );
};

export default BookingHistory;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "#f4f4f8",
  },
  header: {
    fontSize: 26,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 20,
    textAlign: "center",
  },
  bookingItem: {
    backgroundColor: "#fff",
    padding: 15,
    marginBottom: 15,
    borderRadius: 10,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  bookingDate: {
    fontSize: 18,
    fontWeight: "600",
    color: "#333",
    marginBottom: 10,
  },
  classesHeader: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#555",
    marginBottom: 5,
  },
  classItem: {
    backgroundColor: "#f8f8f8",
    padding: 10,
    borderRadius: 8,
    marginVertical: 5,
  },
  classDetail: {
    fontSize: 15,
    color: "#444",
  },
});
