"use client";
import React from "react";

export type BloodRequest = {
  request_id: number;
  blood_type: string;
  quantity: number;
  status: string;
  request_date: string;
  response_date?: string | null;
};

interface Props {
  requests: BloodRequest[];
}

export default function BloodRequestTable({ requests }: Props) {
  return (
    <div className="mt-8">
      <h2 className="text-lg font-semibold mb-2">Blood Request History</h2>
      <table className="min-w-full bg-white border rounded-lg">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">Blood Type</th>
            <th className="py-2 px-4 border-b">Quantity</th>
            <th className="py-2 px-4 border-b">Request Date</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Response Date</th>
          </tr>
        </thead>
        <tbody>
          {requests.length === 0 ? (
            <tr><td colSpan={5} className="text-center py-4 text-gray-400">No requests yet</td></tr>
          ) : (
            requests.map((req) => {
              // Accept both snake_case and camelCase from backend
              const requestId = req.request_id ?? req.requestId;
              const bloodType = req.blood_type ?? req.bloodType;
              const quantity = req.quantity;
              const status = req.status;
              const requestDate = req.request_date ?? req.requestDate;
              const responseDate = req.response_date ?? req.responseDate;
              return (
                <tr key={String(requestId)} className="text-center">
                  <td className="py-2 px-4 border-b">{bloodType}</td>
                  <td className="py-2 px-4 border-b">{quantity}</td>
                  <td className="py-2 px-4 border-b">{requestDate}</td>
                  <td className="py-2 px-4 border-b">
                    {status === "PENDING" && <span className="text-yellow-600 font-semibold">Pending</span>}
                    {status === "APPROVED" && <span className="text-green-600 font-semibold">Approved</span>}
                    {status === "REJECTED" && <span className="text-red-600 font-semibold">Rejected</span>}
                  </td>
                  <td className="py-2 px-4 border-b">{responseDate || "-"}</td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
}
