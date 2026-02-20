export const getCompanyId = () => {
  if (typeof window === 'undefined') {
    return 'COMP-1'
  }

  const stored = window.localStorage.getItem('erp.companyId')
  return stored || 'COMP-1'
}
