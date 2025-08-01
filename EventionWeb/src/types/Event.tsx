import { EventStatus } from "./EventStatus";

export interface Event {
    eventID: string;
    userId: string;
    name: string;
    description: string;
    startAt: string;
    endAt: string;
    price: number;
    createdAt: Date;
    eventStatus: EventStatus;
    eventPicture?: string;
}
