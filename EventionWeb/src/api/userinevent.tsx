import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { api } from './client';
import { UserInEvent } from '../types/UserInEvent';
import { userInEventMocks } from '../mocks/userInEventMock';

const getUserInEvents = async () => (await api.get('/userinevent/api/tickets')).data;
const getUserInEventById = async (id: string) => (await api.get(`/userinevent/api/tickets/${id}`)).data;

export function useUserInEvents(): UseQueryResult<UserInEvent[]> | { data: UserInEvent[]; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<UserInEvent[]>({
        queryKey: ['userinevents'],
        queryFn: getUserInEvents,
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: userInEventMocks,
            isPending: false,
            isError: false,
        };
    }

    return query;
}

export function useUserInEventById(id: string): UseQueryResult<UserInEvent> | { data: UserInEvent | null; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<UserInEvent>({
        queryKey: ['userinevents', id],
        queryFn: () => getUserInEventById(id),
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: userInEventMocks.find(event => event.event_id === id) || null,
            isPending: false,
            isError: false,
        };
    }

    return query;
}
