import React from "react";
import Chart from "react-apexcharts";

interface Props {
  data: { date: string; ticketsSold: number }[];
}

const TicketSalesChart: React.FC<Props> = ({ data }) => {
  const series = [
    {
      name: "Tickets sold",
      data: data.map((d) => d.ticketsSold),
    },
  ];

  const options: ApexCharts.ApexOptions = {
    chart: { type: "line", toolbar: { show: false } },
    xaxis: { categories: data.map((d) => d.date), type: "category" },
    stroke: { curve: "smooth" },
    dataLabels: { enabled: false },
    tooltip: { enabled: true },
    colors: ["#ef4444"],
  };

  return (
    <div className="rounded-2xl border border-gray-200 bg-white px-5 pb-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6">
      <h3 className="mb-2 text-lg font-semibold text-gray-800 dark:text-white/90">Ticket Sales Over Time</h3>
      <Chart options={options} series={series} type="line" height={350} />
    </div>
  );
};

export default TicketSalesChart;
