import {
    Table,
    TableBody,
    TableCell,
    TableHeader,
    TableRow,
} from "../../ui/table";

import { useState } from "react";
import { MoreDotIcon } from "../../../icons";
import { Dropdown } from "../../ui/dropdown/Dropdown";
import { DropdownItem } from "../../ui/dropdown/DropdownItem";
import { useModal } from "../../../hooks/useModal";
import { Modal } from "../../ui/modal";
import Label from "../../form/Label";
import Input from "../../form/input/InputField";
import Button from "../../ui/button/Button";
import { Event } from "../../../types/Event";
import Badge from "../../ui/badge/Badge";
import { EditEventData, useDeleteEvent, useEditEvent } from "../../../api/event";
import { toast } from 'sonner'

interface EventsTableProps {
    data: Event[] | undefined;
    page: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

export default function EventsTable({
    data,
    page,
    totalPages,
    onPageChange,
}: EventsTableProps) {
    const { isOpen, openModal, closeModal } = useModal();
    const eventUrl = import.meta.env.VITE_EVENT_API_URL;
    const isMock = import.meta.env.VITE_MOCKS;

    const {
        isOpen: isDeleteModalOpen,
        openModal: openDeleteModal,
        closeModal: closeDeleteModal,
    } = useModal();

    const [eventToDelete, setEventToDelete] = useState<Event | null>(null);
    const [openDropdownId, setOpenDropdownId] = useState<string | null>(null);
    const { mutate: editEvent } = useEditEvent();
    const { mutate: deleteEvent } = useDeleteEvent();


    const [formData, setFormData] = useState<EditEventData>({
        name: '',
        description: '',
        startAt: '',
        endAt: '',
        price: 0,
    });

    const handleDeleteClick = (event: Event) => {
        setEventToDelete(event);
        openDeleteModal();
    };

    function formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString("pt-PT", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
        });
    }

    function formatLocalDateTime(date: string): string {
        const localDate = new Date(date);
        const year = localDate.getFullYear();
        const month = String(localDate.getMonth() + 1).padStart(2, "0");
        const day = String(localDate.getDate()).padStart(2, "0");
        const hours = String(localDate.getHours()).padStart(2, "0");
        const minutes = String(localDate.getMinutes()).padStart(2, "0");

        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }

    const handleEditClick = (event: Event) => {
        setEventToDelete(event);

        setFormData({
            name: event.name,
            description: event.description,
            startAt: event.startAt,
            endAt: event.endAt,
            price: event.price,
        });

        openModal();
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;

        setFormData(prev => ({
            ...prev,
            [name]: name === "startAt" || name === "endAt" ? new Date(value) : value,
        }));
    };

    const confirmDelete = () => {
        if (eventToDelete) {
            deleteEvent(eventToDelete.eventID, {
                onSuccess: () => {
                    closeDeleteModal();
                    setEventToDelete(null);
                    toast.success('Event deleted successfully!');
                },
                onError: () => {
                    toast.error('Failed to delete the event.');
                },
            });
        }
    };

    const handleSave = async () => {
        if (eventToDelete) {
            try {
                editEvent({ id: eventToDelete.eventID, data: formData });
                toast.success('Event updated successfully!');
                closeModal();
            } catch (error) {
                toast.error('Failed to update the event.');
                console.error(error);
            }
        }
    };

    const toggleDropdown = (id: string) => {
        setOpenDropdownId((prev) => (prev === id ? null : id));
    };

    const closeDropdown = () => {
        setOpenDropdownId(null);
    };

    const handlePageClick = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            onPageChange(newPage);
        }
    };

    const getEventImage = (event: Event) => {
        if (isMock === 'true') return event.eventPicture;
        return event.eventPicture?.startsWith("https")
            ? event.eventPicture
            : `${eventUrl}${event.eventPicture}`;
    };

    return (
        <div className="overflow-hidden mt-5 rounded-xl border border-gray-200 bg-white dark:border-white/[0.05] dark:bg-white/[0.03]">
            <div className="max-w-full overflow-x-auto">
                <Table>
                    <TableHeader className="border-b border-gray-100 dark:border-white/[0.05]">
                        <TableRow>
                            <TableCell isHeader className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400">Event</TableCell>
                            <TableCell isHeader className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400">Start</TableCell>
                            <TableCell isHeader className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400">End</TableCell>
                            <TableCell isHeader className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400">Price</TableCell>
                            <TableCell isHeader className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400">Status</TableCell>
                        </TableRow>
                    </TableHeader>

                    <TableBody className="divide-y divide-gray-100 dark:divide-white/[0.05]">
                        {data!.map((event) => (
                            <TableRow key={event.eventID}>
                                <TableCell className="px-5 py-4 sm:px-6 text-start">
                                    <div className="flex items-center gap-3">
                                        <div className="w-10 h-10 overflow-hidden rounded-full">
                                            <img
                                                loading="lazy"
                                                src={getEventImage(event)}
                                                alt={event.name}
                                                width={40}
                                                height={40}
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
                                        </div>
                                    </div>
                                </TableCell>

                                <TableCell className="px-4 py-3 text-gray-500 text-start text-theme-sm dark:text-gray-400">
                                    {formatDate(event.startAt)}
                                </TableCell>

                                <TableCell className="px-4 py-3 text-gray-500 text-start text-theme-sm dark:text-gray-400">
                                    {formatDate(event.endAt)}
                                </TableCell>


                                <TableCell className="px-4 py-3 text-gray-500 text-theme-sm dark:text-gray-400">
                                    {event.price > 0 ? event.price + ' €' : 'Free'}
                                </TableCell>

                                <TableCell className="px-4 py-3 text-gray-500 text-start text-theme-sm dark:text-gray-400">
                                    <Badge
                                        size="sm"
                                        color={
                                            event.eventStatus.status === "Pendente"
                                                ? "warning"
                                                : event.eventStatus.status === "Aprovado"
                                                    ? "info"
                                                    : event.eventStatus.status === "Completo"
                                                        ? "success"
                                                        : event.eventStatus.status === "Comentado"
                                                            ? "success"
                                                            : "error"
                                        }
                                    >
                                        {event.eventStatus.status}
                                    </Badge>
                                </TableCell>


                                <TableCell className="relative">
                                    <button className="dropdown-toggle" onClick={() => toggleDropdown(event.eventID)}>
                                        <MoreDotIcon className="text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 size-6" />
                                    </button>

                                    <Dropdown
                                        isOpen={openDropdownId === event.eventID}
                                        onClose={closeDropdown}
                                        className="absolute right-full top-0 mt-2 mr-2 z-50 w-40 p-2"
                                    >
                                        <DropdownItem
                                            onItemClick={() => handleEditClick(event)}
                                            className="px-3 py-2 rounded-md hover:bg-yellow-100 dark:hover:bg-yellow-700 text-yellow-600 dark:text-yellow-400"
                                        >
                                            Edit
                                        </DropdownItem>
                                        <DropdownItem
                                            onItemClick={() => handleDeleteClick(event)}
                                            className="px-3 py-2 rounded-md hover:bg-red-100 dark:hover:bg-red-700 text-red-600 dark:text-red-400"
                                        >
                                            Delete
                                        </DropdownItem>
                                    </Dropdown>


                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            <Modal isOpen={isOpen} onClose={closeModal} className="max-w-[700px] m-4">
                <div className="no-scrollbar relative w-full max-w-[700px] overflow-y-auto rounded-3xl bg-white p-4 dark:bg-gray-900 lg:p-11">
                    <div className="px-2 pr-14">
                        <h4 className="mb-5 text-2xl font-semibold text-gray-800 dark:text-white/90">
                            Edit Event Information
                        </h4>
                    </div>
                    <form className="flex flex-col">
                        <div className="custom-scrollbar h-[450px] overflow-y-auto px-2 pb-1">
                            <div>
                                <div className="grid grid-cols-1 gap-x-6 gap-y-5 lg:grid-cols-2">
                                    <div className="col-span-2 lg:col-span-1">
                                        <Label>Event Name</Label>
                                        <Input
                                            type="text"
                                            name="name"
                                            value={formData.name}
                                            onChange={handleInputChange}
                                        />
                                    </div>

                                    <div className="col-span-2 lg:col-span-1">
                                        <Label>Price</Label>
                                        <Input
                                            type="number"
                                            name="price"
                                            value={formData.price}
                                            onChange={handleInputChange}
                                        />
                                    </div>

                                    <div className="col-span-2 lg:col-span-1">
                                        <Label>Start</Label>
                                        <Input
                                            type="datetime-local"
                                            name="startAt"
                                            value={formatLocalDateTime(formData.startAt)}
                                            onChange={handleInputChange}
                                        />
                                    </div>

                                    <div className="col-span-2 lg:col-span-1">
                                        <Label>End</Label>
                                        <Input
                                            type="datetime-local"
                                            name="endAt"
                                            value={formatLocalDateTime(formData.endAt)}
                                            onChange={handleInputChange}
                                        />
                                    </div>

                                    <div className="col-span-2">
                                        <Label>Description</Label>
                                        <textarea
                                            rows={4}
                                            name="description"
                                            value={formData.description}
                                            onChange={handleInputChange}
                                            className="w-full rounded-md border border-gray-300 bg-white p-2 text-sm text-gray-800 dark:border-gray-700 dark:bg-gray-800 dark:text-white/90 h-32 resize-y"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="flex items-center gap-3 px-2 mt-6 lg:justify-end">
                            <Button type="button" size="sm" variant="outline" onClick={closeModal}>
                                Close
                            </Button>
                            <Button type="button" size="sm" onClick={handleSave}>
                                Save Changes
                            </Button>
                        </div>


                    </form>
                </div>
            </Modal>

            <Modal isOpen={isDeleteModalOpen} onClose={closeDeleteModal} className="max-w-md m-4">
                <div className="w-full rounded-3xl bg-white p-6 dark:bg-gray-900">
                    <h4 className="mb-4 text-xl font-semibold text-gray-800 dark:text-white/90">
                        Confirm Event Removal
                    </h4>
                    <p className="mb-6 text-sm text-gray-600 dark:text-gray-300">
                        Are you sure you want to delete the event{" "}
                        <span className="font-medium text-gray-800 dark:text-white">
                            {eventToDelete?.name}
                        </span>
                        ? This action cannot be undone.
                    </p>
                    <div className="flex justify-end gap-3">
                        <Button type="button" variant="outline" size="sm" onClick={closeDeleteModal}>
                            Cancel
                        </Button>
                        <Button type="button" variant='primary' size="sm" onClick={confirmDelete}>
                            Confirm
                        </Button>
                    </div>
                </div>
            </Modal>

            <div className="flex justify-center items-center gap-2 py-4">
                <button
                    disabled={page === 1}
                    onClick={() => handlePageClick(page - 1)}
                    className="px-3 py-1 text-sm border rounded disabled:opacity-50 text-gray-800 dark:text-gray-200 dark:border-white/10"
                >
                    Back
                </button>

                {Array.from({ length: totalPages }, (_, i) => (
                    <button
                        key={i}
                        onClick={() => handlePageClick(i + 1)}
                        className={`px-3 py-1 text-sm border rounded transition-colors ${page === i + 1
                            ? "bg-gray-200 dark:bg-gray-700 text-black dark:text-white"
                            : "text-gray-800 dark:text-gray-200 dark:border-white/10 hover:bg-gray-100 dark:hover:bg-gray-800"}`}
                    >
                        {i + 1}
                    </button>
                ))}

                <button
                    disabled={page === totalPages}
                    onClick={() => handlePageClick(page + 1)}
                    className="px-3 py-1 text-sm border rounded disabled:opacity-50 text-gray-800 dark:text-gray-200 dark:border-white/10"
                >
                    Next
                </button>
            </div>
        </div>
    );
}
