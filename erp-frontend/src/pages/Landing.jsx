import { Link } from 'react-router-dom'
import { useState, useCallback } from 'react'

export default function Landing() {
  const [isHovered, setIsHovered] = useState(null)
  
  const handleMouseEnter = useCallback((idx) => {
    setIsHovered(idx)
  }, [])
  
  const handleMouseLeave = useCallback(() => {
    setIsHovered(null)
  }, [])

  return (
    <div className="w-full bg-white">
      {/* Navigation */}
      <nav className="fixed w-full top-0 z-50 backdrop-blur-md bg-white/80 border-b border-gray-200/50" aria-label="Main navigation">
        <div className="w-full h-16 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <div className="flex items-center gap-10">
            <h1 className="text-2xl font-black bg-gradient-to-r from-teal-600 to-blue-600 bg-clip-text text-transparent">
              NEXORA
            </h1>
            <div className="hidden md:flex gap-8">
              <a href="#features" className="text-sm text-gray-600 hover:text-gray-900 font-medium transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 rounded px-2 py-1">
                Features
              </a>
              <a href="#pricing" className="text-sm text-gray-600 hover:text-gray-900 font-medium transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 rounded px-2 py-1">
                Pricing
              </a>
            </div>
          </div>
          <div className="flex gap-3">
            <Link to="/login" className="px-4 py-2 text-sm font-semibold text-gray-700 hover:text-gray-900 transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 rounded">
              Sign In
            </Link>
            <Link to="/register" className="px-5 py-2 bg-teal-600 text-white text-sm font-semibold rounded-lg hover:bg-teal-700 shadow-lg hover:shadow-xl transition focus:outline-none focus:ring-2 focus:ring-teal-700 focus:ring-offset-2">
              Get Started
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="w-full min-h-screen pt-32 pb-64 px-4 sm:px-6 lg:px-8 bg-gradient-to-b from-white via-teal-50/30 to-white relative overflow-hidden flex items-center justify-center mb-32">
        {/* Background Elements */}
        <div className="absolute inset-0 -z-10">
          <div className="absolute top-20 left-10 w-72 h-72 bg-teal-300/20 rounded-full blur-3xl"></div>
          <div className="absolute bottom-10 right-10 w-96 h-96 bg-purple-300/15 rounded-full blur-3xl"></div>
        </div>

        <div className="max-w-6xl mx-auto text-center w-full">
          <h2 className="text-5xl sm:text-6xl lg:text-7xl font-black text-gray-900 mb-8 sm:mb-10 leading-tight">
            Manage Your <br />
            <span className="bg-gradient-to-r from-teal-600 to-blue-600 bg-clip-text text-transparent">
              Entire Business
            </span>
          </h2>

          <p className="text-base sm:text-lg lg:text-xl text-gray-600 mb-10 sm:mb-16 max-w-3xl mx-auto leading-relaxed">
            NEXORA is the all-in-one ERP platform designed for modern businesses. Streamline HR, Finance, Sales, and Operations from a single intuitive dashboard.
          </p>

          <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 justify-center items-center mb-16 sm:mb-24">
            <Link 
              to="/register"
              className="w-full sm:w-auto px-8 sm:px-10 py-4 sm:py-5 bg-gradient-to-r from-teal-600 to-blue-600 text-white rounded-xl font-semibold shadow-lg hover:shadow-xl transition duration-300 text-base sm:text-lg focus:outline-none focus:ring-2 focus:ring-teal-700 focus:ring-offset-2"
            >
              Start Free Trial
            </Link>
            <button className="w-full sm:w-auto px-8 sm:px-10 py-4 sm:py-5 border-2 border-gray-300 text-gray-900 rounded-xl font-semibold hover:border-gray-400 hover:bg-gray-50 transition duration-300 text-base sm:text-lg focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2">
              Watch Demo
            </button>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-8 sm:gap-10 lg:gap-12 max-w-3xl mx-auto text-center">
            <div className="p-10 sm:p-14 rounded-2xl bg-white/50">
              <p className="text-3xl sm:text-4xl font-black text-gray-900">1000+</p>
              <p className="text-xs sm:text-sm text-gray-600 mt-4 sm:mt-6">Active Users</p>
            </div>
            <div className="p-10 sm:p-14 rounded-2xl bg-white/50">
              <p className="text-3xl sm:text-4xl font-black text-gray-900">99.9%</p>
              <p className="text-xs sm:text-sm text-gray-600 mt-4 sm:mt-6">Uptime</p>
            </div>
            <div className="p-10 sm:p-14 rounded-2xl bg-white/50">
              <p className="text-3xl sm:text-4xl font-black text-gray-900">24/7</p>
              <p className="text-xs sm:text-sm text-gray-600 mt-4 sm:mt-6">Support</p>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="w-full scroll-mt-20 mt-32 py-48 sm:py-56 lg:py-64 px-4 sm:px-6 lg:px-12 bg-gray-50 flex justify-center">
        <div className="max-w-7xl w-full">
          <div className="text-center mb-12 sm:mb-16 lg:mb-20">
            <h3 className="text-3xl sm:text-4xl lg:text-5xl font-black text-gray-900 mb-3 sm:mb-4">
              Everything You Need
            </h3>
            <p className="text-base sm:text-lg text-gray-600 max-w-2xl mx-auto">
              Powerful modules designed to work together seamlessly
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 sm:gap-10 lg:gap-12">
            {[
              { id: 'hr', icon: '👥', title: 'HR Management', desc: 'Employee data, payroll, attendance, and reviews' },
              { id: 'finance', icon: '💰', title: 'Finance', desc: 'Invoicing, billing, journals, and reports' },
              { id: 'sales', icon: '📊', title: 'Sales', desc: 'Leads, opportunities, quotes, and analytics' },
              { id: 'automation', icon: '⚙️', title: 'Automation', desc: 'Workflows, rules, and smart automation' }
            ].map((feature) => (
              <article
                key={feature.id}
                onMouseEnter={() => handleMouseEnter(feature.id)}
                onMouseLeave={handleMouseLeave}
                className="group p-12 sm:p-16 bg-white rounded-2xl border border-gray-200 hover:border-teal-300 hover:shadow-lg transition duration-300 focus-within:ring-2 focus-within:ring-teal-500 focus-within:ring-offset-2"
              >
                <div className="text-5xl sm:text-6xl mb-8 sm:mb-10 group-hover:scale-110 transition duration-300" aria-hidden="true">
                  {feature.icon}
                </div>
                <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-4 sm:mb-6">{feature.title}</h3>
                <p className="text-gray-600 text-sm leading-relaxed">{feature.desc}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      {/* Why Choose Section */}
      <section className="w-full mt-32 py-48 sm:py-56 lg:py-64 px-4 sm:px-6 lg:px-12 flex justify-center">
        <div className="max-w-7xl w-full">
          <div className="text-center mb-12 sm:mb-16 lg:mb-20">
            <h3 className="text-3xl sm:text-4xl lg:text-5xl font-black text-gray-900 mb-3 sm:mb-4">
              Why NEXORA?
            </h3>
            <p className="text-base sm:text-lg text-gray-600">
              Built for the modern business
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 sm:gap-10 lg:gap-12">
            {[
              { id: 'cloud', title: 'Cloud-First', desc: 'Access from anywhere, on any device, with enterprise security' },
              { id: 'fast', title: 'Lightning Fast', desc: 'Optimized for speed with real-time data synchronization' },
              { id: 'friendly', title: 'User-Friendly', desc: 'Intuitive interface that requires zero training' },
              { id: 'scalable', title: 'Scalable', desc: 'Grows with your business from startup to enterprise' },
              { id: 'integrations', title: 'Integrations', desc: 'Connect with your favorite tools and services' },
              { id: 'support', title: 'Expert Support', desc: 'Dedicated team ready to help 24/7' }
            ].map((item) => (
              <div key={item.id} className="flex gap-6 sm:gap-8 p-12 sm:p-16 bg-white rounded-2xl border border-gray-200 hover:border-teal-300 hover:shadow-lg transition duration-300 h-full focus-within:ring-2 focus-within:ring-teal-500 focus-within:ring-offset-2">
                <div className="flex-shrink-0 w-14 h-14 sm:w-16 sm:h-16 bg-gradient-to-br from-teal-600 to-blue-600 rounded-xl flex items-center justify-center text-white font-bold text-lg flex-none" aria-hidden="true">
                  ✓
                </div>
                <div>
                  <h4 className="text-base sm:text-lg font-bold text-gray-900 mb-3 sm:mb-4">{item.title}</h4>
                  <p className="text-gray-600 text-sm leading-relaxed">{item.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Pricing Section */}
      <section id="pricing" className="w-full scroll-mt-20 mt-32 py-48 sm:py-56 lg:py-64 px-4 sm:px-6 lg:px-12 bg-gray-50 flex justify-center">
        <div className="max-w-7xl w-full">
          <div className="text-center mb-12 sm:mb-16 lg:mb-20">
            <h3 className="text-3xl sm:text-4xl lg:text-5xl font-black text-gray-900 mb-3 sm:mb-4">
              Simple, Transparent Pricing
            </h3>
            <p className="text-base sm:text-lg text-gray-600">
              Choose the plan that fits your business
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 sm:gap-10 lg:gap-12 items-center">
            {/* Startup */}
            <div className="bg-white border border-gray-200 rounded-2xl p-12 sm:p-16 hover:shadow-lg transition duration-300 h-full flex flex-col focus-within:ring-2 focus-within:ring-teal-500 focus-within:ring-offset-2">
              <h3 className="text-xl sm:text-2xl font-bold text-gray-900 mb-4">Startup</h3>
              <p className="text-gray-600 text-sm mb-8">Perfect for small teams</p>
              <div className="mb-12">
                <span className="text-3xl sm:text-4xl font-black text-gray-900">$99</span>
                <span className="text-gray-600 ml-2 text-sm">/month</span>
              </div>
              <ul className="space-y-4 sm:space-y-5 mb-12 flex-1">
                {['Up to 10 users', 'Basic modules', '10GB storage', 'Email support'].map((item, i) => (
                  <li key={i} className="flex items-center gap-2 text-gray-700 text-sm">
                    <span className="text-teal-600 font-bold" aria-hidden="true">✓</span> {item}
                  </li>
                ))}
              </ul>
              <button className="w-full py-3 border-2 border-gray-200 text-gray-900 rounded-xl font-bold hover:border-teal-600 hover:text-teal-600 transition duration-300 text-sm sm:text-base focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2">
                Get Started
              </button>
            </div>

            {/* Professional (Featured) */}
            <div className="relative bg-gradient-to-br from-teal-600 to-blue-600 text-white rounded-2xl p-12 sm:p-16 shadow-xl lg:shadow-2xl h-full flex flex-col lg:scale-105 focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-white">
              <div className="absolute top-4 right-4 bg-white/20 px-3 py-1 rounded-full text-xs font-bold">
                POPULAR
              </div>
              <h3 className="text-xl sm:text-2xl font-bold mb-4">Professional</h3>
              <p className="text-white/80 text-sm mb-8">For growing businesses</p>
              <div className="mb-12">
                <span className="text-3xl sm:text-4xl font-black">$299</span>
                <span className="text-white/80 ml-2 text-sm">/month</span>
              </div>
              <ul className="space-y-4 sm:space-y-5 mb-12 flex-1">
                {['Up to 50 users', 'All modules', '100GB storage', 'Priority support', 'API access'].map((item, i) => (
                  <li key={i} className="flex items-center gap-2 text-sm">
                    <span aria-hidden="true">✓</span> {item}
                  </li>
                ))}
              </ul>
              <Link
                to="/register"
                className="block w-full py-3 bg-white text-teal-600 rounded-xl font-bold hover:bg-gray-100 transition duration-300 text-center text-sm sm:text-base focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-teal-600"
              >
                Start Free Trial
              </Link>
            </div>

            {/* Enterprise */}
            <div className="bg-white border border-gray-200 rounded-2xl p-12 sm:p-16 hover:shadow-lg transition duration-300 h-full flex flex-col focus-within:ring-2 focus-within:ring-teal-500 focus-within:ring-offset-2">
              <h3 className="text-xl sm:text-2xl font-bold text-gray-900 mb-4">Enterprise</h3>
              <p className="text-gray-600 text-sm mb-8">For large organizations</p>
              <div className="mb-12">
                <span className="text-3xl sm:text-4xl font-black text-gray-900">Custom</span>
              </div>
              <ul className="space-y-4 sm:space-y-5 mb-12 flex-1">
                {['Unlimited users', 'Unlimited storage', 'Custom integrations', 'Dedicated support', 'SLA guarantee'].map((item, i) => (
                  <li key={i} className="flex items-center gap-2 text-gray-700 text-sm">
                    <span className="text-teal-600 font-bold" aria-hidden="true">✓</span> {item}
                  </li>
                ))}
              </ul>
              <button className="w-full bg-white border-2 border-teal-600 text-teal-600 py-3 rounded-lg font-bold hover:bg-teal-50 transition duration-300 text-sm sm:text-base focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2">
                Contact Sales
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="w-full mt-32 py-48 sm:py-56 lg:py-64 px-4 sm:px-6 lg:px-12 bg-gradient-to-r from-teal-600 to-blue-600 relative overflow-hidden flex justify-center">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-10 left-10 w-64 h-64 bg-white rounded-full blur-3xl"></div>
        </div>

        <div className="max-w-3xl text-center relative z-10">
          <h3 className="text-3xl sm:text-4xl lg:text-5xl font-black text-white mb-4 sm:mb-6">
            Ready to Transform?
          </h3>
          <p className="text-base sm:text-lg text-white/90 mb-8 sm:mb-10">
            Join thousands of companies streamlining their operations with NEXORA
          </p>
          <Link
            to="/register"
            className="inline-block px-6 sm:px-8 py-3 sm:py-4 bg-white text-teal-600 rounded-xl font-bold hover:bg-gray-100 shadow-lg hover:shadow-2xl transition duration-300 text-base sm:text-lg focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-teal-600"
          >
            Start Your Free Trial
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="w-full bg-gray-900 text-gray-400 mt-32 py-48 px-4 sm:px-6 lg:px-12 flex justify-center">
        <div className="max-w-7xl w-full">
          <div className="grid md:grid-cols-4 gap-20 mb-20">
            <div>
              <h4 className="text-white font-black text-xl mb-6">NEXORA</h4>
              <p className="text-sm leading-relaxed">
                The modern ERP platform for businesses that move fast
              </p>
            </div>
            <div>
              <h4 className="text-white font-bold mb-6 text-sm uppercase tracking-wide">Product</h4>
              <ul className="space-y-3 text-sm">
                {['Features', 'Pricing', 'Security', 'Status'].map((item, i) => (
                  <li key={i}><a href="#" className="hover:text-white transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 focus:ring-offset-gray-900 rounded px-2 py-1">{item}</a></li>
                ))}
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-6 text-sm uppercase tracking-wide">Company</h4>
              <ul className="space-y-3 text-sm">
                {['About', 'Blog', 'Careers', 'Contact'].map((item, i) => (
                  <li key={i}><a href="#" className="hover:text-white transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 focus:ring-offset-gray-900 rounded px-2 py-1">{item}</a></li>
                ))}
              </ul>
            </div>
            <div>
              <h4 className="text-white font-bold mb-6 text-sm uppercase tracking-wide">Legal</h4>
              <ul className="space-y-3 text-sm">
                {['Privacy', 'Terms', 'Cookies', 'License'].map((item, i) => (
                  <li key={i}><a href="#" className="hover:text-white transition focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 focus:ring-offset-gray-900 rounded px-2 py-1">{item}</a></li>
                ))}
              </ul>
            </div>
          </div>

          <div className="border-t border-gray-800 pt-8">
            <p className="text-center text-sm">
              &copy; 2026 NEXORA. All rights reserved. Built for the modern business.
            </p>
          </div>
        </div>
      </footer>
    </div>
  )
}
