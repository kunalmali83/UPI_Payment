import React, { useEffect, useState } from "react";
import axios from "axios";

const ChatList = ({ onSelect }) => {
  const [chats, setChats] = useState([]);

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
    <div className="chat-list">
      <h3>Users You Transacted With</h3>
      <ul className="chat-list-ul">
        {chats.map((chat, i) => (
          <li
            key={i}
            onClick={() => onSelect(chat.otherMobile)}
            className="chat-list-item"
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
