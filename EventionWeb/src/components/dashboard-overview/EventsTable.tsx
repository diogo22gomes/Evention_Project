import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from "../ui/table";

import Badge from "../ui/badge/Badge";
import { Event } from "../../types/Event";

interface EventsTableProps {
  data: Event[];
}

export default function EventsTable({ data }: EventsTableProps) {

  const eventUrl = import.meta.env.VITE_EVENT_API_URL;
  const isMock = import.meta.env.VITE_MOCKS;

  const datafilter = data.filter((event) => event.eventStatus.status === "Aprovado" || event.eventStatus.status === "Completo");
  const tableData = datafilter.slice(0, 5)

  return (
    <div className="overflow-hidden rounded-xl border border-gray-200 bg-white dark:border-white/[0.05] dark:bg-white/[0.03]">
      <div className="max-w-full overflow-x-auto">
        <Table>
          <TableHeader className="border-b border-gray-100 dark:border-white/[0.05]">
            <TableRow>
              <TableCell
                isHeader
                className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400"
              >
                Event
              </TableCell>
              <TableCell
                isHeader
                className="px-5 py-3 font-medium text-gray-500 text-right text-theme-xs dark:text-gray-400"
              >
                Status
              </TableCell>
            </TableRow>
          </TableHeader>

          <TableBody className="divide-y divide-gray-100 dark:divide-white/[0.05]">
            {tableData.map((event) => (
              <TableRow key={event.eventID}>
                <TableCell className="px-5 py-4 sm:px-6 text-start">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 overflow-hidden rounded-full">
                      <img
                        src={isMock === 'true' ? event.eventPicture : `${eventUrl}${event.eventPicture}`}
                        alt={event.name}
                        width={60}
                        height={60}
                        className="object-cover object-center w-full h-full"
                        onError={(e) => {
                          (e.target as HTMLImageElement).src = "images/event/default_event.jpg";
                        }}
                      />
                    </div>
                    <div>
                      <span className="block font-medium text-gray-800 text-theme-sm dark:text-white/90">
                        {event.name}
                      </span>
                      <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                        {event.price} €
                      </span>
                    </div>
                  </div>
                </TableCell>

                <TableCell className="px-5 py-4 text-right text-gray-500 text-theme-sm dark:text-gray-400">
                  <Badge
                    size="sm"
                    color={
                      event.eventStatus.status === "Completo"
                        ? "success"
                        : event.eventStatus.status === "Aprovado"
                          ? "primary"
                          : "error"
                    }
                  >
                    {event.eventStatus.status}
                  </Badge>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}
