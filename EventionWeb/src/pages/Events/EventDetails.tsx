import { useParams } from "react-router";
import { useApproveEvent, useDeleteEvent, useEventById } from "../../api/event";
import { format } from "date-fns";
import { toast } from "sonner";
import { useNavigate } from "react-router";
import { BackIcon } from "../../icons";

export default function EventDetails() {
    const { eventId } = useParams();
    const navigate = useNavigate()
    const { data: event, isPending, isError } = useEventById(eventId!);
    const approveEventMutation = useApproveEvent();
    const rejectEventMutation = useDeleteEvent();

    const eventUrl = import.meta.env.VITE_EVENT_API_URL;
    const isMock = import.meta.env.VITE_MOCKS;

    const handleApprove = () => {
        approveEventMutation.mutate(
            { id: eventId! },
            {
                onSuccess: () => {
                    navigate(-1)
                    toast.success("Event approved successfully!");
                },
                onError: () => {
                    toast.error("Error approving the event.");
                },
            }
        );
    };

    const handleReject = () => {
        rejectEventMutation.mutate(eventId!, {
            onSuccess: () => {
                navigate(-1)
                toast.success("Event rejected successfully!");
            },
            onError: () => {
                toast.error("Error rejecting the event.");
            },
        });
    };

    if (isError) {
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
                    Error loading event details. Refresh the page or try again later.
                </span>
            </div>
        );
    }

    if (isPending || !event) {
        return (
            <div className="flex items-center justify-center h-[200px]">
                <svg
                    aria-hidden="true"
                    className="inline w-10 h-10 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600"
                    viewBox="0 0 100 101"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path
                        d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                        fill="currentColor"
                    />
                    <path
                        d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                        fill="currentFill"
                    />
                </svg>
            </div>
        );
    }

    return (
        <div className="max-w-5xl mx-auto">
            <div className="bg-white dark:bg-gray-900 rounded-xl shadow overflow-hidden">
                <div className="relative w-full h-90">
                    <img
                        src={isMock === "true" ? event.eventPicture : `${eventUrl}${event.eventPicture}`}
                        alt={event.name}
                        className="w-full h-full object-cover rounded-t-xl"
                        onError={(e) => {
                            (e.target as HTMLImageElement).src = "images/event/default_event.jpg";
                        }}
                    />

                    <button
                        onClick={() => navigate(-1)}
                        className="absolute top-4 left-4 bg-white dark:bg-gray-800 text-gray-600 dark:text-gray-200 shadow-md hover:bg-gray-100 dark:hover:bg-gray-700 rounded-full p-2"
                        aria-label="Voltar"
                    >
                        <BackIcon />
                    </button>
                </div>

                <div className="p-6 space-y-4">
                    <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
                        {event.name}
                    </h1>

                    <p className="text-gray-700 dark:text-gray-300">
                        {event.description}
                    </p>

                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm text-gray-600 dark:text-gray-400">
                        <div>
                            <span className="font-semibold">Start:</span>{" "}
                            {format(new Date(event.startAt), "dd/MM/yyyy HH:mm")}
                        </div>
                        <div>
                            <span className="font-semibold">End:</span>{" "}
                            {format(new Date(event.endAt), "dd/MM/yyyy HH:mm")}
                        </div>
                        <div>
                            <span className="font-semibold">Price:</span>{" "}
                            {event.price > 0 ? `€ ${event.price.toFixed(2)}` : "Free"}
                        </div>
                        <div>
                            <span className="font-semibold">Status:</span>{" "}
                            {event.eventStatus.status}
                        </div>
                    </div>

                    <div className="mt-6 flex gap-4">
                        <button
                            onClick={handleReject}
                            className="flex-1 bg-red-100 text-red-700 text-sm py-2 rounded-lg hover:bg-red-200 dark:bg-red-900 dark:text-red-400 dark:hover:bg-red-800 transition font-medium"
                        >
                            Reject
                        </button>

                        <button
                            onClick={handleApprove}
                            className="flex-1 bg-green-100 text-green-700 text-sm py-2 rounded-lg hover:bg-green-200 dark:bg-green-900 dark:text-green-400 dark:hover:bg-green-800 transition font-medium"
                        >
                            Approve
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
