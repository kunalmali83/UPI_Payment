import React, { useEffect, useState } from "react";
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
    <div className="chat-window">
      <h3>Transaction History with {mobile}</h3>
      {messages.length === 0 ? (
        <p>No transactions yet.</p>
      ) : (
        <ul className="chat-window-ul">
          {messages.map((msg, i) => (
            <li key={i} className="chat-window-item">
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
