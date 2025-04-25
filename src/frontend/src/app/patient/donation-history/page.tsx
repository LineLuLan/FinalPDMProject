"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

// Fake data for demo
type DonationHistory = {
  id: number;
  date: string;
  volume: string;
  location: string;
};

const mockHistory: DonationHistory[] = [
  { id: 1, date: "2025-03-10", volume: "350ml", location: "Bệnh viện 115" },
  { id: 2, date: "2024-12-22", volume: "350ml", location: "Bệnh viện Chợ Rẫy" },
];

export default function DonationHistoryPage() {
  const { data: session, status } = useSession();
  const router = useRouter();
  const [history, setHistory] = useState<DonationHistory[]>(mockHistory);

  useEffect(() => {
    if (status === "loading") return;
    if (!session) {
      router.replace("/login");
    } else if (session.user.role !== "PATIENT") {
      router.replace("/doctor/dashboard");
    }
  }, [session, status, router]);

  if (status === "loading" || !session || session.user.role !== "PATIENT") {
    return <div className="flex h-screen items-center justify-center">Loading...</div>;
  }

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Lịch sử hiến máu của bạn</h1>
      <table className="min-w-full bg-white border rounded-lg">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">Ngày hiến</th>
            <th className="py-2 px-4 border-b">Thể tích</th>
            <th className="py-2 px-4 border-b">Địa điểm</th>
          </tr>
        </thead>
        <tbody>
          {history.map((item) => (
            <tr key={item.id} className="text-center">
              <td className="py-2 px-4 border-b">{item.date}</td>
              <td className="py-2 px-4 border-b">{item.volume}</td>
              <td className="py-2 px-4 border-b">{item.location}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
