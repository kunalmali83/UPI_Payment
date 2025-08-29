import React, { useState, useEffect } from "react";
import transferApi from "../api/axiosTransfer";

const ConfirmTransfer = () => {
  const [receiver, setReceiver] = useState(null);
  const [amount, setAmount] = useState("");
  const [pin, setPin] = useState("");
  const [messageError, setMessageError] = useState("");
  const [messageSuccess, setMessageSuccess] = useState("");

  // Load receiver info from localStorage
  useEffect(() => {
    const saved = localStorage.getItem("receiver");
    if (saved) setReceiver(JSON.parse(saved));
    else {
      setReceiver(null);
      setMessageError("❌ No receiver selected");
    }
  }, []);

  const handleTransfer = async (e) => {
    e.preventDefault();

    if (!receiver) {
      setMessageError("❌ No receiver selected");
      setMessageSuccess("");
      return;
    }

    // Prepare payload matching TransferRequestDTO
    const payload = {
      receiverAccountNo: receiver.receiverAccount || null,
      receiverIfsc: receiver.receiverIfsc || null,
      receiverMobileNumber: receiver.receiverMobileNumber || null,
      receiverUpiId: receiver.receiverUpiId || null,
      amount: Number(amount),
      pin,
      message: messageSuccess || "",
    };

    try {
      const res = await transferApi.confirmTransfer(payload);
      setMessageSuccess(`✅ ${res.data}`);
      setMessageError("");
      // Clear localStorage and form
      localStorage.removeItem("receiver");
      setReceiver(null);
      setAmount("");
      setPin("");
    } catch (err) {
      console.error(err);
      setMessageError(err.response?.data || "❌ Transfer failed");
      setMessageSuccess("");
    }
  };

  return (
    <div>
      <h2>Confirm Transfer</h2>

      {receiver && (
        <p>
          Sending money to <b>{receiver.receiverName}</b> (A/C:{" "}
          {receiver.receiverAccount || receiver.receiverUpiId || receiver.receiverMobileNumber})
        </p>
      )}

      <form onSubmit={handleTransfer}>
        <input
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Enter PIN"
          value={pin}
          onChange={(e) => setPin(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Optional message"
          value={messageSuccess} // optional message
          onChange={(e) => setMessageSuccess(e.target.value)}
        />
        <button type="submit">Send Money</button>
      </form>

      {messageError && <p style={{ color: "red" }}>{messageError}</p>}
      {messageSuccess && <p style={{ color: "green" }}>{messageSuccess}</p>}
    </div>
  );
};

export default ConfirmTransfer;
