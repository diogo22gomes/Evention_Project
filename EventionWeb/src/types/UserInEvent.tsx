export interface UserInEvent {
    ticketID: string;
    event_id: string;
    user_id: string;
    feedback_id?: string;
    createdAt: Date;
    participated: boolean;
}
