import OverallMetrics from "../../components/dashboard-overview/OverallMetrics";
import MonthlyRevenue from "../../components/charts/MonthlyRevenue";
import UsersTable from "../../components/dashboard-overview/UsersTable";
import EventsTable from "../../components/dashboard-overview/EventsTable";
import TableCard from "../../components/dashboard-overview/TableCard";
import { useEvents } from "../../api/event";
import { useUsers } from "../../api/user";
import { useUserInEvents } from "../../api/userinevent";
import ParticipationChart from "../../components/charts/participationChart";
import TicketSalesChart from "../../components/charts/ticketSalesChart";
import { UserInEvent } from "../../types/UserInEvent";
import { Event } from "../../types/Event";

export default function Overview() {

  const { data, isPending, isError } = useEvents();
  const { data: users } = useUsers();
  const { data: tickets } = useUserInEvents();

  const participationData = getParticipationData(tickets ?? null, data ?? null);
  const ticketSales = getTicketSalesOverTime(tickets ?? null);

  return (
    <>
      <div className="grid grid-cols-12 gap-4 md:gap-6">
        <div className="col-span-12 space-y-6 xl:col-span-12">
          <OverallMetrics data={isPending || isError || !data ? null : data} users={isPending || isError || !users ? null : users} />
        </div>
        <div className="col-span-12 xl:col-span-12">
          <MonthlyRevenue data={tickets || []} events={data || []} />
        </div>

        <div className="col-span-12 xl:col-span-6">

          <ParticipationChart data={participationData} />
        </div>
        <div className="col-span-12 xl:col-span-6">
          <TicketSalesChart data={ticketSales} />
        </div>

        <div className="col-span-12 xl:col-span-6">
          <TableCard title="Users" path="/users">
            <UsersTable data={users || []} />
          </TableCard>
        </div>

        <div className="col-span-12 xl:col-span-6">

          <TableCard title="Events" path="/events">
            <EventsTable data={data || []} />
          </TableCard>
        </div>

      </div>
    </>
  );
}


function getParticipationData(tickets: UserInEvent[] | null, events: Event[] | null) {
  if (!tickets || !events) return [];

  const participantsCount = new Map<string, number>();

  tickets.forEach((ticket) => {
    const eventId = ticket.event_id;
    participantsCount.set(eventId, (participantsCount.get(eventId) || 0) + 1);
  });

  const eventParticipation = events.map((event) => ({
    eventName: event.name,
    participants: participantsCount.get(event.eventID) || 0,
  }));

  eventParticipation.sort((a, b) => b.participants - a.participants);

  if (eventParticipation.length <= 5) {
    return eventParticipation;
  }

  const top5 = eventParticipation.slice(0, 5);
  const others = eventParticipation.slice(5);

  const othersParticipants = others.reduce((sum, e) => sum + e.participants, 0);

  return [...top5, { eventName: "Others", participants: othersParticipants }];
}


function getTicketSalesOverTime(tickets: UserInEvent[] | null) {
  if (!tickets) return [];
  const salesMap = new Map<string, number>();

  tickets.forEach((ticket) => {
    const date = ticket.createdAt.toString().substring(0, 10);
    salesMap.set(date, (salesMap.get(date) || 0) + 1);
  });

  return Array.from(salesMap.entries())
    .sort(([a], [b]) => a.localeCompare(b))
    .map(([date, ticketsSold]) => ({ date, ticketsSold }));
}