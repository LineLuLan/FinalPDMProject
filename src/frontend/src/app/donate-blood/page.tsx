"use client";

import { useState, useEffect } from "react";
import axios from "axios";
import SelectBloodBankStep from "./SelectBloodBankStep";
import { Button } from "@/components/ui/button";

export default function DonateBloodPage() {
  const [form, setForm] = useState({
    donorSsn: "",
    fullName: "",
    email: "",
    phone: "",
    age: "",
    weight: "",
    bloodType: "",

    message: ""
  });

  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [step, setStep] = useState<"form"|"selectBloodBank"|"done">("form");
  const [lastDonorSsn, setLastDonorSsn] = useState<string>("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };



  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setSuccess("");
    try {
      // Gửi dữ liệu donor về backend
      await axios.post("/api/donors", {
        donor_ssn: form.donorSsn,
        name: form.fullName,
        email: form.email,
        phone: form.phone,
        age: Number(form.age),
        weight: Number(form.weight),
        blood_type: form.bloodType,

        healthStatus: "", // Có thể bổ sung field này nếu muốn
        isEligible: true,
        registrationDate: new Date().toISOString(),
        message: form.message
      });
      setSuccess("");
      setLastDonorSsn(form.donorSsn);
      setStep("selectBloodBank");
      // Không reset form ở đây để donorSsn không bị mất trước khi sang bước tiếp theo
      // setForm({ donorSsn: "", fullName: "", email: "", phone: "", age: "", weight: "", bloodType: "", message: "" });
    } catch (err) {
      setSuccess("Đăng ký thất bại! Vui lòng thử lại.");
    }
    setLoading(false);
  };

  return (
    <div className="p-8 max-w-xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Đăng ký hiến máu</h1>
      {step === "form" && (
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
          <div>
            <label className="block mb-1 font-medium">Mã số hiến máu / SSN</label>
            <input
              type="text"
              name="donorSsn"
              value={form.donorSsn}
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
          <div className="flex gap-4">
            <div className="flex-1">
              <label className="block mb-1 font-medium">Tuổi</label>
              <input
                type="number"
                name="age"
                value={form.age}
                onChange={handleChange}
                className="border rounded px-3 py-2 w-full"
                required
              />
            </div>
            <div className="flex-1">
              <label className="block mb-1 font-medium">Cân nặng (kg)</label>
              <input
                type="number"
                name="weight"
                value={form.weight}
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
              <option value="AB+">AB+</option>
              <option value="AB-">AB-</option>
              <option value="O+">O+</option>
              <option value="O-">O-</option>
            </select>
          </div>
          <div>
            <label className="block mb-1 font-medium">Ghi chú (tùy chọn)</label>
            <textarea
              name="message"
              value={form.message}
              onChange={handleChange}
              className="border rounded px-3 py-2 w-full"
              rows={2}
            />
          </div>
          <Button type="submit" className="w-full" disabled={loading}>
            {loading ? "Đang gửi..." : "Đăng ký"}
          </Button>
          {success && <div className="text-green-600 mt-2 font-semibold">{success}</div>}
        </form>
      )}
      {step === "selectBloodBank" && (
        <SelectBloodBankStep donorSsn={lastDonorSsn} onSuccess={() => setStep("done")} />
      )}
      {step === "done" && (
        <div className="text-green-600 mt-4 font-semibold">Cảm ơn bạn đã hoàn thành đăng ký hiến máu!</div>
      )}
    </div>
  );
}
