import React, { useState, useEffect } from "react";
import axios from "axios";

const ChatWindow = ({ mobile }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    if (!mobile) return;

    const token = localStorage.getItem("token");
    axios
      .get(`http://localhost:8081/transactions/chat-history/${mobile}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setMessages(res.data))
      .catch((err) => console.error(err));
  }, [mobile]);

  return (
    <div style={{ flex: 1, padding: "20px" }}>
      <h3>Transaction History with {mobile}</h3>
      {messages.length === 0 ? (
        <p>No transactions yet.</p>
      ) : (
        <ul style={{ listStyle: "none", padding: 0 }}>
          {messages.map((msg, i) => (
            <li key={i} style={{ marginBottom: "10px", padding: "8px", background: "#f9f9f9", borderRadius: "8px" }}>
              <b>{msg.senderMobile}</b> → <b>{msg.receiverMobile}</b> <br />
              Amount: ₹{msg.amount} <br />
              Message: {msg.message || "—"} <br />
              <small>{new Date(msg.timestamp).toLocaleString()}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default ChatWindow;
