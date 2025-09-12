import axiosInstance from "./axiosInstance";

const userApi = {
  login: (data) => axiosInstance.post("/users/login", data),
  register: (data) => axiosInstance.post("/users/signup", data),
  getProfile: () => axiosInstance.get("/users/profile"),
  getMyAccounts: () => axiosInstance.get("/users/my-accounts"),

  // OTP APIs
  sendOtp: (email) => axiosInstance.post("/users/send-otp", { email }),
  verifyOtpRegister: (data) =>
    axiosInstance.post("/users/verify-otp-register", data),
};

export default userApi;
