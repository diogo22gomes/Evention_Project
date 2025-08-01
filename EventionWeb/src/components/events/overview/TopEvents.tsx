import { useMemo } from "react";
import { UserInEvent } from "../../../types/UserInEvent";
import TopTier from "./TopTier";

interface TopUserInEventsProps {
    data: UserInEvent[] | undefined;
}

interface RankedEvent {
    event_id: string;
    participantCount: number;
}

export default function TopEventsMetrics({ data }: TopUserInEventsProps) {
    const topRankedEvents = useMemo<RankedEvent[]>(() => {
        if (!data) return [];

        const filteredData = data.filter(({ participated }) => participated);

        const map: Record<string, number> = {};
        filteredData.forEach(({ event_id }) => {
            map[event_id] = (map[event_id] || 0) + 1;
        });

        return Object.entries(map)
            .sort(([, a], [, b]) => b - a)
            .slice(0, 3)
            .map(([event_id, count]) => ({
                event_id,
                participantCount: count,
            }));
    }, [data]);

    return (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3">
            {topRankedEvents && data && topRankedEvents.map((event, index) => (
                <TopTier
                    key={event.event_id}
                    rank={index}
                    eventID={event.event_id}
                    participantCount={event.participantCount}
                    userInEvents={data}
                />
            ))}
        </div>
    );
}
