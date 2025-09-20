import axiosInstance from "./axiosInstance";

export const getAllChats = async () => {
  try {
    const res = await axiosInstance.get("/transactions/all-chats");
    return res.data;
  } catch (err) {
    console.error("getAllChats error:", err.response || err);
    throw err;
  }
};

export const getChatHistory = async (mobile) => {
  try {
    const res = await axiosInstance.get(`/transactions/chat-history/${mobile}`);
    return res.data;
  } catch (err) {
    console.error("getChatHistory error:", err.response || err);
    throw err;
  }
};

export const sendMessage = async (messageData) => {
  try {
    const res = await axiosInstance.post("/transactions/send", messageData);
    return res.data;
  } catch (err) {
    console.error("sendMessage error:", err.response || err);
    throw err;
  }
};
