import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

import Home from "./pages/Home";
import Login from "./components/Login";
import Register from "./components/Register";
import CheckBalance from "./components/CheckBalance";
import IdentifyReceiver from "./pages/IdentifyReceiver";
import ConfirmTransfer from "./pages/ConfirmTransfer";

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected Routes */}
          <Route
            path="/checkBalance"
            element={
              <ProtectedRoute>
                <CheckBalance />
              </ProtectedRoute>
            }
          />
          <Route
            path="/transfer"
            element={
              <ProtectedRoute>
                <IdentifyReceiver />
              </ProtectedRoute>
            }
          />
          <Route
            path="/confirm-transfer"
            element={
              <ProtectedRoute>
                <ConfirmTransfer />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
