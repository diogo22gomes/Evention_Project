import { Address } from "./AddressUser";
import { UserType } from "./UserType";

export interface User {
    userID: string;
    username: string;
    phone: number;
    email: string;
    password?: string;
    status: boolean;
    createdAt: Date;
    loginType: string;
    userType: UserType;
    usertype_id: string;
    address?: Address;
    profilePicture?: string;
  }