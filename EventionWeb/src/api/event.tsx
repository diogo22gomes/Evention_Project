import { useQuery, useMutation, UseQueryResult, UseMutationResult, useQueryClient } from '@tanstack/react-query';
import { api } from './client';
import { eventMocks, eventStatuses } from '../mocks/eventMock';
import { Event } from '../types/Event';

export interface EditEventData {
    name: string;
    description: string;
    price: number;
    startAt: string;
    endAt: string;
}

interface PaginatedEventResponse {
    data: Event[];
    total: number;
    page: number;
    limit: number;
    totalPages: number;
}

const getEvents = async () => (await api.get('/event/api/events')).data;
const getSuspendedEvents = async () => (await api.get('/event/api/events/suspended')).data;
const getEventById = async (id: string) => (await api.get(`/event/api/events/${id}`)).data;
const approveEvent = async (id: string) => (await api.put(`/event/api/events/${id}/status`)).data;
const editEvent = async (id: string, data: EditEventData): Promise<Event> => {
    const response = await api.put(`/event/api/events/${id}`, data);
    return response.data;
};
const deleteEvent = async (id: string) => (await api.delete(`/event/api/events/${id}`)).data;
const createEvent = async (data: Event) => (await api.post('/event/api/events', data)).data;
const getPaginatedEvents = async (
    page: number,
    limit: number,
    status?: string,
    search?: string
): Promise<PaginatedEventResponse> => {
    const params = new URLSearchParams();

    if (status) {
        params.append('status', status);
    }
    params.append('page', page.toString());
    params.append('limit', limit.toString());
    if (search) {
        params.append('search', search);
    }

    const response = await api.get(`/event/api/eventsPaginated?${params.toString()}`);
    return response.data;
};



export function useEvents(): UseQueryResult<Event[]> | { data: Event[]; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<Event[]>({
        queryKey: ['events'],
        queryFn: getEvents,
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: eventMocks,
            isPending: false,
            isError: false,
        };
    }

    return query;
}

export function useSuspendedEvents(): UseQueryResult<Event[]> | { data: Event[]; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<Event[]>({
        queryKey: ['events'],
        queryFn: getSuspendedEvents,
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: eventMocks.filter((event) => event.eventStatus.status == "Pendente"),
            isPending: false,
            isError: false,
        };
    }

    return query;
}

export function useEventById(id: string): UseQueryResult<Event> | { data: Event | null; isPending: false; isError: false } {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const query = useQuery<Event>({
        queryKey: ['event', id],
        queryFn: () => getEventById(id),
        enabled: !isMock,
    });

    if (isMock) {
        return {
            data: eventMocks.find(event => event.eventID === id) || null,
            isPending: false,
            isError: false,
        };
    }

    return query;
}

export function useEditEvent(): UseMutationResult<Event, Error, { id: string; data: EditEventData }> {
    const queryClient = useQueryClient();

    const mutation = useMutation<Event, Error, { id: string; data: EditEventData }>({
        mutationFn: ({ id, data }) => editEvent(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["events"] });
        },
    });

    return mutation;
}

export function useApproveEvent(): UseMutationResult<Event, Error, { id: string }> {
    const queryClient = useQueryClient();
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const mutation = useMutation<Event, Error, { id: string }>({
        mutationFn: async ({ id }) => {
            if (isMock) {
                const event = eventMocks.find(e => e.eventID === id);
                const approvedStatus = eventStatuses.find(status => status.status === "Aprovado");
                event!.eventStatus = approvedStatus!;
                return { ...event };
            } else {
                return approveEvent(id);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["events"] });
        },
    });

    return mutation;
}

export function useDeleteEvent(): UseMutationResult<void, Error, string> {
    const queryClient = useQueryClient();
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    const mutation = useMutation<void, Error, string>({
        mutationFn: async (id) => {
            if (isMock) {
                const index = eventMocks.findIndex(e => e.eventID === id);
                if (index === -1) {
                    throw new Error("Evento não encontrado no mock");
                }

                eventMocks.splice(index, 1);
                return;
            } else {
                return deleteEvent(id);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["events"] });
        },
    });

    return mutation;
}


export function useCreateEvent(): UseMutationResult<Event, Error, Event> {
    const queryClient = useQueryClient();

    const mutation = useMutation<Event, Error, Event>({
        mutationFn: createEvent,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["events"] });
        },
    });

    return mutation;
}

export function usePaginatedEvents(
    status: string,
    page: number,
    limit: number,
    search?: string
): UseQueryResult<PaginatedEventResponse, Error> {
    const isMock = import.meta.env.VITE_MOCKS === 'true';

    return useQuery<PaginatedEventResponse, Error, PaginatedEventResponse>({
        queryKey: ['events', status, page, limit, search],
        queryFn: () => {
            if (isMock) {
                const filtered = search
                    ? eventMocks.filter((event) =>
                        event.name.toLowerCase().includes(search.toLowerCase())
                    )
                    : eventMocks;

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

            return getPaginatedEvents(page, limit, status, search);
        },
    });
}


