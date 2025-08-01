import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from "../ui/table";

import Badge from "../ui/badge/Badge";
import { User } from "../../types/User";

interface UsersTableProps {
  data: User[];
}

export default function UsersTable({ data }: UsersTableProps) {

  const datafiltered = data.filter(user => user.status == true).slice(0, 5);
  const userUrl = import.meta.env.VITE_USER_API_URL;
  const isMock = import.meta.env.VITE_MOCKS;

  return (
    <div className="overflow-hidden rounded-xl border border-gray-200 bg-white dark:border-white/[0.05] dark:bg-white/[0.03]">
      <div className="max-w-full overflow-x-auto">
        <Table>
          {/* Table Header */}
          <TableHeader className="border-b border-gray-100 dark:border-white/[0.05]">
            <TableRow>
              <TableCell
                isHeader
                className="px-5 py-3 font-medium text-gray-500 text-start text-theme-xs dark:text-gray-400"
              >
                User
              </TableCell>
              <TableCell
                isHeader
                className="px-5 py-3 font-medium text-gray-500 text-right text-theme-xs dark:text-gray-400" // Alinha o cabeçalho à direita
              >
                Status
              </TableCell>
            </TableRow>
          </TableHeader>

          {/* Table Body */}
          <TableBody className="divide-y divide-gray-100 dark:divide-white/[0.05]">
            {datafiltered.map((user) => (
              <TableRow key={user.userID}>
                <TableCell className="px-5 py-4 sm:px-6 text-start">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 overflow-hidden rounded-full">
                      <img
                        width={60}
                        height={60}
                        src={isMock === 'true' ? user.profilePicture : `${userUrl}${user.profilePicture}`}
                        alt={user.username}
                        className="w-full h-full object-contain object-cover object-center"
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

                {/* Status Column with Alignment to the Right */}
                <TableCell className="px-5 py-4 text-right text-gray-500 text-theme-sm dark:text-gray-400">
                  <Badge
                    size="sm"
                    color={
                      user.status == true
                        ? "success"
                        : "error"
                    }
                  >
                    {user.status ? "Active" : "Inactive"}
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
