import React, { useState } from "react";
import userApi from "../api/axiosUser";

const Login = ({ onLoginSuccess }) => {
  const [mobile, setMobile] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await userApi.login({ mobileNumber: mobile, password });
      const token = res.data.token; // JWT returned from backend
      localStorage.setItem("token", token); // ✅ store token
      onLoginSuccess(); // notify parent component
    } catch (err) {
      setError("Invalid credentials or server error");
      console.error(err);
    }
  };

  return (
    <div>
      <h2>Login</h2>
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
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;
