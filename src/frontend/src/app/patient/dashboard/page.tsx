"use client";

import React, { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import BloodRequestTable from "./BloodRequestTable";

export default function PatientDashboard() {
  // All hooks must be called at the top, unconditionally
  const { data: session, status } = useSession();
  const router = useRouter();
  const [patient, setPatient] = useState<any>(null);
  const [requests, setRequests] = useState<any[]>([]);
  const [quantity, setQuantity] = useState(100);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Redirect logic in useEffect
  useEffect(() => {
    if (status === "loading") return;
    if (!session) {
      router.replace("/login");
    } else if (session.user && session.user.role !== "PATIENT") {
      router.replace("/doctor/dashboard");
    }
  }, [session, status, router]);

  // Fetch patient info and blood requests
  useEffect(() => {
    // Defensive: only fetch if session and session.user and pssn exist
    const pssn = session && session.user && session.user.pssn;
    if (!pssn) return;
    // Get patient info
    fetch(`/api/patients/${pssn}`)
      .then(res => res.json())
      .then(data => setPatient(data));
    // Get blood requests
    fetch(`/api/blood-requests?pssn=${pssn}`)
      .then(res => res.json())
      .then(data => setRequests(Array.isArray(data) ? data : []));
  }, [session]);

  // Submit new blood request
  const handleCreateRequest = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    if (!patient?.bloodType) {
      setError('Không xác định được nhóm máu bệnh nhân!');
      return;
    }
    if (!quantity || quantity < 100) {
      setError('Số lượng phải lớn hơn hoặc bằng 100!');
      return;
    }
    setLoading(true);
    try {
      const res = await fetch('/api/blood-requests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          pssn: session.user.pssn,
          blood_type: patient.bloodType,
          quantity: quantity,
          status: "PENDING",
        })
      });
      if (!res.ok) throw new Error('Tạo yêu cầu thất bại!');
      // After successful POST, re-fetch the list from backend for consistency
      const pssn = session.user.pssn;
      const refreshed = await fetch(`/api/blood-requests?pssn=${pssn}`);
      const refreshedData = await refreshed.json();
      setRequests(Array.isArray(refreshedData) ? refreshedData : []);
      setQuantity(100);
    } catch (e: any) {
      setError(e.message);
    }
    setLoading(false);
  };

  return (
    <div className="p-8 max-w-3xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Patient Dashboard</h1>
      {/* --- Form tạo yêu cầu nhận máu --- */}
      <form onSubmit={handleCreateRequest} className="mb-8 flex gap-4 items-end bg-gray-50 p-4 rounded-lg shadow">
        <div>
          <label className="block mb-1 font-medium">Nhóm máu</label>
          <input
            className="border rounded px-3 py-2 w-32 bg-gray-100"
            value={patient?.bloodType || ''}
            disabled
          />
        </div>
        <div>
          <label className="block mb-1 font-medium">Số lượng (ml)</label>
          <input
            type="number"
            className="border rounded px-3 py-2 w-32"
            min={100}
            value={quantity}
            onChange={e => setQuantity(Number(e.target.value))}
            required
          />
        </div>
        <button type="submit" className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600" disabled={loading}>
          {loading ? 'Đang gửi...' : 'Gửi yêu cầu'}
        </button>
        {error && <span className="text-red-600 ml-2">{error}</span>}
      </form>

      {/* --- Lịch sử yêu cầu nhận máu --- */}
      <React.Suspense fallback={<div>Đang tải lịch sử...</div>}>
        <BloodRequestTable requests={requests} />
      </React.Suspense>
    </div>
  );
}
