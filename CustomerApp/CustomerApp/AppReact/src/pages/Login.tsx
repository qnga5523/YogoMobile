import React, { useState } from "react";
import {
  TextInput,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { useUser } from "../../UserContext";

export const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigation = useNavigation();
  const { setUser } = useUser();

  const handleLogin = async () => {
    try {
      const response = await axios.post(
        "http://192.168.1.7:3000/api/users/login",
        {
          email,
          password,
        }
      );
      if (response.status === 200) {
        const { token, userId } = response.data;
        await AsyncStorage.setItem("userToken", token);
        setUser({ userId, email });
        Alert.alert("Login Successful", "Welcome back!");
        navigation.navigate("Home");
      } else {
        Alert.alert("Login Failed", "Incorrect email or password.");
      }
    } catch (error) {
      console.error("Login Error:", error);
      Alert.alert("Login Failed");
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.headerText}>Login</Text>
      <TextInput
        style={styles.input}
        placeholder="Email"
        onChangeText={setEmail}
        value={email}
        keyboardType="email-address"
        autoCapitalize="none"
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        secureTextEntry
        onChangeText={setPassword}
        value={password}
      />
      <TouchableOpacity style={styles.button} onPress={handleLogin}>
        <Text style={styles.buttonText}>Login</Text>
      </TouchableOpacity>
      <Text style={styles.footerText}>Create an Account?</Text>
      <TouchableOpacity onPress={() => navigation.navigate("Register")}>
        <Text style={styles.linkText}>Register</Text>
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
    marginTop: 10,
    fontWeight: "600",
  },
});
