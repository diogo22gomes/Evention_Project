import { useMemo, useState } from "react";
import { useEventById } from "../../../api/event";
import { useUsers } from "../../../api/user";
import { Modal } from "../../ui/modal";
import { UserInEvent } from "../../../types/UserInEvent";

const rankStyles = [
  "bg-yellow-400 text-white",
  "bg-gray-400 text-white",
  "bg-amber-600 text-white",
];

interface TopTierProps {
  eventID: string;
  participantCount: number;
  rank: number;
  userInEvents: UserInEvent[];
}

export default function TopTier({
  eventID,
  participantCount,
  rank,
  userInEvents,
}: TopTierProps) {
  const { data: event, isPending } = useEventById(eventID);
  const { data: users } = useUsers();
  const eventUrl = import.meta.env.VITE_EVENT_API_URL;
  const userUrl = import.meta.env.VITE_USER_API_URL
  const isMock = import.meta.env.VITE_MOCKS;

  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const participantsInEvent = userInEvents.filter(
    (u) => u.event_id === eventID
  );

  const resolvedUsers = useMemo(() => {
    return users?.filter((user) =>
      participantsInEvent.some((p) => p.user_id === user.userID)
    );
  }, [users, participantsInEvent]);

  if (isPending) {
    return (
      <div className="flex items-center gap-4 p-4 rounded-xl border border-gray-200 bg-white dark:border-gray-800 dark:bg-white/[0.03] animate-pulse w-full max-w-3xl mx-auto">
        <div className="w-16 h-16 rounded-lg bg-gray-200 dark:bg-gray-700" />
        <div className="flex-1 space-y-2">
          <div className="h-4 w-1/2 bg-gray-200 rounded dark:bg-gray-700" />
          <div className="h-3 w-1/3 bg-gray-200 rounded dark:bg-gray-700" />
        </div>
      </div>
    );
  }

  if (!event) return null;

  return (
    <>
      <div
        onClick={openModal}
        className={`cursor-pointer flex items-center gap-4 p-4 rounded-2xl border w-full max-w-3xl mx-auto
    ${rank === 0 ? "border-yellow-400 shadow-lg" : "border-gray-200"}
    bg-white dark:bg-white/[0.03] dark:border-gray-800 transition hover:scale-[1.02]`}
      >
        <div className="relative flex-shrink-0">
          <img
            src={isMock === "true" ? event.eventPicture : `${eventUrl}${event.eventPicture}`}
            alt={event.name}
            className="w-16 h-16 object-cover rounded-xl border-2 border-gray-300"
          />
          <div
            className={`absolute -top-2 -right-2 w-6 h-6 rounded-full text-xs font-bold flex items-center justify-center shadow ${rankStyles[rank]}`}
          >
            {rank + 1}
          </div>
        </div>

        <div className="flex-1 min-w-0">
          <h4
            className="text-lg font-bold text-gray-800 dark:text-white 
                 truncate"
            title={event.name}
          >
            {event.name}
          </h4>
          <p className="text-sm text-gray-500 dark:text-gray-400">
            {participantCount} participant{participantCount !== 1 ? "s" : ""}
          </p>
        </div>
      </div>


      <Modal
        isOpen={isModalOpen}
        onClose={closeModal}
        showCloseButton={false}
        className="max-w-lg w-full mx-auto p-6"
      >
        <div className="max-h-[80vh] overflow-y-auto">
          <h2 className="text-xl font-bold mb-4 text-center text-gray-800 dark:text-gray-100 break-words">
            Participants of &quot;{event.name}&quot;
          </h2>

          {resolvedUsers && resolvedUsers.length > 0 ? (
            <ul className="space-y-3">
              {resolvedUsers.map((user) => (
                <li
                  key={user.userID}
                  className="flex items-center gap-3 p-3 border rounded-lg bg-gray-50 dark:bg-gray-800 border-gray-200 dark:border-gray-700"
                >
                  <img
                    src={
                      isMock === "true"
                        ? user.profilePicture
                        : `${userUrl}${user.profilePicture}`
                    }
                    alt={user.username}
                    className="w-10 h-10 rounded-full object-cover border border-gray-300"
                                    onError={(e) => {
                          (e.target as HTMLImageElement).src = "images/user/default_user.jpg";
                        }}
                  />
                  <div>
                    <div className="font-semibold text-gray-800 dark:text-gray-100">
                      {user.username}
                    </div>
                    <div className="text-sm text-gray-500 dark:text-gray-400">
                      {user.email}
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-600 dark:text-gray-300">No participants.</p>
          )}

          <button
            onClick={closeModal}
            className="mt-5 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white dark:bg-blue-500 dark:hover:bg-blue-600 rounded w-full"
          >
            Close
          </button>
        </div>
      </Modal>

    </>
  );
}
