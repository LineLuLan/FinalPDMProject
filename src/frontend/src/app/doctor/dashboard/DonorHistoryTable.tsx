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
        const res = await axios.get(`/api/donation-history/blood-bank/${bid}`);
        // Group by donorSsn, lấy ngày hiến mới nhất và enrich thông tin donor
        const groupMap: Record<string, DonorHistory> = {};
        for (const item of res.data) {
          if (!groupMap[item.donorSsn] || new Date(item.date) > new Date(groupMap[item.donorSsn].date)) {
            groupMap[item.donorSsn] = item;
          }
        }
        const enriched = await Promise.all(Object.values(groupMap).map(async (item: DonorHistory) => {
          try {
            const donorRes = await axios.get(`/api/donors/${item.donorSsn}`);
            console.log("Donor info for SSN:", item.donorSsn, donorRes.data);
            return {
              ...item,
              name: donorRes.data.name,
              bloodType: donorRes.data.bloodType || donorRes.data.blood_type || item.bloodType || '-',
              lastDonationDate: item.date?.slice(0,10)
            };
          } catch {
            return item;
          }
        }));
        setHistory(enriched);
      } catch {
        setHistory([]);
      }
      setLoading(false);
    };
    if (bid) fetchHistory();
  }, [bid]);

  const handleShowDetail = async (donorSsn: string) => {
    try {
      const res = await axios.get(`/api/donors/${donorSsn}`);
      const donor = res.data;
      setSelectedDonor({
        ...donor,
        donorSsn: donor.donorSsn || donor.donor_ssn || '-',
        bloodType: donor.bloodType || donor.blood_type || '-',
      });
      setShowDetail(true);
    } catch {
      setSelectedDonor(null);
      setShowDetail(false);
    }
  };


  return (
    <div className="mt-8">
      <h2 className="text-xl font-semibold mb-2">Lịch sử người hiến máu</h2>
      {loading ? (
        <div>Đang tải dữ liệu...</div>
      ) : (
        <table className="min-w-full border">
          <thead>
            <tr>
              <th className="border px-2 py-1">SSN</th>
              <th className="border px-2 py-1">Tên</th>
              <th className="border px-2 py-1">Nhóm máu</th>
              <th className="border px-2 py-1">Quantity</th>
              <th className="border px-2 py-1">Ngày hiến</th>
              <th className="border px-2 py-1">Chi tiết</th>
            </tr>
          </thead>
          <tbody>
            {history.map((item) => (
              <tr key={item.donationId}>
                <td className="border px-2 py-1">{item.donorSsn}</td>
                <td className="border px-2 py-1">{item.name || "-"}</td>
                <td className="border px-2 py-1">{item.bloodType && item.bloodType !== "" ? item.bloodType : "-"}</td>
                <td className="border px-2 py-1">{item.quantity}</td>
                <td className="border px-2 py-1">{item.date?.slice(0, 10)}</td>
                <td className="border px-2 py-1">
                  <button
                    className="bg-blue-500 text-white px-2 py-1 rounded"
                    onClick={() => handleShowDetail(item.donorSsn)}
                  >
                    Xem chi tiết
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
            <h3 className="text-lg font-bold mb-2">Thông tin chi tiết người hiến</h3>
            <div><b>SSN:</b> {selectedDonor.donorSsn || '-'}</div>
            <div><b>Tên:</b> {selectedDonor.name || '-'}</div>
            <div><b>Nhóm máu:</b> {selectedDonor.bloodType || selectedDonor.blood_type || '-'}</div>
            <div><b>SĐT:</b> {selectedDonor.phone}</div>
            <div><b>Tuổi:</b> {selectedDonor.age}</div>
            <div><b>Cân nặng:</b> {selectedDonor.weight}</div>
            <div><b>Ngày hiến gần nhất:</b> {selectedDonor.lastDonationDate || '-'}</div>
            <div><b>Tình trạng sức khỏe:</b> {selectedDonor.healthStatus}</div>
            <div><b>Email:</b> {selectedDonor.email}</div>
            <div><b>Ngày đăng ký:</b> {selectedDonor.registrationDate?.slice(0, 10)}</div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DonorHistoryTable;
