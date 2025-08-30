import React, { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import "./Home.css";

const Home = () => {
  const { isAuthenticated, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="home-container">
      <h1>🏦 Welcome to UPI App</h1>
      {!isAuthenticated ? (
        <div className="auth-buttons">
          <Link to="/login">
            <button>Login</button>
          </Link>
          <Link to="/register">
            <button>Register</button>
          </Link>
        </div>
      ) : (
        <div className="menu-buttons">
          <Link to="/transfer">
            <button>💸 Transfer Money</button>
          </Link>
          <Link to="/checkBalance">
            <button>💰 Check Balance</button>
          </Link>
          <Link to="/chats">
            <button>💬 Chat</button>   {/* ✅ Chat option */}
          </Link>
          <button onClick={handleLogout}>🚪 Logout</button>
        </div>
      )}
    </div>
  );
};

export default Home;
