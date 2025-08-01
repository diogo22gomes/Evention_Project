import { useState } from "react";
import { UserInEvent } from "../../types/UserInEvent";
import { useEvents } from "../../api/event";
import { useUsers } from "../../api/user";
import { Modal } from "../ui/modal";

interface TopOrganizersProps {
  data: UserInEvent[];
}

export default function TopOrganizersMetrics({ data }: TopOrganizersProps) {
  const { data: events } = useEvents();
  const { data: users } = useUsers();
  const userUrl = import.meta.env.VITE_USER_API_URL;
  const isMock = import.meta.env.VITE_MOCKS;

  const [selectedUserId, setSelectedUserId] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  if (!events || !users) return null;

  const eventMap = new Map(events.map(event => [event.eventID, event]));
  const userMap = new Map(users.map(user => [user.userID, user]));

  const organizerProfits = new Map<string, { userId: string; total: number }>();

  for (const ticket of data) {
    if (!ticket.participated) continue;
    const event = eventMap.get(ticket.event_id);
    if (!event) continue;

    const organizerId = event.userId;
    const currentProfit = organizerProfits.get(organizerId)?.total || 0;

    organizerProfits.set(organizerId, {
      userId: organizerId,
      total: currentProfit + event.price,
    });
  }

  const topOrganizers = Array.from(organizerProfits.values())
    .sort((a, b) => b.total - a.total)
    .slice(0, 3);
  const reordered = [topOrganizers[1], topOrganizers[0], topOrganizers[2]];

  const podiumStyles = [
    {
      size: "w-20 h-20",
      label: "🥈 2º Lugar",
      color: "text-gray-400 border-gray-300",
      boxHeight: "h-28",
    },
    {
      size: "w-24 h-24",
      label: "🥇 1º Lugar",
      color: "text-yellow-500 border-yellow-400",
      boxHeight: "h-36",
    },
    {
      size: "w-20 h-20",
      label: "🥉 3º Lugar",
      color: "text-amber-600 border-amber-500",
      boxHeight: "h-24",
    },
  ];

  const openModal = (userId: string) => {
    setSelectedUserId(userId);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedUserId(null);
    setIsModalOpen(false);
  };

  const selectedUser = selectedUserId ? userMap.get(selectedUserId) : null;
  const selectedUserEvents = selectedUserId
    ? events.filter(event => event.userId === selectedUserId)
    : [];

  const getTicketCount = (eventId: string) =>
    data.filter(ticket => ticket.event_id === eventId && ticket.participated).length;

  return (
    <>
      <div className="flex flex-col sm:flex-row justify-center items-end gap-8 mt-7 w-full max-w-6xl mx-auto px-4">
        {reordered.map((organizer, index) => {
          const user = userMap.get(organizer?.userId || "");

          if (!organizer || !user) return null;

          const style = podiumStyles[index];

          const widthClasses = ["min-w-[160px]", "min-w-[200px]", "min-w-[160px]"];
          const imageSizes = ["w-24 h-24", "w-28 h-28", "w-24 h-24"];
          const boxHeights = ["h-40", "h-48", "h-36"];

          return (
            <div
              key={organizer.userId}
              onClick={() => openModal(organizer.userId)}
              className={`cursor-pointer relative flex flex-col items-center justify-end 
          ${boxHeights[index]} ${widthClasses[index]} 
          bg-white dark:bg-white/[0.03] rounded-2xl shadow-lg p-5 
          border-2 ${style.color} hover:scale-105 transition-transform`}
            >
              <img
                src={
                  isMock === "true"
                    ? user.profilePicture
                    : `${userUrl}${user.profilePicture}`
                }
                alt={user.username || "Organizador"}
                className={`rounded-full border-4 ${style.color} ${imageSizes[index]} object-cover aspect-square`}
                onError={(e) => {
                          (e.target as HTMLImageElement).src = "images/user/default_user.jpg";
                        }}
              />
              <div className="mt-3 text-center min-w-0">
                <div className={`font-bold text-lg ${style.color}`}>
                  {style.label}
                </div>
                <div
                  className="text-md text-gray-800 dark:text-gray-100 font-semibold truncate overflow-hidden whitespace-nowrap max-w-[150px]"
                  title={user.username}
                >
                  {user.username}
                </div>

                <div className="text-sm text-gray-500 dark:text-gray-400 mt-1">
                  {organizer.total.toLocaleString("pt-PT", {
                    style: "currency",
                    currency: "EUR",
                  })}
                </div>
              </div>
            </div>
          );
        })}
      </div>


      <Modal isOpen={isModalOpen} onClose={closeModal} showCloseButton={false} className="max-w-lg mx-auto p-6">
        <div className="max-h-[80vh] overflow-y-auto">
          <h2
            className="text-xl font-bold mb-4 text-center text-gray-800 dark:text-gray-100 truncate max-w-full overflow-hidden whitespace-nowrap"
            title={`Eventos organizados por ${selectedUser?.username}`}
          >
            Events organized by {selectedUser?.username}
          </h2>

          <ul className="space-y-3">
            {selectedUserEvents.length > 0 ? (
              selectedUserEvents.map(event => {
                const ticketCount = getTicketCount(event.eventID);
                return (
                  <li
                    key={event.eventID}
                    className="border border-gray-300 dark:border-gray-600 p-4 rounded-lg bg-gray-50 dark:bg-gray-800 flex justify-between items-center"
                  >
                    <div className="max-w-[70%]">
                      <div
                        className="font-semibold text-gray-800 dark:text-gray-100 truncate overflow-hidden whitespace-nowrap"
                        title={event.name}
                      >
                        {event.name}
                      </div>
                      <div className="text-sm text-gray-600 dark:text-gray-400">
                        {new Date(event.startAt).toLocaleDateString("pt-PT")}
                      </div>
                      <div className="text-sm text-gray-600 dark:text-gray-400">
                        Price:{" "}
                        {event.price.toLocaleString("pt-PT", {
                          style: "currency",
                          currency: "EUR",
                        })}
                      </div>
                    </div>

                    {ticketCount > 0 ?
                      <div className="flex items-center gap-1">
                        <span className="text-xl">🎫</span>
                        <span className="font-bold text-lg text-gray-800 dark:text-gray-100">
                          {ticketCount}
                        </span>
                      </div> : null
                    }

                  </li>
                );
              })
            ) : (
              <p className="text-gray-600 dark:text-gray-300">No events</p>
            )}
          </ul>

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
