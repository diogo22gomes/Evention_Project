import { Login } from "../types/Auth";
import { api } from "./client";


export const login = async (data: Login) => (await api.post('/user/api/users/login', data)).data;