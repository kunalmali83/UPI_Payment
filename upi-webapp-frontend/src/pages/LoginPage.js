import React from 'react';
import Login from '../components/Login';

const LoginPage = () => {
  const handleLoginSuccess = () => {
    // redirect to dashboard or homepage
    console.log('Login successful!');
  };

  return <Login onLoginSuccess={handleLoginSuccess} />;
};

export default LoginPage;
