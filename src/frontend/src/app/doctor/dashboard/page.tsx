"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function DoctorDashboard() {
  const { data: session, status } = useSession();
  const router = useRouter();

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
      <h1 className="text-2xl font-bold mb-4">Doctor Dashboard</h1>
      {/* Nội dung dashboard cho bác sĩ sẽ đặt ở đây */}
      <p>Welcome, Dr. {session.user.name}!</p>
    </div>
  );
}
