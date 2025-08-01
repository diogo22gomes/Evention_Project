import { prisma } from '../prismaClient.js';
import feedbackService from '../services/feedbackService.js';
import userInEventService from '../services/userInEventService.js';

const feedbackController = {

  /**
   * Get all Feedbacks
   * @auth none
   * @route {GET} /feedbacks
   * @returns {Array} List of Feedbacks
   */
  async getAllFeedbacks(req, res) {
    try {
      const feedbacks = await feedbackService.getAllFeedbacks();

      if (feedbacks == null || feedbacks.length === 0) {
        return res.status(404).json({ message: 'No events found' });
      }

      res.status(200).json(feedbacks);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching feedbacks' });
    }
  },

  /**
   * Get a Feedback by its ID
   * @auth none
   * @route {GET} /feedbacks/{id}
   * @param {String} id - The ID of the Feedback
   * @returns {Feedback} The Feedback object
   */
  async getFeedbackById(req, res) {
    const { id } = req.params; // gets id from param url
    try {
      const feedback = await feedbackService.getFeedbackById(id);
  
      if (!feedback) {
        return res.status(404).json({ message: 'Feedback not found' });
      }
  
      res.status(200).json(feedback);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching Feedback by ID' });
    }
  },

  /**
   * Get Feedbacks for a specific Event
   * @auth none
   * @route {GET} /feedbacks/event/{eventId}
   * @param {String} eventId - The ID of the Event
   * @returns {Array} List of Feedbacks for the Event
   */
    async getEventFeedbacks(req, res) {
        const { eventId } = req.params; // get event id from param url

        try {

          const feedbacks = await feedbackService.getEventFeedbacks(eventId);

            res.status(200).json(feedbacks);
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: 'Error fetching feedbacks for the event' });
        }
    },

  /**
   * Create a new Feedback
   * @auth none
   * @route {POST} /feedbacks
   * @body {rating, commentary} - Feedback details
   * @returns {Feedback} The created Feedback object
   */
  async createFeedback(req, res) {
    const { rating, commentary } = req.body;
    const { ticketID } = req.params; 
    
    try {
    
      if (typeof rating !== 'number' || rating < 1 || rating > 5) {
        return res.status(400).json({ message: 'Rating must be a number between 1 and 5' });
      }

      console.log(ticketID);

      const ticket = await userInEventService.getUserInEventById(ticketID);
      if (!ticket) {
        return res.status(404).json({ message: 'UserInEVent not found' });
      }
      if (ticket.feedback_id !== null && ticket.feedback_id !== undefined) {
        return res.status(400).json({ message: 'This Ticket already has a feedback' });
      }

      const newFeedback = await feedbackService.createFeedback({
        rating,
        commentary,
      });

      const updatedticket = await userInEventService.updateUserInEvent(ticketID, {
        feedback_id: newFeedback.feedbackID,
      });
      if (!updatedticket) {
        return res.status(404).json({ message: 'UserInEvent not found or could not be updated' });
      }

      res.status(201).json(newFeedback);
      
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error creating feedback' });
    }
  },

  /**
   * Update an existing Feedback
   * @auth none
   * @route {PUT} /feedbacks/{id}
   * @param {String} id - Feedback ID
   * @body {rating, commentary} - Updated feedback details
   * @returns {Feedback} The updated Feedback object
   */
  async updateFeedback(req, res) {
    const { id } = req.params;
    const feedbackData= req.body;
    try {
      const existingFeedback = await feedbackService.getFeedbackById(id);

      if (!existingFeedback) {
        return res.status(404).json({ message: 'Feedback not found' });
      }

      const updatedFeedback = await eventService.updateEvent(eventId, feedbackData);

      res.status(200).json(updatedFeedback);

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error updating feedback' });
    }
  },

  /**
   * Delete a Feedback
   * @auth none
   * @route {DELETE} /feedbacks/{id}
   * @param {String} id - Feedback ID
   * @returns {Message} Success or failure message
   */
  async deleteFeedback(req, res) {
    const { id } = req.params;
    try {
      const existingFeedback = await feedbackService.getFeedbackById(id);

      if (!existingFeedback) {
        return res.status(404).json({ message: 'Feedback not found' });
      }

      await feedbackService.deleteFeedback(id);
      res.status(200).json({ message: 'Feedback deleted successfully' });

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error deleting feedback' });
    }
  },

};

export default feedbackController;
