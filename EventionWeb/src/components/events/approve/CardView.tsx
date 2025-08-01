import { Event } from "../../../types/Event";
import { useApproveEvent, useDeleteEvent } from "../../../api/event";
import { toast } from "sonner";
import { Link } from "react-router";

interface ApproveEventsTableProps {
    data: Event[] | undefined;
    page: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

export default function ApproveEventsTable({
    data,
    page,
    totalPages,
    onPageChange,
}: ApproveEventsTableProps) {
    const approveEventMutation = useApproveEvent();
    const rejectEventMutation = useDeleteEvent();
    const eventUrl = import.meta.env.VITE_EVENT_API_URL;
    const isMock = import.meta.env.VITE_MOCKS;

    const handlePageClick = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            onPageChange(newPage);
        }
    };

    const handleApprove = (eventId: string) => {
        approveEventMutation.mutate(
            { id: eventId },
            {
                onSuccess: () => {
                    toast.success('Event approved successfully!');
                },
                onError: () => {
                    toast.error('Failed to approve the event.');
                },
            }
        );
    };

    const handleReject = (eventId: string) => {
        rejectEventMutation.mutate(eventId, {
            onSuccess: () => {
                toast.success('Event rejected successfully!');
            },
            onError: () => {
                toast.error('Failed to reject the event.');
            },
        });
    };

    return (
        <div>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {data!.map((event) => (
                    <div
                        key={event.eventID}
                        className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-white/10 rounded-xl shadow overflow-hidden flex flex-col h-full"
                    >
                        <Link to={`/approve/${event.eventID}`}>
                            <div className="relative w-full h-35 overflow-hidden cursor-pointer group rounded-lg">
                                <img
                                    src={isMock === 'true' ? event.eventPicture : `${eventUrl}${event.eventPicture}`}
                                    alt={event.name}
                                    className="object-cover w-full h-full transition duration-300 group-hover:blur-[2.5px] group-hover:scale-[1.02]"
                                    onError={(e) => {
                                        (e.target as HTMLImageElement).src = "images/event/default_event.jpg";
                                    }}
                                />

                                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none">
                                    <span className="bg-white bg-opacity-90 dark:bg-gray-800 dark:bg-opacity-90 text-gray-900 dark:text-gray-100 rounded-full px-4 py-2 font-semibold text-sm shadow-lg select-none">
                                        See more
                                    </span>
                                </div>
                            </div>
                        </Link>

                        <div className="p-4 flex flex-col justify-between flex-1 h-full">
                            <div className="text-start flex-1">
                                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                                    {event.name}
                                </h3>
                                <p className="text-sm text-gray-500 dark:text-gray-400 truncate">
                                    {event.description}
                                </p>
                            </div>

                            <div className="mt-4 flex gap-2">
                                <button
                                    onClick={() => handleReject(event.eventID)}
                                    className="flex-1 bg-red-100 text-red-700 text-sm py-2 rounded-lg hover:bg-red-200 dark:bg-red-900 dark:text-red-400 dark:hover:bg-red-800 transition font-medium"
                                >
                                    Reject
                                </button>

                                <button
                                    onClick={() => handleApprove(event.eventID)}
                                    className="flex-1 bg-green-100 text-green-700 text-sm py-2 rounded-lg hover:bg-green-200 dark:bg-green-900 dark:text-green-400 dark:hover:bg-green-800 transition font-medium"
                                >
                                    Approve
                                </button>
                            </div>

                        </div>
                    </div>
                ))}
            </div>

            <div className="flex justify-center items-center gap-2 py-4">
                <button
                    disabled={page === 1}
                    onClick={() => handlePageClick(page - 1)}
                    className="px-3 py-1 text-sm border rounded disabled:opacity-50 text-gray-800 dark:text-gray-200 dark:border-white/10"
                >
                    Back
                </button>

                {Array.from({ length: totalPages }, (_, i) => (
                    <button
                        key={i}
                        onClick={() => handlePageClick(i + 1)}
                        className={`px-3 py-1 text-sm border rounded transition-colors ${page === i + 1
                            ? "bg-gray-200 dark:bg-gray-700 text-black dark:text-white"
                            : "text-gray-800 dark:text-gray-200 dark:border-white/10 hover:bg-gray-100 dark:hover:bg-gray-800"}`}
                    >
                        {i + 1}
                    </button>
                ))}

                <button
                    disabled={page === totalPages}
                    onClick={() => handlePageClick(page + 1)}
                    className="px-3 py-1 text-sm border rounded disabled:opacity-50 text-gray-800 dark:text-gray-200 dark:border-white/10"
                >
                    Next
                </button>
            </div>

        </div>
    );
}
