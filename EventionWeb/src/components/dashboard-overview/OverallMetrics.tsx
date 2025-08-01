import { Link } from "react-router";
import {
  CalenderIcon,
  GroupIcon,
} from "../../icons";
import { Event } from "../../types/Event";
import { User } from "../../types/User";

interface OverallMetricsProps {
  data: Event[] | null;
  users: User[] | null;
}

export default function OverallMetrics({ data, users }: OverallMetricsProps) {

  const userUrl = import.meta.env.VITE_USER_API_URL;
  const eventUrl = import.meta.env.VITE_EVENT_API_URL;
  const isMock = import.meta.env.VITE_MOCKS;

  if (!data) {
    return (
      <div className="flex justify-center items-center h-[200px]">
        <div className="w-10 h-10 border-4 border-gray-300 border-t-blue-600 rounded-full animate-spin" />
      </div>
    );
  }

  if (!users) {
    return (
      <div className="flex justify-center items-center h-[200px]">
        <div className="w-10 h-10 border-4 border-gray-300 border-t-blue-600 rounded-full animate-spin" />
      </div>
    );
  }

  const activeUsers = users.filter((user) => user.status === true).length;
  const numberofEvents = data.length;
  const pendingEvents = data.filter((event) => event.eventStatus.status === "Pendente").length;

  const activeUserImages = users.map((user) => user.profilePicture).filter((image) => image !== undefined).slice(0, 5);
  const activeEventImages = data.map((event) => event.eventPicture).filter((image) => image !== undefined).slice(0, 5);
  const eventsToApproveImages = data.filter((event) => event.eventStatus.status === "Pendente").map((event) => event.eventPicture).filter((image) => image !== undefined).slice(0, 5);

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 md:gap-6">

      <Link to="/users" className="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-800 dark:bg-white/[0.03] md:p-6 hover:shadow-lg transition">
        <div className="flex items-center justify-center w-12 h-12 bg-gray-100 rounded-xl dark:bg-gray-800">
          <GroupIcon className="text-gray-800 size-6 dark:text-white/90" />
        </div>
        <div className="flex items-end justify-between mt-5">
          <div>
            <span className="text-sm text-gray-500 dark:text-gray-400">
              Users
            </span>
            <h4 className="mt-2 font-bold text-gray-800 text-title-sm dark:text-white/90">
              {activeUsers}
            </h4>
          </div>
          <div className="flex -space-x-2">
            {activeUserImages.map((picture, index) => (
              <div
                key={index}
                className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900"
              >
                <img
                  width={24}
                  height={24}
                  src={isMock === 'true' ? picture : `${userUrl}${picture}`}
                  alt={`Team member ${index + 1}`}
                  className="w-full size-6"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = "images/user/default_user.jpg";
                  }}
                />
              </div>
            ))}
          </div>
        </div>
      </Link>

      <Link to="/events" className="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-800 dark:bg-white/[0.03] md:p-6 hover:shadow-lg transition">
        <div className="flex items-center justify-center w-12 h-12 bg-gray-100 rounded-xl dark:bg-gray-800">
          <CalenderIcon className="text-gray-800 size-6 dark:text-white/90" />
        </div>
        <div className="flex items-end justify-between mt-5">
          <div>
            <span className="text-sm text-gray-500 dark:text-gray-400">
              Events
            </span>
            <h4 className="mt-2 font-bold text-gray-800 text-title-sm dark:text-white/90">
              {numberofEvents}
            </h4>
          </div>
          <div className="flex -space-x-2">
            {activeEventImages.map((picture, index) => (
              <div
                key={index}
                className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900"
              >
                <img
                  width={24}
                  height={24}
                  src={isMock === 'true' ? picture : `${eventUrl}${picture}`}
                  alt={`Team member ${index + 1}`}
                  className="w-full size-6"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = "images/event/default_event.jpg";
                  }}
                />
              </div>
            ))}
          </div>
        </div>
      </Link>

      <Link to="/approve" className="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-800 dark:bg-white/[0.03] md:p-6 hover:shadow-lg transition">
        <div className="flex items-center justify-center w-12 h-12 bg-gray-100 rounded-xl dark:bg-gray-800">
          <CalenderIcon className="text-gray-800 size-6 dark:text-white/90" />
        </div>
        <div className="flex items-end justify-between mt-5">
          <div>
            <span className="text-sm text-gray-500 dark:text-gray-400">
              Events to approve
            </span>
            <h4 className="mt-2 font-bold text-gray-800 text-title-sm dark:text-white/90">
              {pendingEvents}
            </h4>
          </div>
          <div className="flex -space-x-2">
            {eventsToApproveImages.map((picture, index) => (
              <div
                key={index}
                className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900"
              >
                <img
                  width={24}
                  height={24}
                  src={isMock === 'true' ? picture : `${eventUrl}${picture}`}
                  alt={`Team member ${index + 1}`}
                  className="w-full size-6"
                />
              </div>
            ))}
          </div>
        </div>
      </Link>

    </div>
  );
}
