import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import userApi from "../api/axiosUser";
import "./Register.css";

const Register = () => {
  const navigate = useNavigate();
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

  // Check if form is ready for sending OTP
  const isFormValidForOtp = () => {
    if (!formData.name || !formData.email || !formData.password || !formData.address || !formData.mobileNumber) return false;
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(formData.email)) return false;
    return formData.accounts.some(acc => acc.accountNumber && acc.accountHolder && acc.bankName && acc.pin && acc.ifsc);
  };

  const handleUserChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleAccountChange = (index, e) => {
    const updatedAccounts = [...formData.accounts];
    updatedAccounts[index][e.target.name] = e.target.value;
    setFormData({ ...formData, accounts: updatedAccounts });
  };

  const addAccount = () => setFormData(prev => ({
    ...prev,
    accounts: [...prev.accounts, { accountNumber: "", accountHolder: "", bankName: "", pin: "", ifsc: "" }]
  }));

  const removeAccount = (index) => setFormData(prev => ({
    ...prev,
    accounts: prev.accounts.filter((_, i) => i !== index)
  }));

  const sendOtp = async () => {
    if (!isFormValidForOtp()) {
      toast.error("⚠️ Fill all required fields and at least one complete bank account!");
      return;
    }
    try {
      await userApi.sendOtp(formData.email);
      setOtpSent(true);
      toast.success("✅ OTP sent to your email!");
    } catch (err) {
      console.error(err);
      toast.error("❌ Failed to send OTP. Try again.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await userApi.verifyOtpRegister(formData);
      toast.success("✅ Registered successfully! Redirecting to login...");
      setTimeout(() => navigate("/login"), 2000);
    } catch (err) {
      console.error(err);
      toast.error("❌ " + (err.response?.data?.message || "Unknown error"));
    }
  };

  return (
    <div className="register-wrapper">
      <ToastContainer position="top-center" autoClose={2000} />
      <div className="register-card">
        <h2 className="register-title">Create Your Account</h2>

        <form onSubmit={handleSubmit}>
          {/* User Info */}
          <div className="form-group">
            <input type="text" name="name" placeholder="Full Name" required onChange={handleUserChange} />
            <input type="email" name="email" placeholder="Email Address" required onChange={handleUserChange} />
            <input type="password" name="password" placeholder="Password" required onChange={handleUserChange} />
            <input type="text" name="address" placeholder="Address" required onChange={handleUserChange} />
            <input type="text" name="mobileNumber" placeholder="Mobile Number" required onChange={handleUserChange} />
          </div>

          <h4 className="section-title">Bank Account Details</h4>
          {formData.accounts.map((acc, index) => (
            <div key={index} className="account-form">
              <input type="text" name="accountNumber" placeholder="Account Number" value={acc.accountNumber} onChange={e => handleAccountChange(index, e)} />
              <input type="text" name="accountHolder" placeholder="Account Holder" value={acc.accountHolder} onChange={e => handleAccountChange(index, e)} />
              <input type="text" name="bankName" placeholder="Bank Name" value={acc.bankName} onChange={e => handleAccountChange(index, e)} />
              <input type="password" name="pin" placeholder="PIN" value={acc.pin} onChange={e => handleAccountChange(index, e)} />
              <input type="text" name="ifsc" placeholder="IFSC" value={acc.ifsc} onChange={e => handleAccountChange(index, e)} />
              {index > 0 && <button type="button" className="remove-btn" onClick={() => removeAccount(index)}>Remove</button>}
            </div>
          ))}
          <button type="button" className="add-btn" onClick={addAccount}>+ Add Another Account</button>

          {/* OTP */}
          {!otpSent ? (
            <button type="button" className="otp-btn" onClick={sendOtp} disabled={!isFormValidForOtp()}>Send OTP</button>
          ) : (
            <input type="text" name="otp" placeholder="Enter OTP" required onChange={(e) => setFormData({ ...formData, otp: e.target.value })} />
          )}

          <button type="submit" className="submit-btn">Register</button>
        </form>
      </div>
    </div>
  );
};

export default Register;
