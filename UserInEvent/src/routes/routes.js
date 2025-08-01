import { Router } from "express";
import userInEventController from "../controllers/userInEventController.js";
import feedbackController from "../controllers/feedbackController.js";
import {verifyToken }from "../middlewares/authMiddleware.js";
import { verifyAdmin } from "../middlewares/adminMiddleware.js";
import {verifyAdvertiser} from "../middlewares/advertiserMiddleware.js";

const router = Router();

// UserInEvents Routes
router.get('/tickets', verifyToken, verifyAdmin, userInEventController.getAllUserInEvents);
router.get('/tickets/my', verifyToken, userInEventController.getUserTickets);
router.get('/tickets/:id', userInEventController.getUserInEventById);
router.get('/tickets/event/:eventId', userInEventController.getTicketsByEvent);
router.post('/tickets', verifyToken, userInEventController.createUserInEvent);
router.put('/tickets/:id', userInEventController.updateUserInEvent);
//router.put('/tickets/:id/participation', userInEventController.updateUserParticipationInEvent);
router.delete('/tickets/:id', userInEventController.deleteUserInEvent);
router.get('/tickets/qrcode/:ticketId', userInEventController.generateQrCode);
router.put('/tickets/qrcode/read/:ticketId', userInEventController.updateUserParticipationInEvent);
//rota do ticket qr code

// Feedback Routes
router.get('/feedbacks', verifyToken, verifyAdmin, feedbackController.getAllFeedbacks);
router.get('/feedbacks/:id', feedbackController.getFeedbackById);
router.post('/feedbacks/:ticketID', verifyToken, feedbackController.createFeedback);
router.put('/feedbacks/:id', feedbackController.updateFeedback);
router.delete('/feedbacks/:id', feedbackController.deleteFeedback);
router.get('/feedbacks/event/:eventId', feedbackController.getEventFeedbacks);

export default router;