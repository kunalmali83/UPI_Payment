import axiosInstance from "./axiosInstance";

const axiosAccount = {
  getAccounts: () => axiosInstance.get("/users/my-accounts"),
  getPrimaryAccount: () => axiosInstance.get("/users/profile"),

  
  checkBalance: (accountNumber, pin) =>
    axiosInstance.post("/account/checkBalance", { accountNumber, pin }),
};

export default axiosAccount;
