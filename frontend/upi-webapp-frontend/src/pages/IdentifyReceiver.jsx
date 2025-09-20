import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosTransfer from "../api/axiosTransfer";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./IdentifyReceiver.css";

const IdentifyReceiver = () => {
  const navigate = useNavigate();
  const [method, setMethod] = useState("ACCOUNT_IFSC");
  const [details, setDetails] = useState({
    bankAccount: "",
    ifsc: "",
    mobile: "",
    upiId: "",
  });

  const handleChange = (e) => {
    setDetails({ ...details, [e.target.name]: e.target.value });
  };

  const identifyReceiver = async () => {
    const payload = { method };

    if (method === "ACCOUNT_IFSC") {
      payload.bankAccount = details.bankAccount.trim();
      payload.ifsc = details.ifsc.trim();
    } else if (method === "MOBILE") {
      payload.mobile = details.mobile.trim();
    } else if (method === "UPI") {
      payload.upiId = details.upiId.trim();
    }

    try {
      const res = await axiosTransfer.identifyReceiver(payload);
      toast.success("Receiver identified!");
      navigate("/confirm-transfer", {
        state: { receiver: res.data, method },
      });
    } catch (err) {
      toast.error(err.response?.data || "Failed to identify receiver");
    }
  };

  return (
    <div className="identify-receiver-wrapper">
      <ToastContainer position="top-center" autoClose={2000} />
      <h2>Identify Receiver</h2>

      <label>Transfer Method:</label>
      <select value={method} onChange={(e) => setMethod(e.target.value)}>
        <option value="ACCOUNT_IFSC">Account + IFSC</option>
        <option value="MOBILE">Mobile</option>
        <option value="UPI">UPI ID</option>
      </select>

      {method === "ACCOUNT_IFSC" && (
        <div className="input-group">
          <input
            type="text"
            name="bankAccount"
            placeholder="Account Number"
            value={details.bankAccount}
            onChange={handleChange}
          />
          <input
            type="text"
            name="ifsc"
            placeholder="IFSC Code"
            value={details.ifsc}
            onChange={handleChange}
          />
        </div>
      )}

      {method === "MOBILE" && (
        <input
          type="text"
          name="mobile"
          placeholder="Receiver Mobile"
          value={details.mobile}
          onChange={handleChange}
        />
      )}

      {method === "UPI" && (
        <input
          type="text"
          name="upiId"
          placeholder="Receiver UPI ID"
          value={details.upiId}
          onChange={handleChange}
        />
      )}

      <button onClick={identifyReceiver}>Identify Receiver</button>
    </div>
  );
};

export default IdentifyReceiver;
