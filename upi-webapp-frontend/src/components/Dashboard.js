import React from 'react';
import './Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard-container">
      <h2>Hello Kunal 👋</h2>
      <div className="card-grid">
        <div className="card">💸 Transfer Money</div>
        <div className="card">🏦 Check Balance</div>
        <div className="card">📜 Transaction History</div>
      </div>
    </div>
  );
};

export default Dashboard;
