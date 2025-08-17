import React, { useState } from 'react';
import api from '../api/axiosConfig';
import './Login.css';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom'; // ✅ added

const Login = () => {
  const [accountNumber, setAccountNumber] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); // ✅ added

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post('/login', {
        accountNumber,
        password
      });

      if (res.data && res.data.token) {
        localStorage.setItem('jwt', res.data.token);
        toast.success('Login successful!');
        setTimeout(() => navigate('/checkBalance'), 1500); // ✅ changed
      } else {
        toast.error('Invalid credentials');
      }
    } catch (err) {
      console.error(err);
      toast.error('Something went wrong during login');
    }
  };

  return (
    <div className="login-container">
      <h2>Welcome to UPI App</h2>
      <form className="login-form" onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Account Number"
          value={accountNumber}
          onChange={(e) => setAccountNumber(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>

      <ToastContainer position="top-center" autoClose={2000} />
    </div>
  );
};

export default Login;
