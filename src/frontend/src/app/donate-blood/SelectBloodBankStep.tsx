import React, { useEffect, useState } from "react";
import axios from "axios";

interface SelectBloodBankStepProps {
  donorSsn: string;
  onSuccess: () => void;
}

const SelectBloodBankStep: React.FC<SelectBloodBankStepProps> = ({ donorSsn, onSuccess }) => {
  const [bloodBanks, setBloodBanks] = useState<{ bid: number; name: string }[]>([]);
  const [selectedBank, setSelectedBank] = useState("");
  const [quantity, setQuantity] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    axios.get("/api/bloodBanks").then(res => setBloodBanks(res.data));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");
    try {
      console.log('[DEBUG] Gửi donation-history:', { donorSsn });
      await axios.post("/api/donation-history", {
        donorSsn: donorSsn,
        donor_ssn: donorSsn, // gửi cả 2 kiểu để backend nhận đúng
        bid: Number(selectedBank),
        quantity: Number(quantity)
      });
      setSuccess("Đăng ký hiến máu thành công!");
      onSuccess();
    } catch (err) {
      setError("Có lỗi xảy ra khi ghi nhận hiến máu. Vui lòng thử lại!");
    }
    setLoading(false);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 bg-white rounded-lg shadow p-6 mt-6 max-w-xl mx-auto">
      <h2 className="text-xl font-semibold mb-2">Chọn ngân hàng máu và số lượng muốn hiến</h2>
      <div>
        <label className="block mb-1 font-medium">Ngân hàng máu</label>
        <select
          value={selectedBank}
          onChange={e => setSelectedBank(e.target.value)}
          className="border rounded px-3 py-2 w-full"
          required
        >
          <option value="">Chọn ngân hàng máu</option>
          {bloodBanks.map(bank => (
            <option key={bank.bid} value={bank.bid}>{bank.name}</option>
          ))}
        </select>
      </div>
      <div>
        <label className="block mb-1 font-medium">Số lượng máu muốn hiến (ml)</label>
        <input
          type="number"
          min="100"
          max="500"
          step="50"
          value={quantity}
          onChange={e => setQuantity(e.target.value)}
          className="border rounded px-3 py-2 w-full"
          required
        />
      </div>
      <button type="submit" className="bg-red-500 text-white px-4 py-2 rounded" disabled={loading}>
        {loading ? "Đang gửi..." : "Xác nhận hiến máu"}
      </button>
      {success && <div className="text-green-600 mt-2 font-semibold">{success}</div>}
      {error && <div className="text-red-600 mt-2 font-semibold">{error}</div>}
    </form>
  );
};

export default SelectBloodBankStep;
