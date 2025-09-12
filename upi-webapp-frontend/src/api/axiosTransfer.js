import axiosInstance from "./axiosInstance";

const transferApi = {
  // Step 1: Identify receiver
  identifyReceiver: (data) => axiosInstance.post("/transfer/identify", data),

  // Step 2: Confirm and transfer money
  confirmTransfer: (data) => axiosInstance.post("/transfer/confirm-transfer", data),

  // Fetch sender accounts
  getMyAccounts: () => axiosInstance.get("/users/my-accounts"),
};

export default transferApi;
