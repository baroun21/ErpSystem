export const normalizeList = (payload) => {
  if (!payload) {
    return []
  }

  if (Array.isArray(payload)) {
    return payload
  }

  return (
    payload.content ||
    payload.data ||
    payload.results ||
    payload.items ||
    payload.records ||
    payload.invoices ||
    payload.bills ||
    payload.customers ||
    payload.suppliers ||
    []
  )
}

export const pickFirst = (item, keys, fallback = '-') => {
  for (const key of keys) {
    const value = item?.[key]
    if (value !== undefined && value !== null && value !== '') {
      return value
    }
  }
  return fallback
}

export const pickFirstNumber = (item, keys, fallback = 0) => {
  for (const key of keys) {
    const value = item?.[key]
    if (value !== undefined && value !== null && value !== '') {
      const numberValue = Number(value)
      if (!Number.isNaN(numberValue)) {
        return numberValue
      }
    }
  }
  return fallback
}

export const formatMoney = (value, currency = '$') => {
  const amount = Number(value)
  if (Number.isNaN(amount)) {
    return `${currency}0.00`
  }
  return `${currency}${amount.toFixed(2)}`
}

export const formatDate = (value) => {
  if (!value) {
    return '-'
  }
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return String(value)
  }
  return date.toLocaleDateString()
}
