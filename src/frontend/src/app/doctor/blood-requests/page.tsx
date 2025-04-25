"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

// Fake data for demo
type BloodRequest = {
  id: number;
  patientName: string;
  bloodType: string;
  status: string;
  requestedAt: string;
};

const mockRequests: BloodRequest[] = [
  { id: 1, patientName: "Nguyen Van A", bloodType: "A+", status: "Pending", requestedAt: "2025-04-20" },
  { id: 2, patientName: "Tran Thi B", bloodType: "O-", status: "Approved", requestedAt: "2025-04-18" },
];

export default function BloodRequestsPage() {
  const { data: session, status } = useSession();
  const router = useRouter();
  const [requests, setRequests] = useState<BloodRequest[]>(mockRequests);

  useEffect(() => {
    if (status === "loading") return;
    if (!session) {
      router.replace("/login");
    } else if (session.user.role !== "DOCTOR") {
      router.replace("/patient/dashboard");
    }
  }, [session, status, router]);

  if (status === "loading" || !session || session.user.role !== "DOCTOR") {
    return <div className="flex h-screen items-center justify-center">Loading...</div>;
  }

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Quản lý yêu cầu máu</h1>
      <table className="min-w-full bg-white border rounded-lg">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">Tên bệnh nhân</th>
            <th className="py-2 px-4 border-b">Nhóm máu</th>
            <th className="py-2 px-4 border-b">Trạng thái</th>
            <th className="py-2 px-4 border-b">Ngày yêu cầu</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((req) => (
            <tr key={req.id} className="text-center">
              <td className="py-2 px-4 border-b">{req.patientName}</td>
              <td className="py-2 px-4 border-b">{req.bloodType}</td>
              <td className="py-2 px-4 border-b">{req.status}</td>
              <td className="py-2 px-4 border-b">{req.requestedAt}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
