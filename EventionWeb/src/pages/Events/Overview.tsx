import EventsTable from "../../components/events/overview/Table";
import { usePaginatedEvents } from "../../api/event";
import { useUserInEvents } from "../../api/userinevent";
import TopEventsMetrics from "../../components/events/overview/TopEvents";
import { useState } from "react";
import Input from "../../components/form/input/InputField";
import { useDebounce } from "use-debounce";

export default function EventsOverview() {
    const [page, setPage] = useState(1);
    const limit = 7;
    const [search, setSearch] = useState("");
    const [debouncedSearch] = useDebounce(search, 300);

    const { data, isPending, isError } = usePaginatedEvents('approved', page, limit, debouncedSearch);
    const { data: tickets, isPending: isTicketsPending, isError: isTicketsError } = useUserInEvents();

    if (isError || isTicketsError) {
        return (
            <div className="flex flex-col items-center justify-center h-[200px] space-y-4">
                <img
                    src="/images/error/Mask Group.svg"
                    alt="Error"
                    className="hidden dark:block opacity-70"
                />
                <img
                    src="/images/error/Group 33596.svg"
                    alt="Error"
                    className="dark:hidden opacity-70"
                />

                <span className="text-lg text-gray-700 dark:text-gray-300 font-semibold">
                    Error loading events. Refresh the page or try again later.
                </span>
            </div>
        )

    }

    return (
        <>
            {tickets && <TopEventsMetrics data={tickets} />}

            <div className="mt-5 max-w-sm relative">
                <Input
                    type="text"
                    placeholder="Search events..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="w-full pr-10"
                />

                {(isPending || isTicketsPending) && (
                    <div className="absolute right-3 top-3">
                        <svg
                            aria-hidden="true"
                            className="w-4 h-4 text-gray-400 animate-spin fill-blue-500"
                            viewBox="0 0 50 50"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <circle
                                className="opacity-25"
                                cx="25"
                                cy="25"
                                r="20"
                                stroke="currentColor"
                                strokeWidth="5"
                                fill="none"
                            />
                            <path
                                className="opacity-75"
                                fill="currentFill"
                                d="M25 5a20 20 0 0120 20h-5a15 15 0 00-15-15V5z"
                            />
                        </svg>

                    </div>
                )}
            </div>

            {data && data.data.length === 0 ? (
                <div className="text-center text-gray-600 dark:text-gray-400 mt-10">
                    No events to show up.
                </div>
            ) : (
                <EventsTable
                    data={data?.data ?? []}
                    page={page}
                    totalPages={data?.totalPages ?? 1}
                    onPageChange={setPage}
                />
            )}
        </>
    );
}