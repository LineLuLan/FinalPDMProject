"use client";
import React, { useState } from "react";
import axios from "axios";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
  bid: number;
}

const initialState = {
  donor_ssn: "",
  name: "",
  gender: "",
  age: "",
  blood_type: "",
  weight: "",
  health_status: "",
  email: "",
  phone: ""
};

const DonorRegistrationForm: React.FC<Props> = ({ open, onClose, onSuccess, bid }) => {
  const [form, setForm] = useState(initialState);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [showQuantityModal, setShowQuantityModal] = useState(false);
  const [donorSsn, setDonorSsn] = useState("");
  const [quantity, setQuantity] = useState("");
  const [quantityError, setQuantityError] = useState<string | null>(null);

  if (!open) return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const validate = () => {
    if (!form.donor_ssn || !form.name || !form.gender || !form.age || !form.blood_type || !form.weight || !form.email || !form.phone) {
      return "Please fill in all required fields.";
    }
    const age = Number(form.age);
    if (isNaN(age) || age < 18 || age > 65) return "Age valid is 18-65.";
    const weight = Number(form.weight);
    if (isNaN(weight) || weight < 50) return "Weight must be over 50kg.";
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    const v = validate();
    if (v) {
      setError(v);
      return;
    }
    setLoading(true);
    try {
      await axios.post("/api/donors", {
        donor_ssn: form.donor_ssn,
        name: form.name,
        gender: form.gender,
        age: Number(form.age),
        blood_type: form.blood_type,
        weight: Number(form.weight),
        health_status: form.health_status,
        email: form.email,
        phone: form.phone
      });
      setDonorSsn(form.donor_ssn);
      setShowQuantityModal(true);
    } catch (err: any) {
      let msg = err?.response?.data;
      if (typeof msg === "object" && msg !== null) msg = msg.message || JSON.stringify(msg);
      setError(msg || "Registration failed!");
    } finally {
      setLoading(false);
    }
  };

  const handleQuantitySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setQuantityError(null);
    const q = Number(quantity);
    if (isNaN(q) || q < 100 || q > 500) {
      setQuantityError("Blood amount must be between 100 and 500 ml");
      return;
    }
    setLoading(true);
    try {
      await axios.post("/api/donation-history", {
        donorSsn: donorSsn,
        donor_ssn: donorSsn, // gửi cả hai kiểu
        bid: bid,
        quantity: q
      });
      setShowQuantityModal(false);
      setForm(initialState);
      setQuantity("");
      setDonorSsn("");
      onSuccess();
      onClose();
    } catch (err: any) {
      let msg = err?.response?.data;
      if (typeof msg === "object" && msg !== null) msg = msg.message || JSON.stringify(msg);
      setQuantityError(msg || "Failed saving historyhistory!");
    } finally {
      setLoading(false);
    }
  };


  return (
    <>
      <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
        <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md relative">
          <button onClick={onClose} className="absolute top-2 right-3 text-xl">×</button>
          <h2 className="text-2xl font-bold mb-4">Register Donor</h2>
          <form onSubmit={handleSubmit} className="space-y-3">
            <input name="donor_ssn" value={form.donor_ssn} onChange={handleChange} placeholder="Donor SSN" className="w-full border p-2 rounded" required />
            <input name="name" value={form.name} onChange={handleChange} placeholder="Donor Name" className="w-full border p-2 rounded" required />
            <select name="gender" value={form.gender} onChange={handleChange} className="w-full border p-2 rounded" required>
              <option value="">Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
            <input name="age" value={form.age} onChange={handleChange} placeholder="Age" type="number" className="w-full border p-2 rounded" required min={18} max={65}/>
            <select name="blood_type" value={form.blood_type} onChange={handleChange} className="w-full border p-2 rounded" required>
              <option value="">Blood Type</option>
              <option value="A+">A+</option>
              <option value="A-">A-</option>
              <option value="B+">B+</option>
              <option value="B-">B-</option>
              <option value="O+">O+</option>
              <option value="O-">O-</option>
              <option value="AB+">AB+</option>
              <option value="AB-">AB-</option>
            </select>
            <input name="weight" value={form.weight} onChange={handleChange} placeholder="Weight (kg)" type="number" className="w-full border p-2 rounded" required min={50}/>
            <input name="health_status" value={form.health_status} onChange={handleChange} placeholder="Health Status (optional)" className="w-full border p-2 rounded" />
            <input name="email" value={form.email} onChange={handleChange} placeholder="Email" className="w-full border p-2 rounded" required />
            <input name="phone" value={form.phone} onChange={handleChange} placeholder="Phone" className="w-full border p-2 rounded" required />
            {error && <div className="text-red-600 text-sm">{error}</div>}
            <button type="submit" className="w-full bg-red-600 text-white py-2 rounded" disabled={loading}>{loading ? "Registering..." : "Đăng ký"}</button>
          </form>
        </div>
      </div>
      {showQuantityModal && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md relative">
            <h2 className="text-xl font-bold mb-4">Enter amount of blood to donate (ml)</h2>
            <form onSubmit={handleQuantitySubmit} className="space-y-3">
              <input
                type="number"
                min={100}
                max={500}
                value={quantity}
                onChange={e => setQuantity(e.target.value)}
                className="w-full border p-2 rounded"
                placeholder="Blood amount (100-500ml)"
                required
              />
              {quantityError && <div className="text-red-600 text-sm">{quantityError}</div>}
              <div className="flex space-x-2">
                <button type="submit" className="bg-red-600 text-white py-2 px-4 rounded" disabled={loading}>{loading ? "Saving..." : "Save"}</button>
                <button type="button" className="bg-gray-300 py-2 px-4 rounded" onClick={() => setShowQuantityModal(false)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
};

export default DonorRegistrationForm;
