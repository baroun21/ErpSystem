import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import axios from 'axios'

const PLANS = [
  {
    id: 'startup',
    name: 'Startup',
    price: 99,
    billingCycle: 'month',
    description: 'Perfect for small teams',
    features: [
      'Up to 10 users',
      'Basic HR module',
      'Basic Finance module',
      '1 company',
      '10GB storage',
      'Email support',
      'Monthly reports',
    ],
    color: 'blue'
  },
  {
    id: 'professional',
    name: 'Professional',
    price: 299,
    billingCycle: 'month',
    description: 'For growing businesses',
    features: [
      'Up to 50 users',
      'Full HR module',
      'Full Finance module',
      'Up to 5 companies',
      '100GB storage',
      'Priority email & phone support',
      'Weekly reports',
      'API access',
      'Custom workflows',
    ],
    color: 'indigo',
    popular: true
  },
  {
    id: 'enterprise',
    name: 'Enterprise',
    price: 999,
    billingCycle: 'month',
    description: 'For large organizations',
    features: [
      'Unlimited users',
      'All modules',
      'Unlimited companies',
      'Unlimited storage',
      '24/7 dedicated support',
      'Real-time reporting & analytics',
      'Advanced API access',
      'Custom integrations',
      'On-premises deployment option',
      'SLA guarantee',
    ],
    color: 'purple'
  }
]

export default function Subscription() {
  const navigate = useNavigate()
  const location = useLocation()
  const [selectedPlan, setSelectedPlan] = useState('professional')
  const [billingCycle, setBillingCycle] = useState('month')
  const [paymentMethod, setPaymentMethod] = useState('card')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [cardData, setCardData] = useState({
    cardNumber: '',
    expiryDate: '',
    cvc: '',
    name: ''
  })

  const selectedPlanData = PLANS.find(p => p.id === selectedPlan)
  const monthlyPrice = selectedPlanData.price
  const yearlyPrice = monthlyPrice * 12 * 0.8 // 20% discount for yearly
  const displayPrice = billingCycle === 'month' ? monthlyPrice : yearlyPrice
  const pricePerMonth = billingCycle === 'month' ? monthlyPrice : Math.round(yearlyPrice / 12)

  const handleCardChange = (e) => {
    const { name, value } = e.target
    setCardData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSubscribe = async (e) => {
    e.preventDefault()
    
    if (paymentMethod === 'card') {
      if (!cardData.cardNumber || !cardData.expiryDate || !cardData.cvc || !cardData.name) {
        setError('Please fill in all payment details')
        return
      }
    }

    setLoading(true)
    try {
      const response = await axios.post('http://localhost:8081/api/subscriptions', {
        userId: location.state?.userId,
        planId: selectedPlan,
        billingCycle: billingCycle,
        paymentMethod: paymentMethod,
        paymentDetails: paymentMethod === 'card' ? cardData : null,
      })

      navigate('/dashboard', { state: { subscribed: true } })
    } catch (err) {
      setError(err.response?.data?.message || 'Subscription failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const handleSkip = () => {
    navigate('/dashboard', { state: { subscriptionSkipped: true } })
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-white via-teal-50/30 to-white py-20 px-4 sm:px-6 lg:px-12 relative">
      {/* Background Elements */}
      <div className="absolute inset-0 -z-10 overflow-hidden">
        <div className="absolute top-20 right-10 w-72 h-72 bg-teal-300/20 rounded-full blur-3xl"></div>
        <div className="absolute bottom-20 left-10 w-80 h-80 bg-blue-300/15 rounded-full blur-3xl"></div>
      </div>

      <div className="max-w-full mx-auto">
        {/* Header */}
        <div className="text-center mb-24">
          <h1 className="text-5xl font-black text-gray-900 mb-8">
            Simple, Transparent <span className="bg-gradient-to-r from-teal-600 to-blue-600 bg-clip-text text-transparent">Pricing</span>
          </h1>
          <p className="text-xl text-gray-600 mb-14">Choose the plan that grows with your business</p>

          {/* Billing Cycle Toggle */}
          <div className="flex items-center justify-center gap-4">
            <span className={`text-sm font-semibold ${billingCycle === 'month' ? 'text-gray-900' : 'text-gray-500'}`}>
              Monthly
            </span>
            <button
              onClick={() => setBillingCycle(billingCycle === 'month' ? 'year' : 'month')}
              className="relative inline-flex h-10 w-16 items-center rounded-full bg-gradient-to-r from-gray-200 to-gray-300 transition"
            >
              <span
                className={`inline-block h-8 w-8 transform rounded-full bg-white shadow-md transition duration-300 ${
                  billingCycle === 'year' ? 'translate-x-7' : 'translate-x-1'
                }`}
              />
            </button>
            <span className={`text-sm font-semibold ${billingCycle === 'year' ? 'text-gray-900' : 'text-gray-500'}`}>
              Yearly <span className="text-green-600 font-bold ml-1">Save 20%</span>
            </span>
          </div>
        </div>

        {error && (
          <div className="mb-8 p-4 bg-red-50 border border-red-200 rounded-2xl max-w-2xl mx-auto">
            <p className="text-red-700 text-sm font-medium">{error}</p>
          </div>
        )}

        {/* Plans Grid */}
        <div className="grid md:grid-cols-3 gap-16 mb-20">
          {PLANS.map((plan) => (
            <div
              key={plan.id}
              onClick={() => setSelectedPlan(plan.id)}
              className={`relative bg-white rounded-2xl border transition-all duration-300 cursor-pointer ${
                selectedPlan === plan.id 
                  ? 'border-teal-600 shadow-2xl transform md:scale-105' 
                  : 'border-gray-200 shadow-lg hover:shadow-xl hover:border-teal-300'
              } ${plan.popular ? 'md:scale-105' : ''}`}
            >
              {plan.popular && (
                <div className="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                  <div className="bg-gradient-to-r from-teal-600 to-blue-600 text-white px-5 py-1.5 rounded-full text-sm font-black shadow-lg">
                    ⭐ MOST POPULAR
                  </div>
                </div>
              )}

              <div className="p-12">
                <h3 className="text-2xl font-black text-gray-900 mb-4">{plan.name}</h3>
                <p className="text-gray-600 text-sm mb-9">{plan.description}</p>

                <div className="mb-8 p-6 bg-teal-50 rounded-xl">
                  <div className="flex items-baseline gap-2">
                    <span className="text-5xl font-black text-gray-900">${displayPrice}</span>
                    <span className="text-gray-600 font-medium">{billingCycle === 'month' ? '/mo' : '/yr'}</span>
                  </div>
                  {billingCycle === 'year' && (
                    <p className="text-xs text-gray-600 mt-3 font-medium">${pricePerMonth}/month when billed annually</p>
                  )}
                </div>

                <button
                  onClick={() => setSelectedPlan(plan.id)}
                  className={`w-full py-3 rounded-xl font-bold transition mb-10 ${
                    selectedPlan === plan.id
                      ? `bg-gradient-to-r from-teal-600 to-blue-600 text-white hover:shadow-lg`
                      : `border-2 border-gray-300 text-gray-900 hover:border-teal-400`
                  }`}
                >
                  {selectedPlan === plan.id ? '✓ Selected' : 'Select Plan'}
                </button>

                <div className="space-y-5">
                  {plan.features.map((feature, idx) => (
                    <div key={idx} className="flex items-start gap-3">
                      <span className="text-teal-600 font-bold text-lg mt-0.5">✓</span>
                      <span className="text-gray-700 text-sm font-medium">{feature}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Payment Section */}
        {selectedPlan && (
          <div className="max-w-5xl mx-auto bg-white rounded-2xl border border-gray-200 shadow-lg p-12">
            <h2 className="text-3xl font-black text-gray-900 mb-12">Complete Your Setup</h2>

            {/* Plan Summary */}
            <div className="mb-10 p-8 bg-gradient-to-r from-teal-50 to-blue-50 rounded-xl border border-teal-200">
              <div className="flex justify-between items-center">
                <div>
                  <p className="text-gray-600 text-xs font-bold uppercase tracking-wide">Plan</p>
                  <p className="text-2xl font-black text-gray-900 mt-1">{selectedPlanData.name}</p>
                </div>
                <div className="text-right">
                  <p className="text-gray-600 text-xs font-bold uppercase tracking-wide">Total</p>
                  <p className="text-3xl font-black bg-gradient-to-r from-teal-600 to-blue-600 bg-clip-text text-transparent mt-1">
                    ${displayPrice}{billingCycle === 'month' ? '/mo' : '/yr'}
                  </p>
                </div>
              </div>
            </div>

            {/* Payment Method */}
            <form onSubmit={handleSubscribe} className="space-y-8">
              <div>
                <label className="block text-sm font-bold text-gray-900 mb-6 uppercase tracking-wide">Payment Method</label>
                <div className="grid grid-cols-2 gap-4">
                  {[
                    { id: 'card', label: 'Credit Card', icon: '💳' },
                    { id: 'bank', label: 'Bank Transfer', icon: '🏦' }
                  ].map(method => (
                    <button
                      key={method.id}
                      type="button"
                      onClick={() => setPaymentMethod(method.id)}
                      className={`p-4 rounded-xl border-2 transition ${
                        paymentMethod === method.id
                          ? 'border-teal-600 bg-teal-50'
                          : 'border-gray-300 bg-white hover:border-teal-300'
                      }`}
                    >
                      <span className="text-3xl block mb-2">{method.icon}</span>
                      <p className="font-bold text-gray-900">{method.label}</p>
                    </button>
                  ))}
                </div>
              </div>

              {/* Card Details */}
              {paymentMethod === 'card' && (
                <div className="space-y-5 bg-teal-50 p-8 rounded-xl border border-teal-200">
                  <input
                    type="text"
                    name="name"
                    placeholder="Cardholder Name"
                    value={cardData.name}
                    onChange={handleCardChange}
                    className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900 placeholder-gray-500"
                    disabled={loading}
                  />

                  <input
                    type="text"
                    name="cardNumber"
                    placeholder="Card Number"
                    value={cardData.cardNumber}
                    onChange={handleCardChange}
                    maxLength="19"
                    className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900 placeholder-gray-500"
                    disabled={loading}
                  />

                  <div className="grid grid-cols-2 gap-4">
                    <input
                      type="text"
                      name="expiryDate"
                      placeholder="MM/YY"
                      value={cardData.expiryDate}
                      onChange={handleCardChange}
                      maxLength="5"
                      className="px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900 placeholder-gray-500"
                      disabled={loading}
                    />
                    <input
                      type="text"
                      name="cvc"
                      placeholder="CVC"
                      value={cardData.cvc}
                      onChange={handleCardChange}
                      maxLength="4"
                      className="px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent text-gray-900 placeholder-gray-500"
                      disabled={loading}
                    />
                  </div>
                </div>
              )}

              {/* Bank Transfer */}
              {paymentMethod === 'bank' && (
                <div className="p-8 bg-blue-50 border border-blue-200 rounded-xl">
                  <p className="text-sm text-blue-900 font-medium">
                    Bank transfer instructions will be sent to your email. Complete payment within 3 days to activate your subscription.
                  </p>
                </div>
              )}

              {/* Buttons */}
              <div className="flex gap-6 pt-8">
                <button
                  type="submit"
                  disabled={loading}
                  className="flex-1 py-3 bg-gradient-to-r from-teal-600 to-blue-600 text-white font-bold rounded-xl hover:shadow-lg hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed transition duration-300"
                >
                  {loading ? 'Processing...' : `Subscribe to ${selectedPlanData.name}`}
                </button>
                <button
                  type="button"
                  onClick={handleSkip}
                  className="px-6 py-3 border-2 border-gray-300 text-gray-900 font-bold rounded-xl hover:border-teal-400 hover:bg-teal-50 transition"
                >
                  Skip For Now
                </button>
              </div>
            </form>

            {/* Terms */}
            <p className="text-xs text-gray-600 text-center mt-10 font-medium">
              By subscribing, you'll be charged ${displayPrice} {billingCycle === 'month' ? 'every month' : 'every year'}. Cancel anytime, no questions asked.
            </p>
          </div>
        )}
      </div>
    </div>
  )
}
