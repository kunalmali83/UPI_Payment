import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import userApi from "../api/axiosUser";
import { AuthContext } from "../context/AuthContext";
import "./Login.css";

const Login = () => {
  const [mobile, setMobile] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await userApi.login({ mobileNumber: mobile, password });
      const token = res.data.token || res.data.jwt || res.data.accessToken;
      const userData = res.data.user || {};

      if (!token) {
        setError("No token received from server");
        return;
      }

      login(token, userData);
      navigate("/home");
    } catch (err) {
      setError("Invalid credentials or server error");
      console.error("Login error:", err.response || err);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <h2 className="auth-title">Login</h2>
        {error && <p style={{ color: "red" }}>{error}</p>}
        <form onSubmit={handleLogin}>
          <input
            type="text"
            placeholder="Mobile Number"
            value={mobile}
            onChange={(e) => setMobile(e.target.value)}
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

        {/* Sign Up Link */}
        <div className="auth-footer">
          <p>
            Don't have an account?{" "}
            <span
              className="signup-link"
              onClick={() => navigate("/register")}
            >
              Sign Up
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
