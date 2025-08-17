// src/components/CheckBalance.js
import React, { useState } from 'react';
import accountApi from '../api/axiosAccount';
import './CheckBalance.css';

const CheckBalance = () => {

  const [pin, setPin] = useState('');
  const [result, setResult] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
 console.log("✅ Submit clicked with PIN:", pin); 
    try {
      // Inject accountNumber as a fake principal (not best practice)
      // Instead, assume user is already authenticated, and backend extracts accountNumber from JWT.
     const res = await accountApi.post('/checkBalance', { pin });

      if (res.status === 200) {
        setResult(res.data); // expected string like "Your balance is ₹1000"
      } else {
        setResult('❌ Failed to fetch balance');
      }
    } catch (err) {
      console.error(err);
      if (err.response && err.response.data) {
        setResult(`❌ ${err.response.data}`);
      } else {
        setResult('❌ Something went wrong');
      }
    }
  };

  return (
    <div className="balance-container">
      <h2>Check Account Balance</h2>
      <form onSubmit={handleSubmit} className="balance-form">
        {/* Just asking for PIN because accountNumber is extracted from JWT */}
        <input
          type="password"
          placeholder="Enter your PIN"
          value={pin}
          onChange={(e) => setPin(e.target.value)}
          required
        />
        <button type="submit">Check Balance</button>
      </form>

      {result && <p className="balance-result">{result}</p>}
    </div>
  );
};

export default CheckBalance;
