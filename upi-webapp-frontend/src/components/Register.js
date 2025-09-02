import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // <-- import
import { toast, ToastContainer } from "react-toastify"; // <-- import toast
import "react-toastify/dist/ReactToastify.css";
import api from "../api/axiosUser"; 
import "./Register.css";

const Register = () => {
  const navigate = useNavigate(); // <-- for redirect
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    address: "",
    mobileNumber: "",
    accounts: [{ accountNumber: "", accountHolder: "", bankName: "", pin: "", ifsc: "" }],
    otp: "",
  });

  const [otpSent, setOtpSent] = useState(false);

  // User input handlers
  const handleUserChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });
  const handleAccountChange = (index, e) => {
    const { name, value } = e.target;
    const updatedAccounts = [...formData.accounts];
    updatedAccounts[index][name] = value;
    setFormData({ ...formData, accounts: updatedAccounts });
  };
  const addAccount = () => setFormData(prev => ({
    ...prev,
    accounts: [...prev.accounts, { accountNumber: "", accountHolder: "", bankName: "", pin: "", ifsc: "" }]
  }));
  const removeAccount = (index) => setFormData(prev => ({
    ...prev,
    accounts: prev.accounts.filter((_, i) => i !== index),
  }));

  // Send OTP
  const sendOtp = async () => {
    try {
      await api.sendOtp(formData.email);
      setOtpSent(true);
      toast.success("✅ OTP sent to your email!");
    } catch (err) {
      console.error("Send OTP error:", err);
      toast.error("❌ Failed to send OTP");
    }
  };

  // Submit registration
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.verifyOtpRegister(formData);
      toast.success("✅ Registered successfully! Redirecting to login...");
      setTimeout(() => {
        navigate("/login"); // <-- redirect to login page
      }, 2000); // 2 seconds delay
    } catch (err) {
      console.error("Registration error:", err);
      const backendMessage = err.response?.data?.message || "Unknown error";
      toast.error("❌ " + backendMessage);
    }
  };

  return (
    <div className="register-container">
      <ToastContainer /> {/* <-- Toast container */}
      <h2>User Registration</h2>
      <form className="register-form" onSubmit={handleSubmit}>
        {/* User info */}
        <input type="text" name="name" placeholder="Name" required onChange={handleUserChange} />
        <input type="email" name="email" placeholder="Email" required onChange={handleUserChange} />
        <input type="password" name="password" placeholder="Password" required onChange={handleUserChange} />
        <input type="text" name="address" placeholder="Address" required onChange={handleUserChange} />
        <input type="text" name="mobileNumber" placeholder="Mobile Number" required onChange={handleUserChange} />

        <hr />
        <h4>Bank Account Details</h4>
        {formData.accounts.map((acc, index) => (
          <div key={index} className="account-form">
            <input type="text" name="accountNumber" placeholder="Account Number" required value={acc.accountNumber} onChange={(e) => handleAccountChange(index, e)} />
            <input type="text" name="accountHolder" placeholder="Account Holder" required value={acc.accountHolder} onChange={(e) => handleAccountChange(index, e)} />
            <input type="text" name="bankName" placeholder="Bank Name" required value={acc.bankName} onChange={(e) => handleAccountChange(index, e)} />
            <input type="password" name="pin" placeholder="PIN" required value={acc.pin} onChange={(e) => handleAccountChange(index, e)} />
            <input type="text" name="ifsc" placeholder="IFSC" required value={acc.ifsc} onChange={(e) => handleAccountChange(index, e)} />
            {index > 0 && <button type="button" onClick={() => removeAccount(index)}>Remove Account</button>}
          </div>
        ))}

        <button type="button" onClick={addAccount}>+ Add Another Account</button>

        <hr />
        {/* OTP Section */}
        {!otpSent ? (
          <button type="button" onClick={sendOtp}>Send OTP to Email</button>
        ) : (
          <input type="text" name="otp" placeholder="Enter OTP" required onChange={(e) => setFormData({ ...formData, otp: e.target.value })} />
        )}

        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default Register;
