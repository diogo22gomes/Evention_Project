import { PrismaClient } from '@prisma/client';
import axios from 'axios';
import admin from 'firebase-admin';
import fs from 'fs';

const prisma = new PrismaClient();

const serviceAccount = JSON.parse(fs.readFileSync('./src/evention-7c28e-firebase-adminsdk-z84tf-31e1581df0.json'));

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});


const eventService = {
  /**
   * Get all events
   * @returns {Promise<Array>} List of all events
   */
  async getAllEvents() {
    return prisma.event.findMany({
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
    });
  },

  /**
 * Get events by limit
 * @returns {Promise<Array>} List of events
 */
  async getEventsPaginated(status = 'all', page = 1, limit = 10, search = null) {
    const skip = (page - 1) * limit;

    let statusFilter = {};
    if (status === 'approved') {
      statusFilter = {
        eventStatus: {
          eventStatusID: '22222222-2222-2222-2222-222222222222',
        },
      };
    } else if (status === 'suspended') {
      statusFilter = {
        eventStatus: {
          eventStatusID: '11111111-1111-1111-1111-111111111111',
        },
      };
    }

    const searchFilter = search
      ? {
        name: {
          contains: search,
          mode: 'insensitive',
        },
      }
      : {};

    const where = {
      AND: [
        ...Object.keys(statusFilter).length ? [statusFilter] : [],
        ...Object.keys(searchFilter).length ? [searchFilter] : [],
      ],
    };

    const total = await prisma.event.count({ where });

    const data = await prisma.event.findMany({
      where,
      skip,
      take: limit,
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
      orderBy: {
        createdAt: 'desc',
      },
    });

    return {
      data,
      total,
      page,
      limit,
      totalPages: Math.ceil(total / limit),
    };
  },

  /**
   * Get suspended events
   * @returns {Promise<Array>} List of all events
   */
  async getSuspendedEvents() {
    return prisma.event.findMany({
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
      where: {
        eventStatus: {
          eventStatusID: '11111111-1111-1111-1111-111111111111'
        }
      }
    });
  },

  /**
   * Get approved events
   * @returns {Promise<Array>} List of all events
   */
  async getApprovedEvents() {
    return prisma.event.findMany({
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
      where: {
        eventStatus: {
          eventStatusID: '22222222-2222-2222-2222-222222222222'
        }
      }
    });
  },

  /**
 * Get all events
 * @returns {Promise<Array>} List of user events
 */
  async getUserEvents(userId) {
    return prisma.event.findMany({
      where: { userId },
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
    });
  },

  /**
   * Get a specific event by ID
   * @param {string} eventId - The ID of the event to fetch
   * @returns {Promise<Object|null>} The event object or null if not found
   */
  async getEventById(eventId) {
    return prisma.event.findUnique({
      where: { eventID: eventId },
      include: {
        eventStatus: true,
        addressEvents: {
          include: {
            routes: true,
          },
        },
      },
    });
  },

  /**
   * Create a new event
   * @param {Object} eventData - The data for creating the event
   * @returns {Promise<Object>} The created event object
   */
  async createEvent(eventData) {
    return prisma.event.create({
      data: eventData,
    });
  },

  /**
   * Update an event by ID
   * @param {string} eventId - The ID of the event to update
   * @param {Object} eventData - The updated event data
   * @returns {Promise<Object>} The updated event object
   */
  async updateEvent(eventId, eventData) {
    return prisma.event.update({
      where: { eventID: eventId },
      data: eventData,
    });
  },

  /**
   * Update the status of an event by ID
   * @param {string} eventId - The ID of the event to update
   * @param {string} newEventStatusId - The new event status ID to set
   * @returns {Promise<Object>} The updated event object
   */
  async updateEventStatus(eventId, newEventStatusId) {
    return prisma.event.update({
      where: { eventID: eventId },
      data: {
        eventstatus_id: newEventStatusId,
      },
    });
  },

  /**
   * Delete an event by ID
   * @param {string} eventId - The ID of the event to delete
   * @returns {Promise<void>} Resolves when the event is deleted
   */
  async deleteEvent(eventId) {
    await prisma.event.delete({
      where: { eventID: eventId },
    });
  },
};

export default eventService;
