import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { CartProvider } from "./CartContext";
import { UserProvider } from "./UserContext"; // Import UserProvider mới tạo
import { Register } from "./src/pages/Register";
import { Login } from "./src/pages/Login";
import CourseList from "./src/components/CourseItem";
import ClassInstanceList from "./src/components/ClassInstance";
import Home from "./src/pages/Home";
import BookingCart from "./src/components/BookingCart";
import BookingHistory from "./src/components/BookingHistory";

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <UserProvider>
      <CartProvider>
        <NavigationContainer>
          <Stack.Navigator initialRouteName="Yoga Mobile">
            <Stack.Screen name="Register" component={Register} />
            <Stack.Screen name="Login" component={Login} />
            <Stack.Screen name="Home" component={Home} />
            <Stack.Screen
              name="CourseList"
              component={CourseList}
              options={{ title: "Courses" }}
            />
            <Stack.Screen
              name="ClassInstanceList"
              component={ClassInstanceList}
              options={{ title: "Class Instances" }}
            />
            <Stack.Screen name="BookingCart" component={BookingCart} />
            <Stack.Screen name="BookingHistory" component={BookingHistory} />
          </Stack.Navigator>
        </NavigationContainer>
      </CartProvider>
    </UserProvider>
  );
}
