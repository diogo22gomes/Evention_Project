import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from "../ui/table";
import { useModal } from "../../hooks/useModal";
import { Dropdown } from "../ui/dropdown/Dropdown";
import { DropdownItem } from "../ui/dropdown/DropdownItem";
import { MoreDotIcon } from "../../icons";
import Badge from "../ui/badge/Badge";
import { User } from "../../types/User";
import { useState } from "react";
import { Modal } from "../ui/modal";
import Input from "../form/input/InputField";
import Label from "../form/Label";
import Button from "../ui/button/Button";
import { useEditUser, useDeleteUser, EditUserData } from "../../api/user";
import { toast } from 'sonner'

interface UsersTableOverviewProps {
  data: User[];
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function UsersTableOverview({ data, page, totalPages, onPageChange }: UsersTableOverviewProps) {
  const [openDropdownId, setOpenDropdownId] = useState<string | null>(null);
  const [usertoEdit, setUserToEdit] = useState<User | null>(null);
  const [formData, setFormData] = useState<EditUserData>({
    username: "",
    email: "",
    phone: "",
    status: true,
  });

  const { mutate: editUser } = useEditUser();
  const { mutate: deleteUser } = useDeleteUser();
  const userUrl = import.meta.env.VITE_USER_API_URL;
  const isMock = import.meta.env.VITE_MOCKS;

  const {
    isOpen: isEditModalOpen,
    openModal: openEditModal,
    closeModal: closeEditModal,
  } = useModal();

  const {
    isOpen: isDeleteModalOpen,
    openModal: openDeleteModal,
    closeModal: closeDeleteModal,
  } = useModal();

  const [userToDelete, setUserToDelete] = useState<User | null>(null);

  const handleEditClick = (user: User) => {
    setUserToEdit(user);
    setFormData({
      username: user.username,
      email: user.email,
      phone: user.phone !== null ? user.phone.toString() : "",
      status: user.status,
    });
    openEditModal();
  };

  const handleDeleteClick = (user: User) => {
    setUserToDelete(user);
    openDeleteModal();
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: name === "status"
        ? value === "true"
        : value,
    }));
  };

  const toggleDropdown = (id: string) => {
    setOpenDropdownId((prev) => (prev === id ? null : id));
  };

  const closeDropdown = () => {
    setOpenDropdownId(null);
  };

  const handleSave = () => {
    if (usertoEdit) {
      editUser({
        id: usertoEdit.userID,
        data: {
          username: formData.username,
          email: formData.email,
          phone: formData.phone,
          status: formData.status,
        },
      });
      toast.success('User updated successfully!');
      closeEditModal();
      setUserToEdit(null);
    }
  };

  const confirmDelete = () => {
    if (userToDelete) {
      deleteUser(userToDelete.userID, {
        onSuccess: () => {
          closeDeleteModal();
          setUserToDelete(null);
          toast.success('User deleted successfully!');
        },
        onError: () => {
          toast.error('Failed to delete the user.');
        },
      });
    }
  };

  const handlePageClick = (newPage: number) => {
    if (newPage >= 1 && newPage <= totalPages) {
      onPageChange(newPage);
    }
  };

  const getUserImage = (user: User) => {
    if (isMock === 'true') return user.profilePicture;
    return user.profilePicture?.startsWith("https")
      ? user.profilePicture
      : `${userUrl}${user.profilePicture}`;
  };

  return (
    <div className="overflow-hidden rounded-xl border border-gray-200 bg-white dark:border-white/[0.05] dark:bg-white/[0.03] mt-5">
      <div className="max-w-full overflow-x-auto">
        <Table>
          <TableHeader className="border-b border-gray-100 dark:border-white/[0.05]">
            <TableRow>
              <TableCell isHeader className="px-5 py-3 text-start text-theme-xs text-gray-500 dark:text-gray-400">
                User
              </TableCell>
              <TableCell isHeader className="px-5 py-3 text-start text-theme-xs text-gray-500 dark:text-gray-400">
                Email
              </TableCell>
              <TableCell isHeader className="px-5 py-3 text-start text-theme-xs text-gray-500 dark:text-gray-400">
                Phone
              </TableCell>
              <TableCell isHeader className="px-5 py-3 text-start text-theme-xs text-gray-500 dark:text-gray-400">
                Status
              </TableCell>
              <TableCell isHeader children={undefined} />
            </TableRow>
          </TableHeader>
          <TableBody className="divide-y divide-gray-100 dark:divide-white/[0.05]">
            {data.map((user) => (
              <TableRow key={user.userID}>
                <TableCell className="px-5 py-4 sm:px-6 text-start">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 overflow-hidden rounded-full">

                      <img
                        loading="lazy"
                        src={getUserImage(user)}
                        alt={user.username}
                        width={40}
                        height={40}
                        className="object-cover object-center w-full h-full"
                        onError={(e) => {
                          (e.target as HTMLImageElement).src = "images/user/default_user.jpg";
                        }}
                      />
                    </div>
                    <div>
                      <span className="block font-medium text-gray-800 text-theme-sm dark:text-white/90">
                        {user.username}
                      </span>
                      <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                        {user.userType.type}
                      </span>
                    </div>
                  </div>
                </TableCell>
                <TableCell className="px-4 py-3 text-theme-sm text-start text-gray-500 dark:text-gray-400">
                  {user.email}
                </TableCell>
                <TableCell className="px-4 py-3 text-theme-sm text-start text-gray-500 dark:text-gray-400">
                  {user.phone}
                </TableCell>

                <TableCell className="px-4 py-3 text-theme-sm text-start text-gray-500 dark:text-gray-400">
                  <Badge size="sm" color={user.status ? "success" : "error"}>
                    {user.status ? "Active" : "Inactive"}
                  </Badge>
                </TableCell>
                <TableCell className="relative">
                  <button className="dropdown-toggle" onClick={() => toggleDropdown(user.userID)}>
                    <MoreDotIcon className="text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 size-6" />
                  </button>
                  <Dropdown
                    isOpen={openDropdownId === user.userID}
                    onClose={closeDropdown}
                    className="absolute right-full top-0 mt-2 mr-2 z-50 w-40 p-2"
                  >

                    <DropdownItem
                      onItemClick={() => handleEditClick(user)}
                      className="px-3 py-2 rounded-md hover:bg-yellow-100 dark:hover:bg-yellow-700 text-yellow-600 dark:text-yellow-400"
                    >
                      Edit
                    </DropdownItem>
                    <DropdownItem
                      onItemClick={() => handleDeleteClick(user)}
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

      <Modal isOpen={isEditModalOpen} onClose={closeEditModal} className="max-w-md m-4">
        <div className="rounded-3xl bg-white p-6 dark:bg-gray-900">
          <h4 className="mb-4 text-xl font-semibold text-gray-800 dark:text-white/90">Edit User</h4>
          <form className="space-y-4">
            <div>
              <Label>Username</Label>
              <Input name="username" value={formData.username} onChange={handleInputChange} />
            </div>
            <div>
              <Label>Email</Label>
              <Input name="email" type="email" value={formData.email} onChange={handleInputChange} />
            </div>
            <div>
              <Label>Phone</Label>
              <Input name="phone" value={formData.phone} onChange={handleInputChange} />
            </div>
            <div>
              <Label>Status</Label>
              <select
                name="status"
                value={formData.status.toString()}
                onChange={handleInputChange}
                className="w-full rounded-md border border-gray-300 bg-white p-2 text-sm text-gray-800 dark:border-gray-700 dark:bg-gray-800 dark:text-white/90"
              >
                <option value="true">Active</option>
                <option value="false">Inactive</option>
              </select>
            </div>
            <div className="flex justify-end gap-2">
              <Button type="button" variant="outline" size="sm" onClick={closeEditModal}>Cancel</Button>
              <Button type="button" size="sm" onClick={handleSave}>Save</Button>
            </div>
          </form>
        </div>
      </Modal>

      <Modal isOpen={isDeleteModalOpen} onClose={closeDeleteModal} className="max-w-md m-4">
        <div className="rounded-3xl bg-white p-6 dark:bg-gray-900">
          <h4 className="mb-4 text-xl font-semibold text-gray-800 dark:text-white/90">Confirm User Removal</h4>
          <p className="mb-6 text-sm text-gray-600 dark:text-gray-300">
            Are you sure you want to delete{" "}
            <span className="font-medium text-gray-800 dark:text-white">
              {userToDelete?.username}
            </span>
            ? This action cannot be undone.
          </p>
          <div className="flex justify-end gap-3">
            <Button variant="outline" size="sm" onClick={closeDeleteModal}>Cancel</Button>
            <Button variant="primary" size="sm" onClick={confirmDelete}>Confirm</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
