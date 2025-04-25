"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";

export default function DonateBloodPage() {
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    bloodType: "",
    message: ""
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setSuccess("");
    // Fake API call
    setTimeout(() => {
      setLoading(false);
      setSuccess("Cảm ơn bạn đã đăng ký hiến máu! Chúng tôi sẽ liên hệ khi cần thiết.");
      setForm({ fullName: "", email: "", phone: "", bloodType: "", message: "" });
    }, 1000);
  };

  return (
    <div className="p-8 max-w-xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Đăng ký hiến máu</h1>
      <form onSubmit={handleSubmit} className="space-y-4 bg-white rounded-lg shadow p-6">
        <div>
          <label className="block mb-1 font-medium">Họ tên</label>
          <input
            type="text"
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            className="border rounded px-3 py-2 w-full"
            required
          />
        </div>
        <div className="flex gap-4">
          <div className="flex-1">
            <label className="block mb-1 font-medium">Email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              className="border rounded px-3 py-2 w-full"
              required
            />
          </div>
          <div className="flex-1">
            <label className="block mb-1 font-medium">Số điện thoại</label>
            <input
              type="text"
              name="phone"
              value={form.phone}
              onChange={handleChange}
              className="border rounded px-3 py-2 w-full"
              required
            />
          </div>
        </div>
        <div>
          <label className="block mb-1 font-medium">Nhóm máu</label>
          <select
            name="bloodType"
            value={form.bloodType}
            onChange={handleChange}
            className="border rounded px-3 py-2 w-full"
            required
          >
            <option value="">Chọn nhóm máu</option>
            <option value="A+">A+</option>
            <option value="A-">A-</option>
            <option value="B+">B+</option>
            <option value="B-">B-</option>
            <option value="O+">O+</option>
            <option value="O-">O-</option>
            <option value="AB+">AB+</option>
            <option value="AB-">AB-</option>
          </select>
        </div>
        <div>
          <label className="block mb-1 font-medium">Ghi chú (tuỳ chọn)</label>
          <textarea
            name="message"
            value={form.message}
            onChange={handleChange}
            className="border rounded px-3 py-2 w-full"
            rows={3}
          />
        </div>
        <Button type="submit" disabled={loading}>
          {loading ? "Đang gửi..." : "Gửi đăng ký"}
        </Button>
        {success && <div className="text-green-600 mt-2 font-semibold">{success}</div>}
      </form>
    </div>
  );
}
