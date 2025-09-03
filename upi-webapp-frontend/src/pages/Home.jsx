import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import userApi from "../api/axiosUser"; // API call to get profile
import "./Home.css";

const Home = () => {
  const navigate = useNavigate();
  const [profile, setProfile] = useState({
    name: "",
    email: "",
    accounts: [],
  });

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await userApi.getProfile();
        setProfile(res.data);
      } catch (err) {
        console.error(err);
        toast.error("Failed to load profile");
      }
    };
    fetchProfile();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    toast.success("Logged out successfully!");
    navigate("/login");
  };

  return (
    <div className="home-wrapper">
      <ToastContainer position="top-center" autoClose={2000} />

      {/* Header */}
      <header className="home-header">
        <h2>Welcome 👋</h2>
        <button className="logout-btn" onClick={handleLogout}>Logout</button>
      </header>

      {/* Profile Section */}
      <div className="profile-section">
        <div className="profile-avatar">👤</div>
        <div className="profile-info">
          <h2>{profile.name || "User"}</h2>
          <p>{profile.email || "email@example.com"}</p>

          {/* Show all accounts */}
          {profile.accounts && profile.accounts.length > 0 ? (
            profile.accounts.map((acc, idx) => (
              <p key={idx}>
                Account: {acc.accountNumber ? `XXXX${acc.accountNumber.slice(-4)}` : "XXXXXX"} | Bank: {acc.bankName}
              </p>
            ))
          ) : (
            <p>Account: XXXXXX</p>
          )}
        </div>
      </div>

      {/* Dashboard Cards */}
      <div className="card-grid">
        <div className="card" onClick={() => navigate("/transfer")}>
          <span className="icon">💸</span>
          <h3>Transfer Money</h3>
        </div>
        <div className="card" onClick={() => navigate("/checkBalance")}>
          <span className="icon">🏦</span>
          <h3>Check Balance</h3>
        </div>
        <div className="card" onClick={() => navigate("/chats")}>
          <span className="icon">📜</span>
          <h3>Transaction History</h3>
        </div>
      </div>
    </div>
  );
};

export default Home;
