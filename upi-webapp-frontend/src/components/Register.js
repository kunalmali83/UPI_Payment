import React, { useState } from 'react';
import api from '../api/axiosUser'; 
import './Register.css';

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    address: '',
    mobileNumber: '',
    accounts: [{
      accountNumber: '',
      accountHolder: '',
      bankName: '',
      pin: '',
      ifsc:''
    }]
  });

  const [message, setMessage] = useState('');

  const handleUserChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleAccountChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      accounts: [{ ...prev.accounts[0], [name]: value }]
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/signup', formData);
      if (res.status === 200) {
        setMessage('✅ Registered successfully!');
      } else {
        setMessage('❌ Registration failed.');
      }
    } catch (err) {
      console.error(err);
      setMessage('❌ Something went wrong.');
    }
  };

  return (
    <div className="register-container">
      <h2>User Registration</h2>
      <form className="register-form" onSubmit={handleSubmit}>
        <input type="text" name="name" placeholder="Name" required onChange={handleUserChange} />
        <input type="email" name="email" placeholder="Email" required onChange={handleUserChange} />
        <input type="password" name="password" placeholder="Password" required onChange={handleUserChange} />
        <input type="text" name="address" placeholder="Address" required onChange={handleUserChange} />
        <input type="text" name="mobileNumber" placeholder="Mobile Number" required onChange={handleUserChange} />

        <hr />
        <h4>Bank Account Details</h4>
        <input type="text" name="accountNumber" placeholder="Account Number" required onChange={handleAccountChange} />
        <input type="text" name="accountHolder" placeholder="Account Holder" required onChange={handleAccountChange} />
        <input type="text" name="bankName" placeholder="Bank Name" required onChange={handleAccountChange} />
        <input type="password" name="pin" placeholder="PIN" required onChange={handleAccountChange} />
         <input type="text" name="ifsc" placeholder="IFSC" required onChange={handleAccountChange} />

        <button type="submit">Register</button>
        {message && <p>{message}</p>}
      </form>
    </div>
  );
};

export default Register;
