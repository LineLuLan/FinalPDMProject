"use client";

import { useSession } from "next-auth/react";
import React, { useEffect, useState } from 'react';
import axiosInstance from '@/lib/axios';
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import axios from "axios";

// Type definitions for strict typing
// You can move these to a separate types file if needed

type DoctorType = {
  userId?: number;
  dssn?: string;
  dname?: string;
  email?: string;
  specialization?: string;
  isActive?: boolean;
  createdAt?: string;
  password?: string;
  // ... thêm các trường khác nếu có
};
type UserType = {
  userId?: number;
  email?: string;
  role?: string;
  isActive?: boolean;
  createdAt?: string;
  password?: string;
  dssn?: string;
  pssn?: string;
  name?: string;
  phone?: string;
  address?: string;
  // ... thêm các trường khác nếu có
};

export default function ProfilePage() {
  // State cho các entity
  // State cho các entity (chỉ khai báo duy nhất ở đây)
  const { data: session, update } = useSession() as { data: { user: UserType } | null, update: any };
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<{
    name: string;
    email: string;
    phone: string;
    address: string;
    password: string;
  }>({
    name: session?.user?.name || "",
    email: session?.user?.email || "",
    phone: session?.user?.phone || "",
    address: session?.user?.address || "",
    password: ""
  });

  const [role, setRole] = useState<string | null>(null);
  // Các state cho thông tin doctor, patient, bloodBank v.v.
  const [doctor, setDoctor] = useState<DoctorType | null>(null);
  const [patient, setPatient] = useState<any>(null);
  const [bloodBank, setBloodBank] = useState<any>(null);
  const [patientsOfDoctor, setPatientsOfDoctor] = useState<any[]>([]);
  const [doctorApiError, setDoctorApiError] = useState<string | null>(null);

  // Debug log for doctor, patientsOfDoctor, bloodBank
  if (typeof window !== 'undefined') {
    console.log('doctor:', doctor);
    console.log('patientsOfDoctor:', patientsOfDoctor);
    console.log('bloodBank:', bloodBank);
    if(doctorApiError) console.error('doctorApiError:', doctorApiError);
  }

  useEffect(() => {
    // Log session user để kiểm tra userId
    if (typeof window !== 'undefined') {
      console.log('Session user object:', session?.user);
    }
    if (!session?.user) return;
    const userRole = session.user.role;
    setRole(userRole);

    if (userRole === 'DOCTOR') {
      const dssn = session.user?.dssn;
      if (!dssn) {
        setDoctorApiError("Không tìm thấy dssn trong session!");
        return;
      }
      fetchDoctorInfo(dssn);
    } else if (userRole === 'PATIENT') {
      const pssn = session.user?.pssn;
      if (!pssn) {
        console.error('Không tìm thấy pssn trong session!');
        return;
      }
      fetchPatientInfo(pssn);
    }
  }, [session]);

  // Hàm lấy thông tin doctor theo dssn
  const fetchDoctorInfo = (dssn: string) => {
    axiosInstance.get(`/api/doctors/dssn/${dssn}`)
      .then(res => {
        setDoctor(res.data);
        if (res.data.bloodBankId) {
          fetchBloodBankInfo(res.data.bloodBankId);
        }
        // Nếu cần lấy danh sách bệnh nhân của bác sĩ, gọi thêm API ở đây
        axiosInstance.get(`/api/patients`).then(pRes => {
          const patients = pRes.data.filter((p: any) => p.assignedDoctorId === res.data.dssn);
          setPatientsOfDoctor(patients);
        }).catch(e => {
          console.error('Error fetching patients list:', e);
        });
      })
      .catch(e => {
        setDoctor(null);
        setDoctorApiError(e?.message || 'API error');
        console.error('Error fetching doctor info:', e);
        if (e.response) {
          console.error('Doctor API error response:', e.response.data);
        }
      });
  };

  // Hàm lấy thông tin patient theo pssn
  const fetchPatientInfo = (pssn: string) => {
    axiosInstance.get(`/api/patients/${pssn}`)
      .then(res => {
        setPatient(res.data);
        if (res.data.assignedDoctorId) {
          fetchDoctorInfo(res.data.assignedDoctorId);
        }
      })
      .catch(e => {
        console.error('Error fetching patient info:', e);
      });
  };

  // Hàm lấy thông tin blood bank
  const fetchBloodBankInfo = (bloodBankId: string) => {
    axiosInstance.get(`/api/bloodBanks/${bloodBankId}`)
      .then(res => setBloodBank(res.data))
      .catch(e => {
        console.error('Error fetching blood bank:', e);
      });
  };

  if (!session) {
    return (
      <div className="flex h-screen items-center justify-center">
        <p className="text-lg">Please sign in to view your profile.</p>
      </div>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await update({ ...session, user: { ...session.user, ...formData } });
    setIsEditing(false);
  };

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase();
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mx-auto max-w-2xl">
        <div className="rounded-lg bg-white p-6 shadow-md">
          <div className="mb-6 flex items-center space-x-4">
            <Avatar className="h-20 w-20">
              <AvatarImage src={session.user.image || ""} />
              <AvatarFallback>{getInitials(role === 'PATIENT' && patient ? patient.name : session.user.name)}</AvatarFallback>
            </Avatar>
            <div>
              {role === 'PATIENT' && patient ? (
                <>
                  <h1 className="text-2xl font-bold">{patient.name}</h1>
                  <p className="text-gray-600">{patient.email}</p>
                  <div className="text-gray-600">{patient.phone}</div>
                  <div className="text-gray-600">{patient.address}</div>
                  <span className="text-sm text-blue-500 font-semibold">{role}</span>
                </>
              ) : (
                <>
                  <h1 className="text-2xl font-bold">{doctor ? doctor.dname : session.user.name}</h1>
                  <p className="text-gray-600">{doctor ? doctor.email : session.user.email}</p>
                  <span className="text-sm text-blue-500 font-semibold">{role}</span>
                </>
              )}
            </div>
          </div>

          {/* Doctor-specific info block */}
          {role === "DOCTOR" && doctorApiError && (
            <div className="text-red-600 font-bold mb-4">Lỗi lấy thông tin doctor: {doctorApiError}</div>
          )}
          {role === "DOCTOR" && !doctor && !doctorApiError && (
            <div className="text-red-600 font-bold mb-4">Không tìm thấy thông tin doctor hoặc chưa load xong!</div>
          )}
          {role === "DOCTOR" && doctor && (
            <div className="space-y-2 mb-4">
              {/* Edit button for doctor */}
              <Button className="mb-4" onClick={() => setIsEditing(true)}>Edit Profile</Button>
              <Dialog open={isEditing} onOpenChange={setIsEditing}>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Edit Profile</DialogTitle>
                  </DialogHeader>
                  <form onSubmit={async (e) => {
                    e.preventDefault();
                    try {
                      // Update Doctor (name, email, specialization)
                      const doctorId = doctor?.userId ?? session?.user?.userId;
                      if (!doctorId || typeof doctorId !== 'number') {
                        alert('Không tìm thấy doctorId hợp lệ để cập nhật doctor!');
                        return;
                      }
                      await axiosInstance.put(`/api/doctors/${doctorId}`, {
                        dname: formData.name || doctor.dname,
                        email: formData.email || doctor.email,
                        specialization: formData.specialization || doctor.specialization,
                        bloodBankId: doctor.bloodBankId
                      });
                      // Prepare user update object
                      const userId = (doctor?.userId ?? session?.user?.userId);
                      if (!userId || typeof userId !== 'number') {
                        alert('Không tìm thấy userId hợp lệ để cập nhật user!');
                        return;
                      }
                      const userUpdate: any = {
                        userId,
                        email: formData.email || doctor?.email || session?.user?.email,
                        role: doctor?.role || session?.user?.role,
                        isActive: doctor?.isActive ?? session?.user?.isActive ?? true,
                        createdAt: doctor?.createdAt || session?.user?.createdAt || new Date().toISOString(),
                      };
                      if (formData.password && formData.password.trim() !== "") {
                        userUpdate.password = formData.password;
                      }
                      console.log('userUpdate gửi lên:', userUpdate);
                      await axiosInstance.put(`/api/users/${userId}`, userUpdate);
                      // Reload doctor info
                      fetchDoctorInfo(doctor.dssn);
                      setIsEditing(false);
                    } catch (error) {
                      console.error('Update profile error:', error);
                      if (error.response) {
                        console.error('Backend error response:', error.response.data);
                        alert('Update failed: ' + JSON.stringify(error.response.data));
                      } else {
                        alert('Update failed: ' + error.message);
                      }
                    }
                  }} className="space-y-4">
                    <div>
                      <Label htmlFor="name">Name</Label>
                      <Input
                        id="name"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      />
                    </div>
                    <div>
                      <Label htmlFor="email">Email</Label>
                      <Input
                        id="email"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                      />
                    </div>
                    <div>
                      <Label htmlFor="specialization">Specialization</Label>
                      <Input
                        id="specialization"
                        value={formData.specialization || ''}
                        onChange={(e) => setFormData({ ...formData, specialization: e.target.value })}
                      />
                    </div>
                    <div>
                      <Label htmlFor="password">Password</Label>
                      <Input
                        id="password"
                        type="password"
                        value={formData.password || ''}
                        onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                      />
                    </div>
                    <Button type="submit">Save Changes</Button>
                  </form>
                </DialogContent>
              </Dialog>
              <div>
                <span className="font-semibold">Dssn:</span> <span>{doctor.dssn}</span>
              </div>
              <div>
                <span className="font-semibold">Specialization:</span> <span>{doctor.specialization || "-"}</span>
              </div>
              <div>
                <span className="font-semibold">Blood Bank:</span> <span>{bloodBank ? bloodBank.name : ""}</span>
              </div>
              <div>
                <span className="font-semibold">Patients:</span>
                {patientsOfDoctor.length === 0 ? (
                  <span> None</span>
                ) : (
                  <ul className="list-disc ml-6">
                    {patientsOfDoctor.map((p) => (
                      <li key={p.pssn}>{p.name}</li>
                    ))}
                  </ul>
                )}
              </div>
            </div>
          )}

          <div className="space-y-4">
            {/* Only show edit form and patient info if not doctor */}
            {role !== "DOCTOR" && (
              <>
            <Dialog open={isEditing} onOpenChange={setIsEditing}>
              <DialogTrigger asChild>
                <Button className="mb-4">Edit Profile</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Edit Profile</DialogTitle>
                </DialogHeader>
                <form onSubmit={handleSubmit} className="space-y-4">
                  <div>
                    <Label htmlFor="name">Name</Label>
                    <Input
                      id="name"
                      value={formData.name}
                      onChange={(e) =>
                        setFormData({ ...formData, name: e.target.value })
                      }
                    />
                  </div>
                  <div>
                    <Label htmlFor="phone">Phone</Label>
                    <Input
                      id="phone"
                      value={formData.phone}
                      onChange={(e) =>
                        setFormData({ ...formData, phone: e.target.value })
                      }
                    />
                  </div>
                  <div>
                    <Label htmlFor="address">Address</Label>
                    <Input
                      id="address"
                      value={formData.address}
                      onChange={(e) =>
                        setFormData({ ...formData, address: e.target.value })
                      }
                    />
                  </div>
                  <Button type="submit">Save Changes</Button>
                </form>
              </DialogContent>
            </Dialog>

            <div className="space-y-2">

              {/* Patient-specific fields */}
              {role === "PATIENT" && patient && (
  <>
    <div>
      <span className="font-semibold">PSSN:</span> <span>{patient.pssn}</span>
    </div>
    <div>
      <span className="font-semibold">Blood Type:</span> <span>{patient.bloodType}</span>
    </div>
    <div>
      <span className="font-semibold">Gender:</span> <span>{patient.gender}</span>
    </div>
    <div>
      <span className="font-semibold">Age:</span> <span>{patient.age}</span>
    </div>
    <div>
      <span className="font-semibold">Assigned Doctor:</span> <span>{doctor ? doctor.dname : ""}</span>
    </div>
    <div>
      <span className="font-semibold">Blood Bank:</span> <span>{bloodBank ? bloodBank.name : ""}</span>
    </div>
  </>
)}
            </div>
            </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}