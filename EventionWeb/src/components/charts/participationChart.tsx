import React from "react";
import Chart from "react-apexcharts";

interface Props {
  data: { eventName: string; participants: number }[];
}

const ParticipationChart: React.FC<Props> = ({ data }) => {
  const series = [
    {
      name: "Participants",
      data: data.map((d) => d.participants),
    },
  ];

  const options: ApexCharts.ApexOptions = {
    chart: { type: "bar", toolbar: { show: false }, stacked: false },
    plotOptions: {
      bar: { horizontal: true, barHeight: "60%", borderRadius: 4 },
    },
    yaxis: { title: { text: undefined } },
    xaxis: { categories: data.map((d) => d.eventName), labels: { formatter: (val) => val.toString() }, tickAmount: 5 },
    dataLabels: { enabled: false },
    tooltip: { enabled: true },
    colors: ["#10b981"],
  };

  return (
    <div className="rounded-2xl border border-gray-200 bg-white px-5 pb-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6">
      <h3 className="mb-2 text-lg font-semibold text-gray-800 dark:text-white/90">Participation by Event</h3>
      <Chart options={options} series={series} type="bar" height={350} />
    </div>
  );
};

export default ParticipationChart;
