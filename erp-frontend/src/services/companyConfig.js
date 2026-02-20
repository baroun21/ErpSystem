const DEFAULT_COMPANY_ID = 1

export const getCompanyId = () => {
  const stored = localStorage.getItem('companyId')
  if (!stored) {
    return DEFAULT_COMPANY_ID
  }

  const parsed = Number(stored)
  return Number.isNaN(parsed) ? DEFAULT_COMPANY_ID : parsed
}
