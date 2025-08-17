import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

// Page and Component Imports
import Home from './pages/Home';
import Login from './components/Login';
import Register from './components/Register';
import CheckBalance from './components/CheckBalance';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login onLoginSuccess={() => alert('✅ Logged In')} />} />
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
      </Routes>
    </Router>
  );
}

export default App;
