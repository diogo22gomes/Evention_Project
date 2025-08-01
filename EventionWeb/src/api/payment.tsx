import { api } from './client';

export const getPayments = () => api.get('/payment/api/payments');
export const getPaymentById = (id: string) => api.get(`/payment/api/payments/${id}`);
