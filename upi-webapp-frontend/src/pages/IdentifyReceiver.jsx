import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import transferApi from "../api/axiosTransfer";

const IdentifyReceiver = () => {
  const [method, setMethod] = useState("MOBILE");
  const [mobile, setMobile] = useState("");
  const [upiId, setUpiId] = useState("");
  const [bankAccount, setBankAccount] = useState("");
  const [ifsc, setIfsc] = useState("");
  const [result, setResult] = useState(null);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { method };
      if (method === "MOBILE") payload.mobile = mobile;
      if (method === "UPI") payload.upiId = upiId;
      if (method === "ACCOUNT_IFSC") {
        payload.bankAccount = bankAccount;
        payload.ifsc = ifsc;
      }
      const res = await transferApi.identifyReceiver(payload);
      setResult(res.data);
      localStorage.setItem("receiver", JSON.stringify(res.data));
      setTimeout(() => navigate("/confirm-transfer"), 1500);
    } catch (err) {
      console.error(err);
      setResult(err.response?.data || "❌ Something went wrong");
    }
  };

  return (
    <div>
      <h2>Identify Receiver</h2>
      <form onSubmit={handleSubmit}>
        <select value={method} onChange={(e) => setMethod(e.target.value)}>
          <option value="MOBILE">Mobile Number</option>
          <option value="UPI">UPI ID</option>
          <option value="ACCOUNT_IFSC">Account + IFSC</option>
        </select>

        {method === "MOBILE" && (
          <input
            type="text"
            placeholder="Enter mobile number"
            value={mobile}
            onChange={(e) => setMobile(e.target.value)}
            required
          />
        )}
        {method === "UPI" && (
          <input
            type="text"
            placeholder="Enter UPI ID"
            value={upiId}
            onChange={(e) => setUpiId(e.target.value)}
            required
          />
        )}
        {method === "ACCOUNT_IFSC" && (
          <>
            <input
              type="text"
              placeholder="Enter account number"
              value={bankAccount}
              onChange={(e) => setBankAccount(e.target.value)}
              required
            />
            <input
              type="text"
              placeholder="Enter IFSC code"
              value={ifsc}
              onChange={(e) => setIfsc(e.target.value)}
              required
            />
          </>
        )}

        <button type="submit">Confirm Receiver</button>
      </form>

      {result && (
        <p>
          ✅ Receiver Found: {result.receiverName} ({result.receiverAccount})
        </p>
      )}
    </div>
  );
};

export default IdentifyReceiver;
