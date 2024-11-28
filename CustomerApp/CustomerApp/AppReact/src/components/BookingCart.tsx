import React from "react";
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
} from "react-native";
import { useCart } from "../../CartContext";
import axios from "axios";
import { useUser } from "../../UserContext";
const BookingCart = () => {
  const { cart, clearCart } = useCart();
  const { user } = useUser();
  const confirmBooking = async () => {
    const { userId, email } = user;
    if (!userId || !email) {
      alert("User information is missing. Please log in again.");
      return;
    }
    const cartIds = cart.map((item) => item._id);
    console.log("userId:", userId);
    console.log("email:", email);
    console.log("cartIds:", cartIds);
    try {
      const response = await axios.post(
        "http://192.168.1.7:3000/api/cart/confirm",
        {
          userId,
          email,
          cart: cartIds,
        }
      );
      console.log(response.data.message);
      alert("Booking confirmed!");
      clearCart();
    } catch (error) {
      console.error("Error confirming booking:", error);
      alert("Failed to confirm booking.");
    }
  };
  return (
    <View style={styles.container}>
      <Text style={styles.cartHeader}>Your Shopping Cart</Text>
      <Text style={styles.cartCount}>Items in Cart: {cart.length}</Text>
      <FlatList
        data={cart}
        keyExtractor={(item, index) => `${item._id}-${index}`}
        renderItem={({ item }) => (
          <View style={styles.cartItem}>
            <Text style={styles.cartItemText}>
              {item.courseId.name} - {item.date}
            </Text>
          </View>
        )}
        ListEmptyComponent={
          <Text style={styles.emptyCart}>Your cart is empty.</Text>
        }
      />
      {cart.length > 0 && (
        <TouchableOpacity style={styles.confirmButton} onPress={confirmBooking}>
          <Text style={styles.confirmButtonText}>Confirm Booking</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};
export default BookingCart;
const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "#f4f4f8",
  },
  cartHeader: {
    fontSize: 26,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 15,
    textAlign: "center",
  },
  cartCount: {
    fontSize: 18,
    color: "#555",
    marginBottom: 20,
    textAlign: "center",
  },
  cartItem: {
    padding: 15,
    backgroundColor: "#fff",
    borderRadius: 8,
    marginVertical: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  cartItemText: {
    fontSize: 16,
    color: "#333",
  },
  emptyCart: {
    fontStyle: "italic",
    color: "#777",
    textAlign: "center",
    marginTop: 20,
  },
  confirmButton: {
    backgroundColor: "#28a745",
    paddingVertical: 15,
    paddingHorizontal: 20,
    borderRadius: 8,
    marginVertical: 15,
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 3,
    elevation: 5,
  },
  confirmButtonText: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "bold",
  },
});
