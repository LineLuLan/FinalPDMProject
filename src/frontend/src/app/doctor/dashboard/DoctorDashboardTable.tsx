"use client";
import React, { useEffect, useState } from "react";
import DonorRegistrationForm from "./DonorRegistrationForm";
import DonorHistoryTable from "./DonorHistoryTable";
import axios from "axios";

interface ToastProps {
  message: string;
  type: "success" | "error";
  onClose: () => void;
}

const Toast: React.FC<ToastProps> = ({ message, type, onClose }) => (
  <div className={`fixed top-4 right-4 z-50 px-4 py-2 rounded shadow-lg text-white ${type === "success" ? "bg-green-500" : "bg-red-500"}`}>
    <span>{message}</span>
    <button className="ml-4" onClick={onClose}>×</button>
  </div>
);


interface BloodStock {
  stockId: number;
  bloodType: string;
  quantity: number;
  status: string;
  expirationDate: string;
  storageLocation: string;
}

interface BloodRequest {
  requestId: number;
  pssn: string;
  bloodType: string;
  quantity: number;
  status: string;
  requestDate: string;
  responseDate: string | null;
}

interface DoctorDashboardTableProps {
  doctorId: number;
  dssn: string;
}

const DoctorDashboardTable: React.FC<DoctorDashboardTableProps> = ({ doctorId, dssn }) => {
  const [bloodStocks, setBloodStocks] = useState<BloodStock[]>([]);
  const [bloodRequests, setBloodRequests] = useState<BloodRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState<{ message: string; type: "success" | "error" } | null>(null);
  const [showDonorForm, setShowDonorForm] = useState(false);
  const [bid, setBid] = useState<number | null>(null);

  // Fetch doctor info to get bid
  React.useEffect(() => {
    const fetchDoctor = async () => {
      try {
        const res = await axios.get(`/api/doctors/${doctorId}`);
        setBid(res.data.bloodBankId);
      } catch (err) {
        setToast({ message: "Failed to get doctor's blood bank information!", type: "error" });
      }
    };
    fetchDoctor();
  }, [doctorId]);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [stocksRes, requestsRes] = await Promise.all([
        axios.get(`/api/doctors/${doctorId}/blood-stocks`),
        axios.get(`/api/doctors/${doctorId}/blood-requests`),
      ]);
      setBloodStocks(stocksRes.data);
      setBloodRequests(requestsRes.data);
    } catch (err) {
      setToast({ message: "Error loading data!", type: "error" });
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [doctorId]);

  const handleRequestAction = async (requestId: number, action: "accept" | "reject") => {
    try {
      await axios.put(`/api/blood-requests/${requestId}/${action}`);
      setToast({ message: action === "accept" ? "Blood request accepted." : "Blood request rejected.", type: "success" });
      fetchData(); // Refetch to update status
    } catch (err) {
      setToast({ message: "An error occurred while updating the request!", type: "error" });
    }
  };


  if (loading) return <div>Loading dashboard...</div>;

  return (
    <div className="space-y-8">
      {toast && (
        <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />
      )}
      <button
        className="bg-blue-600 text-white px-4 py-2 rounded mb-4 hover:bg-blue-700"
        onClick={() => setShowDonorForm(true)}
      >
        Register Donor
      </button>
      {showDonorForm && bid && (
        <DonorRegistrationForm
          open={showDonorForm}
          onClose={() => setShowDonorForm(false)}
          onSuccess={() => setToast({ message: "Register Donor thành công!", type: "success" })}
          bid={bid}
        />
      )}
      <div>
        <h2 className="text-xl font-semibold mb-2">Blood Stock Status</h2>
        <table className="min-w-full border">
          <thead>
            <tr>
              <th className="border px-2 py-1">Storage Location</th>
              <th className="border px-2 py-1">Blood Type</th>
              <th className="border px-2 py-1">Quantity</th>
              <th className="border px-2 py-1">Status</th>
              <th className="border px-2 py-1">Expiration Date</th>
            </tr>
          </thead>
          <tbody>
            {bloodStocks.map((stock) => (
              <tr key={stock.stockId}>
                <td className="border px-2 py-1">{stock.storageLocation}</td>
                <td className="border px-2 py-1">{stock.bloodType}</td>
                <td className="border px-2 py-1">{stock.quantity}</td>
                <td className="border px-2 py-1">{stock.status}</td>
                <td className="border px-2 py-1">{stock.expirationDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div>
        <h2 className="text-xl font-semibold mb-2">Blood Requests</h2>
        <table className="min-w-full border">
          <thead>
            <tr>
              <th className="border px-2 py-1">Patient SSN</th>
              <th className="border px-2 py-1">Blood Type</th>
              <th className="border px-2 py-1">Quantity</th>
              <th className="border px-2 py-1">Status</th>
              <th className="border px-2 py-1">Request Date</th>
              <th className="border px-2 py-1">Response Date</th>
              <th className="border px-2 py-1">Actions</th>
            </tr>
          </thead>
          <tbody>
            {bloodRequests.map((req) => (
              <tr key={req.requestId}>
                <td className="border px-2 py-1">{req.pssn}</td>
                <td className="border px-2 py-1">{req.bloodType}</td>
                <td className="border px-2 py-1">{req.quantity}</td>
                <td className="border px-2 py-1">{req.status}</td>
                <td className="border px-2 py-1">{req.requestDate}</td>
                <td className="border px-2 py-1">{req.responseDate || "-"}</td>
                <td className="border px-2 py-1 space-x-2">
                  {req.status === "PENDING" && (
                    <>
                      <button className="bg-green-500 text-white px-2 py-1 rounded" onClick={() => handleRequestAction(req.requestId, "accept")}>Accept</button>
                      <button className="bg-red-500 text-white px-2 py-1 rounded" onClick={() => handleRequestAction(req.requestId, "reject")}>Reject</button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* Bảng lịch sử người hiến máu */}
      {bid && <DonorHistoryTable bid={bid} />}
    </div>
  );
};

export default DoctorDashboardTable;
