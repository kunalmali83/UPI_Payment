import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/users/login'; // Change this if your backend URL is different

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;


