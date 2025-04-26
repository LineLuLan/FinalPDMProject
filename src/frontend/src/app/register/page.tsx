'use client';

import { useState, useEffect } from 'react';
import useBloodBanks from './useBloodBanks';
import axios from '@/lib/axios';
import PssnSuggest from './PssnSuggest';

export default function Register() {
  const bloodBanks = useBloodBanks();
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    bloodType: '',
    gender: '',
    dob: '',
    phone: '',
    address: '',
    role: '',
    pssn: '',
    specialization: '',
    bloodBankId: '',
    assignedDoctorId: ''
  });


  const [doctorOptions, setDoctorOptions] = useState<{ dssn: string; dname: string; bloodBankId?: number; bloodBankName?: string }[]>([]);

  useEffect(() => {
    if (formData.role === 'PATIENT') {
      fetch('/api/doctors')
        .then(res => res.json())
        .then(async (doctors) => {
          // Lấy thêm tên blood bank nếu có bloodBankId
          let banks: any[] = [];
          try {
            const banksRes = await fetch('/api/bloodBanks');
            banks = await banksRes.json();
          } catch {}
          setDoctorOptions(doctors.map((doc: any) => ({
            dssn: doc.dssn,
            dname: doc.dname,
            bloodBankId: doc.bloodBankId,
            bloodBankName: banks.find(b => b.bid === doc.bloodBankId)?.name || ''
          })));
        });
    }
  }, [formData.role]);


  // Hàm tính tuổi dùng ở nhiều nơi
  function calculateAge(dob: string): number {
    if (!dob) return 0;
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const [message, setMessage] = useState<string|null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    if (!formData.role) {
      setMessage('Vui lòng chọn vai trò tài khoản!');
      return;
    }
    setLoading(true);
    try {
      let payload: any = {
        user: {
          email: formData.email,
          password: formData.password,
          role: formData.role
        }
      };
      if (formData.role === 'DOCTOR') {
        payload.doctor = {
          dname: formData.fullName,
          gender: formData.gender,
          bloodType: formData.bloodType,
          specialization: formData.specialization,
          email: formData.email,
          bloodBankId: formData.bloodBankId ? Number(formData.bloodBankId) : undefined,
          dssn: formData.pssn 
        };
      } else if (formData.role === 'PATIENT') {
        const age = calculateAge(formData.dob);
        payload.patient = {
          pssn: formData.pssn,
          name: formData.fullName,
          bloodType: formData.bloodType,
          age,
          gender: formData.gender,
          phone: formData.phone,
          email: formData.email,
          assignedDoctorId: formData.assignedDoctorId || undefined
        };
      }
      const endpoint = formData.role === 'PATIENT' ? '/api/registration/registerPatientUser' : '/api/registration/registerUser';
      const res = await axios.post(endpoint, payload);
      setMessage('Đăng ký thành công! Vui lòng đăng nhập.');
      setFormData({
        fullName: '',
        email: '',
        password: '',
        bloodType: '',
        gender: '',
        dob: '',
        phone: '',
        address: '',
        role: '',
        pssn: '',
        specialization: '',
        bloodBankId: '',
        assignedDoctorId: ''
      });
    } catch (err: any) {
      if (err?.response?.data) {
        setMessage(typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data));
      } else {
        setMessage('Đăng ký thất bại!');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="max-w-2xl mx-auto">
          {/* Header */}
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Đăng ký tài khoản (Chỉ dành cho Bác sĩ &amp; Bệnh nhân)</h1>
            <p className="text-xl text-gray-600">
              Chỉ dành cho đối tượng Bác sĩ và Bệnh nhân muốn sử dụng hệ thống quản lý máu. Nếu bạn muốn hiến máu, vui lòng dùng mục "Đăng ký hiến máu" trên thanh menu.
            </p>
          </div>
          <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow-lg">
            <div className="mb-8">
              <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-2">
                Đăng ký tài khoản dưới vai trò
              </label>
              <select
                id="role"
                name="role"
                value={formData.role}
                onChange={handleChange}
                className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                required
              >
                <option value="">Chọn vai trò</option>
                <option value="PATIENT">Bệnh nhân</option>
                <option value="DOCTOR">Bác sĩ</option>
              </select>
            </div>
            {formData.role === 'PATIENT' && (
              <>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Thông tin bệnh nhân</h2>
                <div className="grid grid-cols-1 gap-6">
                  <div>
                    <label htmlFor="fullName" className="block text-sm font-medium text-gray-700 mb-2">
                      Full Name
                    </label>
                    <input
                      type="text"
                      id="fullName"
                      name="fullName"
                      value={formData.fullName}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="gender" className="block text-sm font-medium text-gray-700 mb-2">
                      Gender
                    </label>
                    <select
                      id="gender"
                      name="gender"
                      value={formData.gender}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    >
                      <option value="">Select Gender</option>
                      <option value="male">Male</option>
                      <option value="female">Female</option>
                      <option value="other">Other</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="dob" className="block text-sm font-medium text-gray-700 mb-2 mt-4">
                      Ngày sinh
                    </label>
                    <input
                      type="date"
                      id="dob"
                      name="dob"
                      value={formData.dob}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="age" className="block text-sm font-medium text-gray-700 mb-2 mt-4">
                      Tuổi
                    </label>
                    <input
                      type="number"
                      id="age"
                      name="age"
                      value={formData.dob ? calculateAge(formData.dob) : ''}
                      readOnly
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                    />
                  </div>
                  <div>
                    <PssnSuggest
                      fullName={formData.fullName}
                      dob={formData.dob}
                      value={formData.pssn}
                      onChange={(val) => setFormData((prev) => ({ ...prev, pssn: val }))}
                    />
                  </div>
                  <div>
                    <label htmlFor="bloodType" className="block text-sm font-medium text-gray-700 mb-2 mt-4">
                      Blood Type
                    </label>
                    <select
                      id="bloodType"
                      name="bloodType"
                      value={formData.bloodType}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    >
                      <option value="">Select Blood Type</option>
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
                  <div>
                    <label htmlFor="assignedDoctorId" className="block text-sm font-medium text-gray-700 mb-2 mt-4">
                      Bác sĩ chỉ định
                    </label>
                    <select
                      id="assignedDoctorId"
                      name="assignedDoctorId"
                      value={formData.assignedDoctorId}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                    >
                      <option value="">Chọn bác sĩ</option>
                      {doctorOptions.map(doc => (
                        <option key={doc.dssn} value={doc.dssn}>
                          {doc.dname} {doc.bloodBankName ? `- ${doc.bloodBankName}` : ''}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </>
            )}
            {formData.role === 'DOCTOR' && (
              <>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Thông tin bác sĩ</h2>
                <div className="grid grid-cols-1 gap-6">
                  <div>
                    <label htmlFor="pssn" className="block text-sm font-medium text-gray-700 mb-2">
                      DSSN
                    </label>
                    <input
                      type="text"
                      id="pssn"
                      name="pssn"
                      value={formData.pssn}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="fullName" className="block text-sm font-medium text-gray-700 mb-2">
                      Name
                    </label>
                    <input
                      type="text"
                      id="fullName"
                      name="fullName"
                      value={formData.fullName}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="specialization" className="block text-sm font-medium text-gray-700 mb-2">
                      Specialization
                    </label>
                    <input
                      type="text"
                      id="specialization"
                      name="specialization"
                      value={formData.specialization}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                      Email
                    </label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="bloodBankId" className="block text-sm font-medium text-gray-700 mb-2">
                      Blood Bank
                    </label>
                    <select
                      id="bloodBankId"
                      name="bloodBankId"
                      value={formData.bloodBankId}
                      onChange={handleChange}
                      className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                      required
                    >
                      <option value="">Chọn blood bank</option>
                      {bloodBanks.map((b) => (
                        <option key={b.bid} value={b.bid}>{b.bid} - {b.name}</option>
                      ))}
                    </select>
                  </div>
                </div>
              </>
            )}


            {/* Account Security */}
            <div className="mb-8">
              <h2 className="text-xl font-semibold text-gray-900 mb-6">Account Security</h2>
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                  Password
                </label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                  required
                  minLength={8}
                />
                <p className="mt-2 text-sm text-gray-500">
                  Password must be at least 8 characters long
                </p>
              </div>
            </div>

            <button
              type="submit"
              className="w-full bg-red-600 text-white py-3 px-6 rounded-md hover:bg-red-700 transition-colors font-semibold"
            >
              Create Account
            </button>

            <p className="mt-4 text-center text-gray-600">
              Already have an account?{' '}
              <a href="/login" className="text-red-600 hover:text-red-700">
                Login here
              </a>
            </p>
            {message && (
              <div className={`mt-4 text-center font-semibold ${message.includes('thành công') ? 'text-green-600' : 'text-red-600'}`}>{message}</div>
            )}
          </form>
        </div>
      </div>
    </div>
  );
}