import React, { useEffect, useState } from 'react';
import { healthService } from '../services/api';
import { useApp } from '../context/AppContext';

const StatusCard = ({ service, status }) => {
  const statusColor = status ? 'bg-green-100 border-green-400 text-green-700' : 'bg-red-100 border-red-400 text-red-700';
  const statusText = status ? '✅ Healthy' : '❌ Offline';

  return (
    <div className={`p-4 border-2 rounded-lg ${statusColor}`}>
      <p className="font-semibold">{service}</p>
      <p className="text-sm mt-2">{statusText}</p>
    </div>
  );
};

const Dashboard = () => {
  const { setServiceStatus } = useApp();
  const [springStatus, setSpringStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkServices = async () => {
      try {
        const results = await healthService.checkAll();
        const springResult = results.find(r => r.service === 'Spring Boot');
        setSpringStatus(springResult?.healthy ?? false);
        setServiceStatus('springBoot', springResult?.healthy ?? false);
      } catch (error) {
        console.error('Failed to check services:', error);
      } finally {
        setLoading(false);
      }
    };

    checkServices();
    const interval = setInterval(checkServices, 30000); // Check every 30 seconds

    return () => clearInterval(interval);
  }, [setServiceStatus]);

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Header */}
        <div className="mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Dashboard</h1>
          <p className="text-gray-600">Welcome to your ERP System</p>
        </div>

        {/* Service Status */}
        <div className="bg-white rounded-lg shadow-lg p-8 mb-8">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Service Status</h2>
          {loading ? (
            <p className="text-gray-600">Checking services...</p>
          ) : (
            <div className="grid grid-cols-1 gap-6">
              <StatusCard 
                service="🔵 Spring Boot (Port 8081)" 
                status={springStatus}
              />
            </div>
          )}
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="text-blue-600 text-3xl font-bold mr-4">👥</div>
              <div>
                <p className="text-gray-600 text-sm">Employees</p>
                <p className="text-2xl font-bold">-</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="text-green-600 text-3xl font-bold mr-4">💰</div>
              <div>
                <p className="text-gray-600 text-sm">AR Balance</p>
                <p className="text-2xl font-bold">-</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="text-orange-600 text-3xl font-bold mr-4">📄</div>
              <div>
                <p className="text-gray-600 text-sm">Invoices</p>
                <p className="text-2xl font-bold">-</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="text-purple-600 text-3xl font-bold mr-4">📊</div>
              <div>
                <p className="text-gray-600 text-sm">Trial Balance</p>
                <p className="text-2xl font-bold">-</p>
              </div>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="bg-white rounded-lg shadow-lg p-8">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Recent Activity</h2>
          <p className="text-gray-600">No recent activity yet. Start by creating records in the HR or Finance modules.</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
