import React, { useState, useEffect } from "react";
import axiosAccount from "../api/axiosAccount";
import "./CheckBalance.css";

const CheckBalance = () => {
  const [pin, setPin] = useState("");
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState("");
  const [balance, setBalance] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Load user's accounts to select
    axiosAccount.getAccounts()
      .then(res => {
        setAccounts(res.data);
        if (res.data.length > 0) setSelectedAccount(res.data[0].accountNumber);
      })
      .catch(err => console.error(err));
  }, []);

  const handleCheckBalance = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setBalance(null);

    try {
      const res = await axiosAccount.checkBalance(selectedAccount, pin);
      setBalance(res.data.balance);
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to fetch balance");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="check-balance-wrapper">
      <h2>💰 Account Balance</h2>

      <form onSubmit={handleCheckBalance}>
        <div>
          <label>Select Account:</label>
          <select
            value={selectedAccount}
            onChange={e => setSelectedAccount(e.target.value)}
          >
            {accounts.map(acc => (
              <option key={acc.accountNumber} value={acc.accountNumber}>
                {acc.accountHolder} - {acc.accountNumber} ({acc.bankName})
              </option>
            ))}
          </select>
        </div>

        <div>
          <input
            type="password"
            placeholder="Enter UPI PIN"
            value={pin}
            onChange={(e) => setPin(e.target.value)}
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Checking..." : "Check Balance"}
        </button>
      </form>

      {error && <p className="error-message">{error}</p>}
      {balance !== null && <p className="balance-display">₹ {balance}</p>}
    </div>
  );
};

export default CheckBalance;
