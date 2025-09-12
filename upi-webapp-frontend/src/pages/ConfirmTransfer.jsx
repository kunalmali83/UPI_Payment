import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axiosTransfer from "../api/axiosTransfer";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./ConfirmTransfer.css";

const ConfirmTransfer = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // Receiver info passed from IdentifyReceiver
  const { receiver, method } = location.state || {};

  const [accounts, setAccounts] = useState([]);
  const [form, setForm] = useState({
    fromAccountNo: "",
    amount: "",
    pin: "",
    message: "",
  });

  // Fetch sender's accounts from backend
  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const res = await axiosTransfer.getMyAccounts();
        setAccounts(res.data);
        if (res.data.length > 0) setForm((prev) => ({ ...prev, fromAccountNo: res.data[0].accountNumber }));
      } catch (err) {
        toast.error("Failed to fetch accounts");
      }
    };
    fetchAccounts();
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const confirmTransfer = async () => {
    if (!form.amount || !form.pin) {
      toast.error("Amount and PIN are required");
      return;
    }

    const payload = {
      ...form,
      method,
      receiverMobile: receiver.receiverMobile,
      receiverAccountNo: receiver.receiverAccountNo,
      receiverIfsc: receiver.bankName ? receiver.receiverIfsc : undefined,
      upiId: receiver.upiId,
    };

     try {
    await axiosTransfer.confirmTransfer(payload);
    toast.success("Transaction successful! Redirecting to home...", { autoClose: 2000 });

    // Redirect after 2 seconds to allow toast to show
    setTimeout(() => {
      navigate("/home");
    }, 2000);
    
  } catch (err) {
    toast.error(err.response?.data || "Transaction failed");
  }
};

  if (!receiver) return <p>No receiver information found.</p>;

  return (
    <div className="confirm-transfer-wrapper">
      <ToastContainer position="top-center" autoClose={2000} />
      <h2>Confirm Transfer</h2>

      <p>Sending money to: <strong>{receiver.receiverName}</strong></p>
      <p>Account / Mobile: <strong>{receiver.receiverAccountNo || receiver.receiverMobile}</strong></p>

      <label>From Account:</label>
      <select name="fromAccountNo" value={form.fromAccountNo} onChange={handleChange}>
        {accounts.map((acc) => (
          <option key={acc.accountNumber} value={acc.accountNumber}>
            {acc.accountNumber} - {acc.bankName}
          </option>
        ))}
      </select>

      <input
        type="number"
        name="amount"
        placeholder="Amount"
        value={form.amount}
        onChange={handleChange}
      />
      <input
        type="password"
        name="pin"
        placeholder="PIN"
        value={form.pin}
        onChange={handleChange}
      />
      <input
        type="text"
        name="message"
        placeholder="Message (optional)"
        value={form.message}
        onChange={handleChange}
      />

      <button onClick={confirmTransfer}>Transfer Money</button>
    </div>
  );
};

export default ConfirmTransfer;
