import { NextRequest, NextResponse } from "next/server";

const BACKEND_URL = process.env.BACKEND_URL || "http://localhost:8080";

// GET /api/blood-requests?pssn=...
export async function GET(req: NextRequest) {
  const { searchParams } = new URL(req.url);
  const pssn = searchParams.get("pssn");
  if (!pssn) {
    return NextResponse.json([], { status: 200 });
  }
  // Proxy to backend (correct endpoint for filtering by user)
  const backendRes = await fetch(`${BACKEND_URL}/api/bloodRequests/pssn/${encodeURIComponent(pssn)}`);
  const data = await backendRes.json();
  return NextResponse.json(data);
}

// POST /api/blood-requests
export async function POST(req: NextRequest) {
  const body = await req.json();
  const backendRes = await fetch(`${BACKEND_URL}/api/bloodRequests`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await backendRes.json();
  return NextResponse.json(data, { status: backendRes.status });
}
