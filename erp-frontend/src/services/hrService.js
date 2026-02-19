// HR API services
import axios from 'axios'

const HR_API = 'http://localhost:8081/api/hr'

export const employeeService = {
  getAll: () => axios.get(`${HR_API}/employees`),
  getById: (id) => axios.get(`${HR_API}/employees/${id}`),
  create: (data) => axios.post(`${HR_API}/employees`, data),
  update: (id, data) => axios.put(`${HR_API}/employees/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/employees/${id}`),
}

export const departmentService = {
  getAll: () => axios.get(`${HR_API}/department`),
  getById: (id) => axios.get(`${HR_API}/department/${id}`),
  create: (data) => axios.post(`${HR_API}/department`, data),
  update: (id, data) => axios.put(`${HR_API}/department/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/department/${id}`),
}

export const positionService = {
  getAll: () => axios.get(`${HR_API}/jobs`),
  getById: (id) => axios.get(`${HR_API}/jobs/${id}`),
  create: (data) => axios.post(`${HR_API}/jobs`, data),
  update: (id, data) => axios.put(`${HR_API}/jobs/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/jobs/${id}`),
}

export const locationService = {
  getAll: () => axios.get(`${HR_API}/locations`),
  getById: (id) => axios.get(`${HR_API}/locations/${id}`),
  create: (data) => axios.post(`${HR_API}/locations`, data),
  update: (id, data) => axios.put(`${HR_API}/locations/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/locations/${id}`),
}

export const attendanceService = {
  getAll: () => axios.get(`${HR_API}/attendances`),
  getById: (id) => axios.get(`${HR_API}/attendances/${id}`),
  create: (data) => axios.post(`${HR_API}/attendances`, data),
  update: (id, data) => axios.put(`${HR_API}/attendances/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/attendances/${id}`),
}

export const leaveService = {
  getAll: () => axios.get(`${HR_API}/leaves`),
  getById: (id) => axios.get(`${HR_API}/leaves/${id}`),
  create: (data) => axios.post(`${HR_API}/leaves`, data),
  update: (id, data) => axios.put(`${HR_API}/leaves/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/leaves/${id}`),
}

export const holidayService = {
  getAll: () => axios.get(`${HR_API}/holidays`),
  getById: (id) => axios.get(`${HR_API}/holidays/${id}`),
  create: (data) => axios.post(`${HR_API}/holidays`, data),
  update: (id, data) => axios.put(`${HR_API}/holidays/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/holidays/${id}`),
}

export const salaryService = {
  getAll: () => axios.get(`${HR_API}/salary`),
  getById: (id) => axios.get(`${HR_API}/salary/${id}`),
  create: (data) => axios.post(`${HR_API}/salary`, data),
  update: (id, data) => axios.put(`${HR_API}/salary/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/salary/${id}`),
}

export const payrollService = {
  getAll: () => axios.get(`${HR_API}/payroll`),
  getById: (id) => axios.get(`${HR_API}/payroll/${id}`),
  create: (data) => axios.post(`${HR_API}/payroll`, data),
  update: (id, data) => axios.put(`${HR_API}/payroll/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/payroll/${id}`),
}

export const deductionService = {
  getAll: () => axios.get(`${HR_API}/deductions`),
  getById: (id) => axios.get(`${HR_API}/deductions/${id}`),
  create: (data) => axios.post(`${HR_API}/deductions`, data),
  update: (id, data) => axios.put(`${HR_API}/deductions/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/deductions/${id}`),
}

export const reviewService = {
  getAll: () => axios.get(`${HR_API}/reviews`),
  getById: (id) => axios.get(`${HR_API}/reviews/${id}`),
  create: (data) => axios.post(`${HR_API}/reviews`, data),
  update: (id, data) => axios.put(`${HR_API}/reviews/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/reviews/${id}`),
}

export const goalService = {
  getAll: () => axios.get(`${HR_API}/goals`),
  getById: (id) => axios.get(`${HR_API}/goals/${id}`),
  create: (data) => axios.post(`${HR_API}/goals`, data),
  update: (id, data) => axios.put(`${HR_API}/goals/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/goals/${id}`),
}

export const hrUserRoleService = {
  getAll: () => axios.get(`${HR_API}/roles`),
  getById: (id) => axios.get(`${HR_API}/roles/${id}`),
  create: (data) => axios.post(`${HR_API}/roles`, data),
  update: (id, data) => axios.put(`${HR_API}/roles/${id}`, data),
  delete: (id) => axios.delete(`${HR_API}/roles/${id}`),
}
