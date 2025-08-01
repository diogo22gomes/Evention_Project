import { useQuery, useMutation, UseQueryResult, UseMutationResult, useQueryClient } from '@tanstack/react-query';
import { User } from '../types/User';
import { api } from './client';
import { userMocks } from '../mocks/userMock';

export interface EditUserData {
    username: string;
    phone?: string;
    email: string;
    status: boolean;
}

interface PaginatedUserResponse {
    data: User[];
    total: number;
    page: number;
    limit: number;
    totalPages: number;
}


// GETs
const getUsers = async () => {
    const token = localStorage.getItem("token");

    return (await api.get('/user/api/users', {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })).data;
};

const getMyProfile = async () => {
    const token = localStorage.getItem("token");

    return (await api.get('/user/api/users/my-profile', {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })).data;
};

const getUserById = async (id: string) => (await api.get(`/user/api/users/${id}`)).data;

//Edit
const editUser = async (id: string, data: EditUserData): Promise<User> => {
    const response = await api.put(`/user/api/users/${id}`, data);
    return response.data;
};

//Delete
const deleteUser = async (id: string) => (await api.delete(`/user/api/users/${id}`)).data;

//Create
const createUser = async (data: User) => (await api.post('/user/api/users', data)).data;

const getPaginatedUsers = async (
    page: number,
    limit: number,
    search?: string
): Promise<PaginatedUserResponse> => {
    const params = new URLSearchParams();

    params.append('page', page.toString());
    params.append('limit', limit.toString());
    if (search) {
        params.append('search', search);
    }

    const response = await api.get(`/user/api/users/paginated?${params.toString()}`);
    return response.data;
};

//use
export function useUsers(): UseQueryResult<User[]> | { data: User[]; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<User[]>({
        queryKey: ['users'],
        queryFn: getUsers,
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: userMocks,
            isPending: false,
            isError: false,
        };
    }

    return query;
}


export function useUserMyProfile(): UseQueryResult<User> | { data: User; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<User>({
        queryKey: ['userProfile'],
        queryFn: getMyProfile,
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: userMocks[0],
            isPending: false,
            isError: false,
        };
    }

    return query;
}


export function useUserById(id: string): UseQueryResult<User> | { data: User | null; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<User>({
        queryKey: ['user', id],
        queryFn: () => getUserById(id),
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: userMocks.find(user => user.userID === id) || null,
            isPending: false,
            isError: false,
        };
    }

    return query;
}

export function useEditUser(): UseMutationResult<User, Error, { id: string; data: EditUserData }> {
    const queryClient = useQueryClient();

    const mutation = useMutation<User, Error, { id: string; data: EditUserData }>({
        mutationFn: ({ id, data }) => editUser(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["users"] });
        },
    });

    return mutation;
}

export function useDeleteUser(): UseMutationResult<void, Error, string> {
    const queryClient = useQueryClient();

    const mutation = useMutation<void, Error, string>({
        mutationFn: (id) => deleteUser(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["users"] });
        },
    });

    return mutation;
}

export function useCreateUser(): UseMutationResult<User, Error, User> {
    const queryClient = useQueryClient();

    const mutation = useMutation<User, Error, User>({
        mutationFn: createUser,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["users"] });
        },
    });

    return mutation;
}

export function usePaginatedUsers(
    page: number,
    limit: number,
    search?: string
): UseQueryResult<PaginatedUserResponse, Error> {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    return useQuery<PaginatedUserResponse, Error, PaginatedUserResponse>({
        queryKey: ['users', page, limit, search],
        queryFn: () => {
            if (isMock) {
                const filtered = search
                    ? userMocks.filter((user) =>
                        user.username.toLowerCase().includes(search.toLowerCase())
                    )
                    : userMocks;

                const start = (page - 1) * limit;
                const end = start + limit;
                const paginated = filtered.slice(start, end);

                return Promise.resolve({
                    data: paginated,
                    total: filtered.length,
                    page,
                    limit,
                    totalPages: Math.ceil(filtered.length / limit),
                });
            }

            return getPaginatedUsers(page, limit, search);
        },
    });
}