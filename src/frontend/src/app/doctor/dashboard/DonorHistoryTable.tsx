"use client";
import React, { useEffect, useState } from "react";
import axios from "axios";

interface DonorHistory {
  donationId: number;
  donorSsn: string;
  name?: string;
  bloodType?: string;
  quantity: number;
  date: string;
}

interface DonorDetail {
  donorSsn: string;
  name: string;
  bloodType: string;
  phone: string;
  age: number;
  weight: number;
  lastDonationDate: string;
  healthStatus: string;
  isEligible: boolean;
  registrationDate: string;
  email: string;
}

interface DonorHistoryTableProps {
  bid: number;
}

const DonorHistoryTable: React.FC<DonorHistoryTableProps> = ({ bid }) => {
  const [history, setHistory] = useState<DonorHistory[]>([]);
  const [loading, setLoading] = useState(true);
  const [showDetail, setShowDetail] = useState(false);
  const [selectedDonor, setSelectedDonor] = useState<DonorDetail | null>(null);

  useEffect(() => {
    const fetchHistory = async () => {
      setLoading(true);
      try {
        const res = await axios.get(`/api/donation-history/blood-bank/${bid}/with-donor`);
        setHistory(res.data);

      } catch {
        setHistory([]);
      }
      setLoading(false);
    };
    if (bid) fetchHistory();
  }, [bid]);

  const handleShowDetail = async (donorSsn: string) => {
    try {
      const [donorRes, historyRes] = await Promise.all([
        axios.get(`/api/donors/${donorSsn}`),
        axios.get(`/api/donation-history/donor/${donorSsn}`),
      ]);
      const donor = donorRes.data;
      // Get the latest donation date from donation history
      let lastDonationDate = "-";
      if (Array.isArray(historyRes.data) && historyRes.data.length > 0) {
        const maxDate = historyRes.data
          .map((item: any) => item.date)
          .filter(Boolean)
          .sort((a: string, b: string) => new Date(b).getTime() - new Date(a).getTime())[0];
        if (maxDate) lastDonationDate = maxDate.slice(0, 10);
      }
      setSelectedDonor({
        ...donor,
        donorSsn: donor.donorSsn || donor.donor_ssn || '-',
        bloodType: donor.bloodType || donor.blood_type || '-',
        lastDonationDate,
      });
      setShowDetail(true);
    } catch {
      setSelectedDonor(null);
      setShowDetail(false);
    }
  };

  return (
    <div className="mt-8">
      <h2 className="text-xl font-semibold mb-2">Donation History</h2>
      {loading ? (
        <div>Loading data...</div>
      ) : (
        <table className="min-w-full border">
          <thead>
            <tr>
              <th className="border px-2 py-1">Donor SSN</th>
              <th className="border px-2 py-1">Donor Name</th>
              <th className="border px-2 py-1">Blood Type</th>
              <th className="border px-2 py-1">Quantity</th>
              <th className="border px-2 py-1">Donation Date</th>
              <th className="border px-2 py-1">Details</th>
            </tr>
          </thead>
          <tbody>
            {history.map((item) => (
              <tr key={item.donationId}>
                <td className="border px-2 py-1">{item.donorSsn}</td>
                <td className="border px-2 py-1">{item.donorName || "-"}</td>
                <td className="border px-2 py-1">{item.bloodType && item.bloodType !== "" ? item.bloodType : "-"}</td>
                <td className="border px-2 py-1">{item.quantity}</td>
                <td className="border px-2 py-1">{item.date?.slice(0, 10)}</td>
                <td className="border px-2 py-1">
                  <button
                    className="bg-blue-500 text-white px-2 py-1 rounded"
                    onClick={() => handleShowDetail(item.donorSsn)}
                  >
                    View Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {showDetail && selectedDonor && (
        <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded shadow-lg min-w-[320px] relative">
            <button className="absolute top-2 right-2 text-xl" onClick={() => setShowDetail(false)}>&times;</button>
            <h3 className="text-lg font-bold mb-2">Donor Details</h3>
            <div><b>SSN:</b> {selectedDonor.donorSsn || '-'}</div>
            <div><b>Name:</b> {selectedDonor.name || '-'}</div>
            <div><b>Blood Type:</b> {selectedDonor.bloodType || selectedDonor.blood_type || '-'}</div>
            <div><b>Phone:</b> {selectedDonor.phone}</div>
            <div><b>Age:</b> {selectedDonor.age}</div>
            <div><b>Weight:</b> {selectedDonor.weight}</div>
            <div><b>Latest Donation Date:</b> {selectedDonor.lastDonationDate || '-'}</div>
            <div><b>Health Status:</b> {selectedDonor.healthStatus}</div>
            <div><b>Email:</b> {selectedDonor.email}</div>
            <div><b>Registration Date:</b> {selectedDonor.registrationDate?.slice(0, 10)}</div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DonorHistoryTable;
