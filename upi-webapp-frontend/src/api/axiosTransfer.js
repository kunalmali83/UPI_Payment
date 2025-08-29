import axios from "axios";

const API_BASE_URL = "http://localhost:8081/api"; // base API URL

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Attach JWT for all requests
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers["Authorization"] = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// Export transfer-specific API calls
const transferApi = {
  identifyReceiver: (data) => axiosInstance.post("/transfer/identify", data),
  confirmTransfer: (data) => axiosInstance.post("/transfer/confirm-transfer", data),
};

export default transferApi;
