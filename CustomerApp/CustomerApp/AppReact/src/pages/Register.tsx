import axios from "axios";
import React, { useState } from "react";
import {
  TextInput,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
export const Register = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigation = useNavigation();
  const handleRegister = async () => {
    try {
      await axios.post("http://192.168.1.7:3000/api/users/register", {
        username,
        email,
        password,
      });
      alert("Registration successful");
    } catch (error) {
      alert("Registration failed");
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.headerText}>Register</Text>
      <TextInput
        style={styles.input}
        placeholder="Username"
        onChangeText={setUsername}
        value={username}
      />
      <TextInput
        style={styles.input}
        placeholder="Email"
        onChangeText={setEmail}
        value={email}
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        secureTextEntry
        onChangeText={setPassword}
        value={password}
      />
      <TouchableOpacity style={styles.button} onPress={handleRegister}>
        <Text style={styles.buttonText}>Register</Text>
      </TouchableOpacity>
      <Text style={styles.footerText}>Already have an account?</Text>
      <TouchableOpacity onPress={() => navigation.navigate("Login")}>
        <Text style={{ color: "blue", marginTop: 10 }}>Login</Text>
      </TouchableOpacity>
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
  headerText: {
    fontSize: 28,
    fontWeight: "bold",
    marginBottom: 20,
    color: "#333",
  },
  input: {
    width: "100%",
    padding: 15,
    marginVertical: 10,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    backgroundColor: "#fff",
  },
  button: {
    backgroundColor: "#6200ee",
    paddingVertical: 15,
    paddingHorizontal: 80,
    borderRadius: 8,
    marginVertical: 10,
  },
  buttonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
  footerText: {
    fontSize: 16,
    color: "#666",
    marginTop: 20,
  },
  linkText: {
    fontSize: 16,
    color: "#6200ee",
    marginTop: 5,
    fontWeight: "600",
  },
});
