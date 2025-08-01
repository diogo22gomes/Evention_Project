import { BrowserRouter as Router, Routes, Route } from "react-router";
import SignIn from "./pages/AuthPages/SignIn";
//import SignUp from "./pages/AuthPages/SignUp";
import NotFound from "./pages/OtherPage/NotFound";
import UserProfiles from "./pages/UserProfiles";
import AppLayout from "./layout/AppLayout";
import { ScrollToTop } from "./components/common/ScrollToTop";
import Overview from "./pages/Dashboard/Overview";
import EventsOverview from "./pages/Events/Overview";
import ApproveEvents from "./pages/Events/Approve";
import UsersOverview from "./pages/UsersOverview/UsersOverview";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import EventDetails from "./pages/Events/EventDetails";
import { Toaster } from "sonner";

export default function App() {
  return (
    <>
      <Toaster richColors position="top-center" />
      <Router>
        <ScrollToTop />
        <Routes>

          <Route element={<AppLayout />}>
            <Route index path="/" element={<ProtectedRoute><Overview /></ProtectedRoute>} />
            <Route path="/events" element={<ProtectedRoute><EventsOverview /></ProtectedRoute>} />
            <Route path="/approve" element={<ProtectedRoute><ApproveEvents /></ProtectedRoute>} />
            <Route path="/approve/:eventId" element={<ProtectedRoute><EventDetails /></ProtectedRoute>} />
            <Route path="/users" element={<ProtectedRoute><UsersOverview /></ProtectedRoute>} />
            <Route path="/profile" element={<ProtectedRoute><UserProfiles /></ProtectedRoute>} />
          </Route>

          <Route path="/signin" element={<SignIn />} />
          {/* <Route path="/signup" element={<SignUp />} /> */}

          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </>
  );
}
