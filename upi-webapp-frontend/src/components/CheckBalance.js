import React, { useState } from "react";
import accountApi from "../api/axiosAccount";
import "./CheckBalance.css";

const CheckBalance = () => {
  const [pin, setPin] = useState("");
  const [balance, setBalance] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleCheckBalance = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setBalance(null);

    try {
      const res = await accountApi.getBalance(pin);
      if (res.data?.balance !== undefined) {
        setBalance(res.data.balance);
      } else {
        setError("Balance not found in response");
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to fetch balance");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="check-balance-wrapper">
      <h2>💰 Account Balance</h2>

      {!balance && !error && (
        <form onSubmit={handleCheckBalance}>
          <input
            type="password"
            placeholder="Enter UPI PIN"
            value={pin}
            onChange={(e) => setPin(e.target.value)}
            required
          />
          <button type="submit" disabled={loading}>
            {loading ? "Checking..." : "Check Balance"}
          </button>
        </form>
      )}

      {error && <p className="error-message">{error}</p>}
      {balance !== null && <p className="balance-display">₹ {balance}</p>}
    </div>
  );
};

export default CheckBalance;
