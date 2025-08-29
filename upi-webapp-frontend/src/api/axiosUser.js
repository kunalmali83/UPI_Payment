import axios from "axios";

const API_BASE_URL = "http://localhost:8081/api"; // just the base URL

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Optional: attach JWT for protected endpoints
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers["Authorization"] = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// Export user-specific API calls
const userApi = {
  login: (data) => axiosInstance.post("/users/login", data),
  register: (data) => axiosInstance.post("/users/register", data),
  getProfile: () => axiosInstance.get("/users/profile"),
};

export default userApi;
