import { useState } from 'react'
import axios from 'axios'

export default function Profile() {
  const [activeTab, setActiveTab] = useState('profile')
  const [formData, setFormData] = useState({
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    phone: '+1 (555) 000-0000',
    companyName: 'Acme Corp',
    department: 'Finance',
    role: 'Admin'
  })

  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  })

  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState({ type: '', text: '' })

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handlePasswordChange = (e) => {
    const { name, value } = e.target
    setPasswordData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSaveProfile = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      // API call would go here
      setMessage({ type: 'success', text: 'Profile updated successfully!' })
    } catch (err) {
      setMessage({ type: 'error', text: 'Failed to update profile' })
    } finally {
      setLoading(false)
    }
  }

  const handleChangePassword = async (e) => {
    e.preventDefault()
    
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setMessage({ type: 'error', text: 'Passwords do not match' })
      return
    }

    setLoading(true)
    try {
      // API call would go here
      setMessage({ type: 'success', text: 'Password changed successfully!' })
      setPasswordData({
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      })
    } catch (err) {
      setMessage({ type: 'error', text: 'Failed to change password' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-12 max-w-4xl">
      <div className="mb-12">
        <h1 className="text-5xl font-black text-gray-900 mb-3">Account Settings</h1>
        <p className="text-lg text-gray-600">Manage your profile, security, and subscription</p>
      </div>

      {/* Message */}
      {message.text && (
        <div className={`p-5 rounded-xl ${message.type === 'success' ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'}`}>
          <p className={`font-bold ${message.type === 'success' ? 'text-green-700' : 'text-red-700'}`}>
            {message.type === 'success' ? '✓ ' : '✕ '}{message.text}
          </p>
        </div>
      )}

      {/* Tabs */}
      <div className="flex gap-1 border-b-2 border-gray-200 overflow-x-auto">
        <button
          onClick={() => setActiveTab('profile')}
          className={`px-6 py-4 font-bold border-b-2 transition whitespace-nowrap ${
            activeTab === 'profile'
              ? 'border-teal-600 text-teal-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          }`}
        >
          👤 Profile
        </button>
        <button
          onClick={() => setActiveTab('security')}
          className={`px-6 py-4 font-bold border-b-2 transition whitespace-nowrap ${
            activeTab === 'security'
              ? 'border-teal-600 text-teal-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          }`}
        >
          🔒 Security
        </button>
        <button
          onClick={() => setActiveTab('preferences')}
          className={`px-6 py-4 font-bold border-b-2 transition whitespace-nowrap ${
            activeTab === 'preferences'
              ? 'border-teal-600 text-teal-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          }`}
        >
          ⚙️ Preferences
        </button>
        <button
          onClick={() => setActiveTab('subscription')}
          className={`px-6 py-4 font-bold border-b-2 transition whitespace-nowrap ${
            activeTab === 'subscription'
              ? 'border-teal-600 text-teal-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          }`}
        >
          💳 Subscription
        </button>
      </div>

      {/* Profile Tab */}
      {activeTab === 'profile' && (
        <form onSubmit={handleSaveProfile} className="bg-white p-10 rounded-2xl border border-gray-200 shadow-lg max-w-2xl">
          <h2 className="text-2xl font-black text-gray-900 mb-10">Edit Profile Information</h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
            <div>
              <label className="block text-sm font-bold text-gray-900 mb-2">First Name</label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleInputChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
            </div>
            <div>
              <label className="block text-sm font-bold text-gray-900 mb-2">Last Name</label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleInputChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
            </div>
          </div>

          <div className="mb-8">
            <label className="block text-sm font-bold text-gray-900 mb-2">Email Address</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
            />
          </div>

          <div className="mb-8">
            <label className="block text-sm font-bold text-gray-900 mb-2">Phone Number</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-10">
            <div>
              <label className="block text-sm font-bold text-gray-900 mb-2">Company Name</label>
              <input
                type="text"
                name="companyName"
                value={formData.companyName}
                onChange={handleInputChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
            </div>
            <div>
              <label className="block text-sm font-bold text-gray-900 mb-2">Department</label>
              <input
                type="text"
                name="department"
                value={formData.department}
                onChange={handleInputChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900 bg-gray-50 cursor-not-allowed"
                disabled
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="px-8 py-3 bg-gradient-to-r from-teal-600 to-blue-600 text-white font-bold rounded-xl hover:shadow-lg hover:scale-105 disabled:opacity-50 transition duration-300"
          >
            {loading ? 'Saving...' : 'Save Changes'}
          </button>
        </form>
      )}

      {/* Security Tab */}
      {activeTab === 'security' && (
        <div className="space-y-6">
          <form onSubmit={handleChangePassword} className="bg-white p-10 rounded-2xl border border-gray-200 shadow-lg max-w-2xl">
            <h2 className="text-2xl font-black text-gray-900 mb-8">Change Password</h2>

            <div className="mb-5">
              <label className="block text-sm font-bold text-gray-900 mb-2">Current Password</label>
              <input
                type="password"
                name="currentPassword"
                value={passwordData.currentPassword}
                onChange={handlePasswordChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
            </div>

            <div className="mb-5">
              <label className="block text-sm font-bold text-gray-900 mb-2">New Password</label>
              <input
                type="password"
                name="newPassword"
                value={passwordData.newPassword}
                onChange={handlePasswordChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
              <p className="text-xs text-gray-600 mt-2 font-medium">Minimum 8 characters, mix of upper/lowercase, numbers, and symbols</p>
            </div>

            <div className="mb-8">
              <label className="block text-sm font-bold text-gray-900 mb-2">Confirm New Password</label>
              <input
                type="password"
                name="confirmPassword"
                value={passwordData.confirmPassword}
                onChange={handlePasswordChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="px-8 py-3 bg-gradient-to-r from-teal-600 to-blue-600 text-white font-bold rounded-xl hover:shadow-lg hover:scale-105 disabled:opacity-50 transition duration-300"
            >
              {loading ? 'Updating...' : 'Update Password'}
            </button>
          </form>

          {/* Two-Factor Authentication */}
          <div className="bg-gradient-to-br from-teal-50 to-blue-50 p-10 rounded-2xl border border-teal-200 shadow-lg max-w-2xl">
            <h2 className="text-2xl font-black text-gray-900 mb-3">🔐 Two-Factor Authentication</h2>
            <p className="text-gray-700 mb-6 font-medium">Protect your account with an extra layer of security. Enable 2FA using an authenticator app.</p>
            <button className="px-8 py-3 border-2 border-teal-600 text-teal-600 font-bold rounded-xl hover:bg-teal-50 transition">
              Enable 2FA
            </button>
          </div>
        </div>
      )}

      {/* Preferences Tab */}
      {activeTab === 'preferences' && (
        <div className="bg-white p-10 rounded-2xl border border-gray-200 shadow-lg max-w-2xl">
          <h2 className="text-2xl font-black text-gray-900 mb-10">Notification Preferences</h2>

          <div className="space-y-6">
            <div className="flex items-center justify-between pb-6 border-b border-gray-200">
              <div>
                <p className="font-bold text-gray-900">📧 Email Notifications</p>
                <p className="text-sm text-gray-600 mt-1">Receive updates about your account and activity</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" defaultChecked className="sr-only peer" />
                <div className="w-12 h-7 bg-gray-300 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-teal-500 rounded-full peer peer-checked:after:translate-x-5 peer-checked:after:border-white after:content-[''] after:absolute after:top-[3px] after:left-[3px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-teal-600"></div>
              </label>
            </div>

            <div className="flex items-center justify-between pb-6 border-b border-gray-200">
              <div>
                <p className="font-bold text-gray-900">🔔 Login Alerts</p>
                <p className="text-sm text-gray-600 mt-1">Get notified when someone signs in from a new device</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" defaultChecked className="sr-only peer" />
                <div className="w-12 h-7 bg-gray-300 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-teal-500 rounded-full peer peer-checked:after:translate-x-5 peer-checked:after:border-white after:content-[''] after:absolute after:top-[3px] after:left-[3px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-teal-600"></div>
              </label>
            </div>

            <div className="flex items-center justify-between">
              <div>
                <p className="font-bold text-gray-900">📰 Product Updates</p>
                <p className="text-sm text-gray-600 mt-1">Hear about new features, improvements, and special offers</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" className="sr-only peer" />
                <div className="w-12 h-7 bg-gray-300 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-teal-500 rounded-full peer peer-checked:after:translate-x-5 peer-checked:after:border-white after:content-[''] after:absolute after:top-[3px] after:left-[3px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-teal-600"></div>
              </label>
            </div>
          </div>
        </div>
      )}

      {/* Subscription Tab */}
      {activeTab === 'subscription' && (
        <div className="bg-white p-10 rounded-2xl border border-gray-200 shadow-lg max-w-2xl">
          <h2 className="text-2xl font-black text-gray-900 mb-10">Subscription Details</h2>

          <div className="space-y-8 mb-10">
            <div className="p-8 bg-gradient-to-br from-teal-50 to-blue-50 rounded-xl border border-teal-200">
              <p className="text-xs font-bold text-gray-600 uppercase tracking-wide">Current Plan</p>
              <p className="text-4xl font-black text-gray-900 mt-3">Professional</p>
              <p className="text-sm text-gray-600 mt-3 font-medium">✓ Full access to all modules</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="p-8 bg-gray-50 rounded-xl border border-gray-200">
                <p className="text-xs font-bold text-gray-600 uppercase tracking-wide">Billing Cycle</p>
                <p className="text-2xl font-black text-gray-900 mt-3">Monthly</p>
                <p className="text-lg font-bold text-teal-600 mt-2">$299/mo</p>
              </div>

              <div className="p-8 bg-gray-50 rounded-xl border border-gray-200">
                <p className="text-xs font-bold text-gray-600 uppercase tracking-wide">Next Billing Date</p>
                <p className="text-2xl font-black text-gray-900 mt-3">March 21, 2026</p>
              </div>
            </div>
          </div>

          <div className="flex gap-3 mb-8 pb-8 border-b border-gray-200">
            <button className="flex-1 px-6 py-3 border-2 border-teal-600 text-teal-600 font-bold rounded-xl hover:bg-teal-50 transition">
              📈 Upgrade Plan
            </button>
            <button className="flex-1 px-6 py-3 border-2 border-gray-300 text-gray-700 font-bold rounded-xl hover:bg-gray-50 transition">
              Cancel Subscription
            </button>
          </div>

          {/* Billing History */}
          <div>
            <h3 className="font-black text-gray-900 mb-5 flex items-center gap-2">
              💰 Billing History
            </h3>
            <div className="space-y-3">
              <div className="flex justify-between items-center p-4 bg-gray-50 rounded-xl border border-gray-200 hover:border-teal-300 transition">
                <div>
                  <p className="font-bold text-gray-900">February 2026</p>
                  <p className="text-sm text-gray-600 font-medium">Professional Plan</p>
                </div>
                <div className="text-right">
                  <p className="font-black text-gray-900">$299.00</p>
                  <button className="text-sm text-teal-600 hover:text-teal-700 font-bold">View Invoice →</button>
                </div>
              </div>
              <div className="flex justify-between items-center p-4 bg-gray-50 rounded-xl border border-gray-200 hover:border-teal-300 transition">
                <div>
                  <p className="font-bold text-gray-900">January 2026</p>
                  <p className="text-sm text-gray-600 font-medium">Professional Plan</p>
                </div>
                <div className="text-right">
                  <p className="font-black text-gray-900">$299.00</p>
                  <button className="text-sm text-teal-600 hover:text-teal-700 font-bold">View Invoice →</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
