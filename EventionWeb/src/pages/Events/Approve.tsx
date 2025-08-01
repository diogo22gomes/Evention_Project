import { useState } from "react";
import ApproveEventsTable from "../../components/events/approve/Table";
import ApproveEventsCardView from "../../components/events/approve/CardView";
import { GridIcon, ListIcon } from "../../icons";
import { usePaginatedEvents } from "../../api/event";

export default function ApproveEvents() {
    const [page, setPage] = useState(1);
    const limit = 6;

    const { data, isPending, isError } = usePaginatedEvents('suspended', page, limit);

    const [viewType, setViewType] = useState<"table" | "card">("table");

    if (isPending)
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

    if (data?.data.length == 0) return (
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
                No suspended events
            </span>
        </div>
    );

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
                    Error loading pending events. Refresh the page or try again later.
                </span>
            </div>
        )

    }



    return (
        <>
            <div className="flex justify-between mb-4">
                <div>
                    <h1 className="text-2xl font-semibold text-gray-800 dark:text-white">
                        Approve Events
                    </h1>
                </div>
                <div className="inline-flex items-center gap-1 rounded-md border p-1 dark:border-white/10">
                    <button
                        onClick={() => setViewType("table")}
                        className={`p-2 rounded-md transition-colors ${viewType === "table"
                            ? "bg-gray-200 text-gray-900 dark:bg-gray-700 dark:text-white"
                            : "text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800"
                            }`}
                        title="List View"
                    >
                        <ListIcon className="w-5 h-5" />
                    </button>
                    <button
                        onClick={() => setViewType("card")}
                        className={`p-2 rounded-md transition-colors ${viewType === "card"
                            ? "bg-gray-200 text-gray-900 dark:bg-gray-700 dark:text-white"
                            : "text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800"
                            }`}
                        title="Card View"
                    >
                        <GridIcon className="w-5 h-5" />
                    </button>
                </div>
            </div>

            {viewType === "table" && data ? (
                <ApproveEventsTable
                    data={data.data ?? []}
                    page={page}
                    totalPages={data?.totalPages ?? 1}
                    onPageChange={setPage}
                />
            ) : data ? (
                <ApproveEventsCardView
                    data={data.data ?? []}
                    page={page}
                    totalPages={data?.totalPages ?? 1}
                    onPageChange={setPage}
                />
            ) : null}
        </>
    );
}
