  import NextAuth from "next-auth";
import { AuthOptions } from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";

export const authOptions: AuthOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        email: { label: "Email", type: "text" },
        password: { label: "Password", type: "password" }
      },
      async authorize(credentials) {
        if (!credentials) return null;
        try {
          const res = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              email: credentials.email,
              password: credentials.password,
            }),
          });
          if (!res.ok) return null;
          const user = await res.json();
          return {
            id: user.userId?.toString() || user.id?.toString() || user.email,
            name: user.fullName || user.name || user.email,
            email: user.email,
            image: null,
            address: user.address || '',
            phone: user.phone || '',
            role: user.role || '',
            dssn: user.dssn || '', 
            pssn: user.pssn || '', 
          };
        } catch (err) {
          return null;
        }
      }
    })
  ],
  pages: {
    signIn: '/login',
  },
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async session({ session, token }) {
      if (token && session.user) {
        session.user.id = token.sub!;
        session.user.address = token.address as string;
        session.user.phone = token.phone as string;
        session.user.role = token.role as string;
        session.user.dssn = token.dssn as string; 
        session.user.pssn = token.pssn as string; 
      }
      return session;
    },
    async jwt({ token, user }) {
      if (user) {
        token.address = (user as any).address;
        token.phone = (user as any).phone;
        token.role = (user as any).role;
        token.dssn = (user as any).dssn; 
        token.pssn = (user as any).pssn; 
      }
      return token;
    }
  }
};

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST };
