import React, { createContext, useContext, useState, ReactNode } from "react";

export type UserRole = "DOCTOR" | "PATIENT" | "DONOR" | null;

export interface UserContextType {
  userId: string | null;
  email: string | null;
  role: UserRole;
  setUser: (user: { userId: string | null; email: string | null; role: UserRole }) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<{ userId: string | null; email: string | null; role: UserRole }>({
    userId: null,
    email: null,
    role: null,
  });

  return (
    <UserContext.Provider value={{ ...user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within a UserProvider");
  return context;
};
