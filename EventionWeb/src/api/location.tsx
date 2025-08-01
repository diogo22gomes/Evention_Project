import { api } from './client';

export const getLocations = () => api.get('/location/api/locations');
export const getLocationById = (id: string) => api.get(`/location/api/locations/${id}`);
