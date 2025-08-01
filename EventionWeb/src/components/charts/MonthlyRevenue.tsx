import Chart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { useState } from "react";
import { UserInEvent } from "../../types/UserInEvent";
import { Event } from "../../types/Event";

interface MonthlyRevenueProps {
  data: UserInEvent[];
  events: Event[];
}

export default function MonthlyRevenuee({ data, events }: MonthlyRevenueProps) {

  const [showProfit, setShowProfit] = useState(false);

  const isLoading = !data.length || !events.length;

  const monthlyEventCount = new Array(12).fill(0);
  const monthlyRevenue = new Array(12).fill(0);
  const monthlyTicketCount = new Array(12).fill(0);

  const eventMap = new Map<string, Event>();
  events.forEach((event) => {
    eventMap.set(event.eventID, event);
  });

  const countedEventsByMonth = new Array(12)
    .fill(null)
    .map(() => new Set<string>());

  data.forEach((ticket) => {
    const event = eventMap.get(ticket.event_id);
    if (!event) return;

    const month = new Date(event.startAt).getMonth();

    monthlyRevenue[month] += event.price ?? 0;
    monthlyTicketCount[month] += 1;

    if (!countedEventsByMonth[month].has(event.eventID)) {
      countedEventsByMonth[month].add(event.eventID);
      monthlyEventCount[month] += 1;
    }
  });

  const monthlyRevenueFormatted = monthlyRevenue.map((value) =>
    parseFloat(value.toFixed(2))
  );

  const monthlyProfitFormatted = monthlyRevenue.map((value) =>
    parseFloat((value * 0.1).toFixed(2))
  );

  const options: ApexOptions = {
    legend: {
      show: false,
      position: "top",
      horizontalAlign: "left",
    },
    colors: ["#465FFF", "#9CB9FF", "#6EE7B7"],
    chart: {
      fontFamily: "Outfit, sans-serif",
      height: 310,
      type: "line",
      toolbar: {
        show: false,
      },
    },
    stroke: {
      curve: "straight",
      width: [2, 2, 2],
    },
    fill: {
      type: "gradient",
      gradient: {
        opacityFrom: 0.55,
        opacityTo: 0,
      },
    },
    markers: {
      size: 0,
      strokeColors: "#fff",
      strokeWidth: 2,
      hover: {
        size: 6,
      },
    },
    grid: {
      xaxis: {
        lines: {
          show: false,
        },
      },
      yaxis: {
        lines: {
          show: true,
        },
      },
    },
    dataLabels: {
      enabled: false,
    },
    tooltip: {
      enabled: true,
      x: {
        format: "dd MMM yyyy",
      },
      y: {
        formatter: (value, { w, seriesIndex }) => {
          const seriesName = w.globals.seriesNames[seriesIndex];
          if (seriesName === "Revenue" || seriesName === "Profit") {
            return `${value.toFixed(2)} €`;
          }
          return Math.round(value).toString();
        },
      },
    },
    xaxis: {
      type: "category",
      categories: [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
      ],
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
      tooltip: {
        enabled: false,
      },
    },
    yaxis: {
      labels: {
        style: {
          fontSize: "12px",
          colors: ["#6B7280"],
        },
        formatter: (value) => `${Math.round(value)} €`,
      },
      title: {
        text: "",
        style: {
          fontSize: "0px",
        },
      },
    },
  };

  const series = [
    {
      name: "Events",
      data: monthlyEventCount,
    },
    {
      name: showProfit ? "Profit" : "Revenue",
      data: showProfit ? monthlyProfitFormatted : monthlyRevenueFormatted,
    },
    {
      name: "Tickets",
      data: monthlyTicketCount,
    },
  ];

  return (
    <div className="rounded-2xl border border-gray-200 bg-white px-5 pb-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6">
      <div className="flex flex-col gap-5 mb-6 sm:flex-row sm:justify-between">
        <div className="w-full">
          <h3 className="text-lg font-semibold text-gray-800 dark:text-white/90">
            Monthly Revenue
          </h3>
          <p className="mt-1 text-gray-500 text-theme-sm dark:text-gray-400">
            {showProfit
              ? "Profit for the platform over the last 12 months."
              : "Revenue generated from events over the last 12 months."}
          </p>
        </div>
        <button
          onClick={() => setShowProfit(!showProfit)}
          className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors"
        >
          {showProfit ? "See Revenue" : "See Profit"}
        </button>
      </div>


      <div className="w-full max-w-full overflow-x-auto h-[320px]">
        {/* Loader sobreposto */}
        {isLoading && (
          <div className="absolute inset-0 z-10 flex items-center justify-center bg-white/60 dark:bg-gray-900/60">
            <div className="w-10 h-10 border-4 border-gray-300 border-t-blue-600 rounded-full animate-spin" />
          </div>
        )}

        {!isLoading && (
          <Chart
            options={options}
            series={series}
            type="area"
            height={300}
          />
        )}
      </div>


    </div>
  );
}
