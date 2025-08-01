import { User } from "../types/User";
import { UserType } from "../types/UserType";

const adminType: UserType = {
  userTypeID: "type-uuid-1",
  type: "Admin"
};

const announcerType: UserType = {
  userTypeID: "type-uuid-2",
  type: "Announcer"
};

const userType: UserType = {
    userTypeID: "type-uuid-3",
    type: "User"
  };
  

export const userMocks: User[] = [
  {
    userID: "user-001",
    username: "john_doe",
    phone: 123456789,
    email: "john@example.com",
    password: "hashedpassword1",
    status: true,
    createdAt: new Date(),
    loginType: "email",
    userType: adminType,
    usertype_id: adminType.userTypeID,
    address: {
      addressID: "addr-uuid-1",
      road: "Main St",
      roadNumber: 100,
      postCode: "12345-678",
      NIF: "123456789",
      localtown_id: "Braga"
    },
    profilePicture: "images/user/user-01.jpg"
  },
  {
    userID: "user-002",
    username: "jane_smith",
    phone: 339939393,
    email: "jane@example.com",
    status: true,
    createdAt: new Date(),
    loginType: "google",
    userType: announcerType,
    usertype_id: announcerType.userTypeID,
    profilePicture: "images/user/user-02.jpg"
  },
  {
    userID: "user-003",
    username: "maria99",
    phone: 987654321,
    email: "maria@example.com",
    password: "hashedpassword3",
    status: false,
    createdAt: new Date(),
    loginType: "email",
    userType: announcerType,
    usertype_id: announcerType.userTypeID,
    address: {
      addressID: "addr-uuid-2",
      road: "Second St",
      roadNumber: 45,
      postCode: "23456-789",
      localtown_id: "local-2"
    },
    profilePicture: "images/user/user-03.jpg"
  },
  {
    userID: "user-004",
    username: "bruno_dev",
    phone: 399292929,
    email: "bruno@example.com",
    password: "hashedpassword4",
    status: true,
    createdAt: new Date(),
    loginType: "facebook",
    userType: adminType,
    usertype_id: adminType.userTypeID,
    profilePicture: "images/user/user-04.jpg"
  },
  {
    userID: "user-005",
    username: "sofia22",
    email: "sofia@example.com",
    phone: 111222333,
    password: "hashedpassword5",
    status: true,
    createdAt: new Date(),
    loginType: "email",
    userType: announcerType,
    usertype_id: announcerType.userTypeID,
    profilePicture: "images/user/user-05.jpg"
  },
  {
    userID: "user-006",
    username: "ricardo_007",
    email: "ricardo@example.com",
    phone: 444555666,
    status: true,
    createdAt: new Date(),
    loginType: "apple",
    userType: announcerType,
    usertype_id: announcerType.userTypeID,
    profilePicture: "images/user/user-06.jpg"
  },
  {
    userID: "user-007",
    username: "ana_moura",
    phone: 9399393283,
    email: "ana@example.com",
    status: false,
    createdAt: new Date(),
    loginType: "email",
    userType: adminType,
    usertype_id: adminType.userTypeID,
    profilePicture: "images/user/user-07.jpg"
  },
  {
    userID: "user-008",
    username: "tiago.tech",
    email: "tiago@example.com",
    phone: 999888777,
    password: "hashedpassword8",
    status: true,
    createdAt: new Date(),
    loginType: "github",
    userType: userType,
    usertype_id: userType.userTypeID,
    profilePicture: "images/user/user-08.jpg"
  },
  {
    userID: "user-009",
    username: "luis_lopes",
    phone: 3944804432,
    email: "luis@example.com",
    password: "hashedpassword9",
    status: true,
    createdAt: new Date(),
    loginType: "email",
    userType: userType,
    usertype_id: userType.userTypeID,
    profilePicture: "images/user/user-09.jpg"
  },
  {
    userID: "user-010",
    username: "carla.silva",
    phone: 9100983735,
    email: "carla@example.com",
    status: true,
    createdAt: new Date(),
    loginType: "linkedin",
    userType: userType,
    usertype_id: userType.userTypeID,
    profilePicture: "images/user/user-10.jpg"
  }
];
