"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";

// Fake data for demo
type BloodRequest = {
  id: number;
  bloodType: string;
  status: string;
  requestedAt: string;
};

const mockRequests: BloodRequest[] = [
  { id: 1, bloodType: "A+", status: "Pending", requestedAt: "2025-04-20" },
  { id: 2, bloodType: "O-", status: "Approved", requestedAt: "2025-04-18" },
];

export default function PatientBloodRequestsPage() {
  const { data: session, status } = useSession();
  const router = useRouter();
  const [requests, setRequests] = useState<BloodRequest[]>(mockRequests);
  const [bloodType, setBloodType] = useState("");
  const [loading, setLoading] = useState(false);

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

  const handleRequest = (e: React.FormEvent) => {
    e.preventDefault();
    if (!bloodType) return;
    setLoading(true);
    // Fake API call
    setTimeout(() => {
      setRequests([
        {
          id: requests.length + 1,
          bloodType,
          status: "Pending",
          requestedAt: new Date().toISOString().slice(0, 10),
        },
        ...requests,
      ]);
      setBloodType("");
      setLoading(false);
    }, 800);
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Blood requirement</h1>
      <form onSubmit={handleRequest} className="mb-8 flex gap-4 items-end">
        <div>
          <label className="block mb-1 font-medium">Blood type required</label>
          <select
            className="border rounded px-3 py-2 w-40"
            value={bloodType}
            onChange={e => setBloodType(e.target.value)}
            required
          >
            <option value="">Select blood type</option>
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
        <Button type="submit" disabled={loading || !bloodType}>
          {loading ? "Đang gửi..." : "Gửi yêu cầu"}
        </Button>
      </form>
      <h2 className="text-lg font-semibold mb-2">Blood request history</h2>
      <table className="min-w-full bg-white border rounded-lg">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">Blood type</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Date request</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((req) => (
            <tr key={req.id} className="text-center">
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
