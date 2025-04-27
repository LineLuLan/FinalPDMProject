'use client'

import Link from 'next/link'
import { useState } from 'react'
import { useSession, signOut } from 'next-auth/react'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'

export const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { data: session } = useSession();

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase();
  };

  // Helper to render menu based on role and menu type (mobile/desktop)
  function renderMenu(mobile: boolean) {
    const linkClass = mobile ? "block text-gray-700 hover:text-red-400 px-3 py-2" : "text-gray-700 hover:text-red-400 px-3 py-2";
    const buttonClass = mobile
      ? "block w-full text-left bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md mx-3 mt-2"
      : "bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md";
    const regClass = mobile
      ? "block hover:bg-red-400 text-gray-800 border hover:border-red-700 hover:text-white px-4 py-2 rounded-md"
      : "hover:bg-red-400 text-gray-800 border hover:border-red-700 hover:text-white px-4 py-2 rounded-md";
    const loginClass = mobile
      ? "block bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md"
      : "bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md";
    const onClick = mobile ? () => setIsOpen(false) : undefined;
    if (!session?.user) {
      return (
        <>
          <Link href="/" className={linkClass} onClick={onClick}>Home</Link>
          <Link href="/about" className={linkClass} onClick={onClick}>About Us</Link>
          <Link href="/donate-blood" className={linkClass} onClick={onClick}>Đăng ký hiến máu</Link>
          <Link href="/register" className={regClass} onClick={onClick}>Register Now</Link>
          <Link href="/login" className={loginClass} onClick={onClick}>Login</Link>
        </>
      );
    }
    if (session.user.role === "DOCTOR") {
      return (
        <>
          <Link href="/" className={linkClass} onClick={onClick}>Home</Link>
          <Link href="/about" className={linkClass} onClick={onClick}>About Us</Link>
          <Link href="/doctor/dashboard" className={linkClass + " font-semibold"} onClick={onClick}>Doctor Dashboard</Link>
          <Link href="/profile" className={mobile ? linkClass : "flex items-center space-x-2 "+linkClass} onClick={onClick}>
            {!mobile && (
              <Avatar className="h-8 w-8">
                <AvatarFallback>{session.user.name ? getInitials(session.user.name) : '?'}</AvatarFallback>
              </Avatar>
            )}
            <span>Profile</span>
          </Link>
          <button
            onClick={() => { signOut(); if(mobile) setIsOpen(false); }}
            className={buttonClass}
          >
            Sign Out
          </button>
        </>
      );
    }
    if (session.user.role === "PATIENT") {
      return (
        <>
          <Link href="/" className={linkClass} onClick={onClick}>Home</Link>
          <Link href="/about" className={linkClass} onClick={onClick}>About Us</Link>
          <Link href="/patient/dashboard" className={linkClass + " font-semibold"} onClick={onClick}>Patient Dashboard</Link>
          <Link href="/profile" className={mobile ? linkClass : "flex items-center space-x-2 "+linkClass} onClick={onClick}>
            {!mobile && (
              <Avatar className="h-8 w-8">
                <AvatarFallback>{session.user.name ? getInitials(session.user.name) : '?'}</AvatarFallback>
              </Avatar>
            )}
            <span>Profile</span>
          </Link>
          <button
            onClick={() => { signOut(); if(mobile) setIsOpen(false); }}
            className={buttonClass}
          >
            Sign Out
          </button>
        </>
      );
    }
    return null;
  }

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link href="/" className="text-2xl font-bold text-red-400">
              Healthcare
            </Link>
          </div>

          {/* Desktop menu */}
          <div className="hidden md:flex items-center space-x-4 flex-wrap">
  {renderMenu(false)}
</div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsOpen(!isOpen)}
              className="text-gray-700 hover:text-red-400 focus:outline-none"
            >
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                {isOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>
        </div>

        {/* Mobile menu */}
{isOpen && (
  <div className="md:hidden">
    <div className="pt-2 pb-3 space-y-1">
      {renderMenu(true)}
    </div>
  </div>
)}
      </div>
    </nav>
  )
}