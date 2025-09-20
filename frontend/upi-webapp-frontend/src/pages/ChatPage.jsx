import "./ChatPage.css";
import React, { useState } from "react";
import ChatList from "../components/ChatList";
import ChatWindow from "../components/ChatWindow";

export default function ChatPage() {
  const [selectedMobile, setSelectedMobile] = useState(null);

  return (
    <div className="chat-page-wrapper">
      <ChatList onSelect={setSelectedMobile} />
      {selectedMobile ? (
        <ChatWindow mobile={selectedMobile} />
      ) : (
        <p className="chat-placeholder">Select a user to view transaction history</p>
      )}
    </div>
  );
}
