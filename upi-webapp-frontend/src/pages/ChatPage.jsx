import React, { useState } from "react";
import ChatList from "../components/ChatList";
import ChatWindow from "../components/ChatWindow";

export default function ChatPage() {
  const [selectedMobile, setSelectedMobile] = useState(null);

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <ChatList onSelect={setSelectedMobile} />
      {selectedMobile ? (
        <ChatWindow mobile={selectedMobile} />
      ) : (
        <p style={{ margin: "auto" }}>Select a user to view transaction history</p>
      )}
    </div>
  );
}
