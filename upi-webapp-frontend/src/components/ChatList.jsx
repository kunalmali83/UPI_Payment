import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ChatList = ({ onSelect }) => {
  const [chats, setChats] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("http://localhost:8081/transactions/all-chats", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setChats(res.data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div style={{ width: "300px", padding: "20px", borderRight: "1px solid #ccc" }}>
      <h3>Users You Transacted With</h3>
      <ul style={{ listStyle: "none", padding: 0 }}>
        {chats.map((chat, i) => (
          <li
            key={i}
            onClick={() => onSelect(chat.otherMobile)}
            style={{
              cursor: "pointer",
              marginBottom: "10px",
              padding: "10px",
              background: "#f5f5f5",
              borderRadius: "8px",
            }}
          >
            <b>{chat.otherMobile}</b> ({chat.otherUpi}) <br />
            Last Transaction: {new Date(chat.lastTransactionTime).toLocaleString()} <br />
            Amount: ₹{chat.lastAmount}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ChatList;
