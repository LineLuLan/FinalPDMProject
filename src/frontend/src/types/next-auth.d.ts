import NextAuth from "next-auth";

declare module "next-auth" {
  interface User {
    id: string;
    name: string;
    email: string;
    image?: string | null;
    address: string;
    phone: string;
    role: 'DOCTOR' | 'PATIENT' | string;
  }

  interface Session {
    user: {
      id: string;
      name: string;
      email: string;
      image?: string | null;
      address: string;
      phone: string;
      role: 'DOCTOR' | 'PATIENT' | string;
    };
    // Có thể mở rộng thêm các trường khác nếu cần
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    sub: string;
    address?: string;
    phone?: string;
    role?: 'DOCTOR' | 'PATIENT' | string;
  }
}