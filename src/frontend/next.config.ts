import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      // Không proxy /api/auth/* để NextAuth xử lý
      {
        source: '/api/auth/:path*',
        destination: '/api/auth/:path*', // Next.js sẽ tự xử lý NextAuth
      },
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*', // Proxy các route business sang backend
      },
    ];
  },
};

export default nextConfig;
