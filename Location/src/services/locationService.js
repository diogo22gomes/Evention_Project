import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

const locationService = {
  /**
   * Get all locations
   * @returns {Promise<Array>} List of all locations
   */
  async getAllLocations() {
    return prisma.location.findMany();
  },

  /**
   * Get a specific location by ID
   * @param {string} locationId - The ID of the location to fetch
   * @returns {Promise<Object|null>} The location object or null if not found
   */
  async getLocationById(locationId) {
    return prisma.location.findUnique({
      where: { locationId: locationId },
    });
  },

  /**
   * Create a new location
   * @param {Object} locationData - The data for creating the location
   * @returns {Promise<Object>} The created location object
   */
  async createLocation(locationData) {
    return prisma.location.create({
      data: locationData,
    });
  },

  /**
   * Update a location by ID
   * @param {string} locationId - The ID of the location to update
   * @param {Object} locationData - The updated location data
   * @returns {Promise<Object>} The updated location object
   */
  async updateLocation(locationId, locationData) {
    return prisma.location.update({
      where: { locationId: locationId },
      data: locationData,
    });
  },

  /**
   * Delete a location by ID
   * @param {string} locationId - The ID of the location to delete
   * @returns {Promise<void>} Resolves when the location is deleted
   */
  async deleteLocation(locationId) {
    await prisma.location.delete({
      where: { locationId: locationId },
    });
  },

  /**
   * Get a specific location by localtown
   * @param {string} localtown - The name of the localtown to fetch
   * @returns {Promise<Object|null>} The location object or null if not found
   */
  async getLocationByLocaltown(localtown) {
    return prisma.location.findFirst({
      where: { localtown: localtown },
    });
  }

};

export default locationService;
