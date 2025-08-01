import { prisma } from '../prismaClient.js';

const feedbackService = {

   /**
   * Get all Feedbacks
   * @returns {Promise<Array>} List of all Feedbacks
   */
  async getAllFeedbacks() {
    return prisma.feedback.findMany();
  },

   /**
   * Get a specific Feedback by ID
   * @param {string} id - The ID of the Feedback to fetch
   * @returns {Promise<Object|null>} The Feedback object or null if not found
   */
  async getFeedbackById(id) {
    return prisma.feedback.findUnique({
      where: { feedbackID: id },
    });
  },

   /**
   * Create a new Feedback
   * @param {Object} data - The data for creating the Feedback
   * @returns {Promise<Object>} The created Feedback object
   */
  async createFeedback(data) {
    return prisma.feedback.create({
      data,
    });
  },

   /**
   * Update an Feedback by ID
   * @param {string} id - The ID of the Feedback to update
   * @param {Object} data - The updated Feedback data
   * @returns {Promise<Object>} The updated Feedback object
   */
  async updateFeedback(id, data) {
    return prisma.feedback.update({
      where: { feedbackID: id },
      data,
    });
  },

   /**
   * Delete an Feedback by ID
   * @param {string} id - The ID of the Feedback to delete
   * @returns {Promise<void>} Resolves when the Feedback is deleted
   */
  async deleteFeedback(id) {
    return prisma.feedback.delete({
      where: { feedbackID: id },
    });
  },

   /**
   * Get Feedbacks from a specific Event by the Event ID
   * @param {string} eventId - The ID of the Event to fetch
   * @returns {Promise<Array>} List of the Event Feedbacks
   */
  async getEventFeedbacks(eventId) {
    const userInEvents = await prisma.userInEvent.findMany({
      where: {
        event_id: eventId,
        feedback_id: { not: null },
      },
      include: {
        feedback: true,
      },
    });

    // get apenas feedbacks
    return userInEvents.map((userInEvent) => userInEvent.feedback);
  },
};

export default feedbackService;
