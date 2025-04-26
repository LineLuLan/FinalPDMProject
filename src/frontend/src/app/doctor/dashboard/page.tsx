"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import dynamic from "next/dynamic";

const DoctorDashboardTable = dynamic(() => import("./DoctorDashboardTable"), { ssr: false });

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
      <p>Welcome, Dr. {session.user.name}!</p>
      <div className="mt-6">
        {/* Bảng quản lý blood stock và blood requests */}
        {/* Ưu tiên lấy userId và dssn nếu có, nếu không thì lấy id và email */}
        {(session.user as any).userId && (session.user as any).dssn ? (
          <DoctorDashboardTable doctorId={Number((session.user as any).userId)} dssn={(session.user as any).dssn as string} />
        ) : (session.user as any).id && (session.user as any).email ? (
          <DoctorDashboardTable doctorId={Number((session.user as any).id)} dssn={(session.user as any).email as string} />
        ) : (
          <div className="text-red-500">Không tìm thấy thông tin doctorId hoặc dssn trong session. Vui lòng kiểm tra lại cấu hình đăng nhập.</div>
        )}
      </div>
    </div>
  );
}
