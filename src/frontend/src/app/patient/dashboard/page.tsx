"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function PatientDashboard() {
  const { data: session, status } = useSession();
  const router = useRouter();

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
      <h1 className="text-2xl font-bold mb-4">Patient Dashboard</h1>
      {/* Nội dung dashboard cho bệnh nhân sẽ đặt ở đây */}
      <p>Welcome, {session.user.name}!</p>
    </div>
  );
}
