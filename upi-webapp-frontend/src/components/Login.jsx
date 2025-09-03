import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import { AuthContext } from "../context/AuthContext";
import userApi from "../api/axiosUser";
import "react-toastify/dist/ReactToastify.css";
import "./Login.css";

const Login = () => {
  const [accountNumber, setAccountNumber] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await userApi.login({ accountNumber, password });
      if (res.data?.token) {
        login(res.data.token, { accountNumber });
        toast.success("Login successful!");
        navigate("/home"); // Go to dashboard
      } else {
        toast.error("Invalid credentials");
      }
    } catch (err) {
      console.error(err);
      toast.error("Login failed. Please try again.");
    }
  };

  return (
    <div className="auth-wrapper">
      <ToastContainer position="top-center" autoClose={2000} />
      <div className="auth-card">
        <h2 className="auth-title">Welcome Back!</h2>
        <p className="auth-subtitle">Login to your UPI account</p>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Account Number"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" className="btn-primary">Login</button>
        </form>
        <p className="auth-footer">
          Don’t have an account? <span className="link" onClick={() => navigate("/register")}>Sign Up</span>
        </p>
      </div>
    </div>
  );
};

export default Login;
