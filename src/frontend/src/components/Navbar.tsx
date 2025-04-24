'use client'

import Link from 'next/link'
import { useState } from 'react'
import { useSession, signOut } from 'next-auth/react'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'

export const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false)
  const { data: session } = useSession()

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
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
          <div className="hidden md:flex items-center space-x-4">
            <Link href="/" className="text-gray-700 hover:text-red-400 px-3 py-2">
              Home
            </Link>
            <Link href="/about" className="text-gray-700 hover:text-red-400 px-3 py-2">
              About Us
            </Link>
            <Link href="/find-blood" className="text-gray-700 hover:text-red-400 px-3 py-2">
              Find Blood
            </Link>
            {session ? (
              <>
                <Link href="/profile" className="flex items-center space-x-2 text-gray-700 hover:text-red-400 px-3 py-2">
                  <Avatar className="h-8 w-8">
                    <AvatarFallback>{getInitials(session.user.name)}</AvatarFallback>
                  </Avatar>
                  <span>Profile</span>
                </Link>
                <button
                  onClick={() => signOut()}
                  className="bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md"
                >
                  Sign Out
                </button>
              </>
            ) : (
              <>
                <Link href="/register" className="hover:bg-red-400 text-gray-800 border hover:border-red-700 hover:text-white px-4 py-2 rounded-md">
                  Register Now
                </Link>
                <Link href="/login" className="bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md">
                  Login
                </Link>
              </>
            )}
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
              <Link 
                href="/"
                className="block text-gray-700 hover:text-red-400 px-3 py-2"
                onClick={() => setIsOpen(false)}
              >
                Home
              </Link>
              <Link
                href="/about"
                className="block text-gray-700 hover:text-red-400 px-3 py-2"
                onClick={() => setIsOpen(false)}
              >
                About Us
              </Link>
              <Link
                href="/find-blood"
                className="block text-gray-700 hover:text-red-400 px-3 py-2"
                onClick={() => setIsOpen(false)}
              >
                Find Blood
              </Link>
              {session ? (
                <>
                  <Link
                    href="/profile"
                    className="block text-gray-700 hover:text-red-400 px-3 py-2"
                    onClick={() => setIsOpen(false)}
                  >
                    Profile
                  </Link>
                  <button
                    onClick={() => {
                      signOut()
                      setIsOpen(false)
                    }}
                    className="block w-full text-left bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md mx-3 mt-2"
                  >
                    Sign Out
                  </button>
                </>
              ) : (
                <>
                  <Link
                    href="/register"
                    className="block bg-red-400 text-white hover:bg-darkred-400 px-4 py-2 rounded-md mx-3"
                    onClick={() => setIsOpen(false)}
                  >
                    Register Now
                  </Link>
                  <Link
                    href="/login"
                    className="block bg-white text-red-400 border border-red-400 hover:bg-red-400 hover:text-white px-4 py-2 rounded-md mx-3 mt-2"
                    onClick={() => setIsOpen(false)}
                  >
                    Login
                  </Link>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  )
}