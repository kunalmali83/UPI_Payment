// src/api/axiosAccount.js
import axios from "axios";

// Create an Axios instance
const accountApi = axios.create({
  baseURL: "http://localhost:8081", // Spring Boot backend runs here
  headers: {
    "Content-Type": "application/json",
  },
});

// If you are using JWT authentication, attach token from localStorage
accountApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // token saved after login
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default accountApi;
