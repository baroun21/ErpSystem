import React from 'react';
import { useApp } from '../context/AppContext';

const Navbar = () => {
  const { user } = useApp();

  return (
    <nav className="bg-primary text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-4">
            <h1 className="text-2xl font-bold">🏢 ERP System</h1>
            <div className="hidden md:flex space-x-8 ml-10">
              <a href="/" className="hover:text-secondary transition">Dashboard</a>
              <a href="/hr" className="hover:text-secondary transition">HR</a>
              <a href="/finance" className="hover:text-secondary transition">Finance</a>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            {user ? (
              <>
                <span className="text-sm">👤 {user.name || 'User'}</span>
                <button className="bg-secondary px-4 py-2 rounded hover:bg-blue-600">
                  Logout
                </button>
              </>
            ) : (
              <button className="bg-secondary px-4 py-2 rounded hover:bg-blue-600">
                Login
              </button>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
