import locationService from '../services/locationService.js';

const locationController = {
  /**
   * Get all locations
   * @route {GET} /locations
   * @returns {Array} List of locations
   */
  async getAllLocations(req, res) {
    try {
      const locations = await locationService.getAllLocations();

      if (!locations || locations.length === 0) {
        return res.status(404).json({ message: 'No locations found' });
      }

      res.status(200).json(locations);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching locations' });
    }
  },

  /**
   * Get a specific location by ID
   * @route {GET} /locations/:id
   * @param {string} id - The ID of the location
   * @returns {Object} The location object
   */
  async getLocationById(req, res) {
    try {
      const locationId = req.params.id;
      const location = await locationService.getLocationById(locationId);

      if (!location) {
        return res.status(404).json({ message: 'Location not found' });
      }

      res.status(200).json(location);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching location' });
    }
  },

  /**
   * Create a new location
   * @route {POST} /locations
   * @bodyparam {Object} locationData - The data for creating a location
   * @returns {Object} The newly created location
   */
  async createLocation(req, res) {
    try {
      const { localtown, city } = req.body;

      if (!localtown || !city) {
        return res.status(400).json({ message: 'Missing required fields: localtown, city' });
      }

      const newLocation = await locationService.createLocation(req.body);
      res.status(201).json(newLocation);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error creating location' });
    }
  },

  /**
   * Update an existing location
   * @route {PUT} /locations/:id
   * @param {string} id - The ID of the location to update
   * @bodyparam {Object} locationData - The new data for the location
   * @returns {Object} The updated location
   */
  async updateLocation(req, res) {
    try {
      const locationId = req.params.id;
      const locationData = req.body;

      const location = await locationService.getLocationById(locationId);
      if (!location) {
        return res.status(404).json({ message: 'Location not found' });
      }

      const updatedLocation = await locationService.updateLocation(locationId, locationData);
      res.status(200).json(updatedLocation);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error updating location' });
    }
  },

  /**
   * Delete a location
   * @route {DELETE} /locations/:id
   * @param {string} id - The ID of the location to delete
   * @returns {Object} Success message or error
   */
  async deleteLocation(req, res) {
    try {
      const locationId = req.params.id;

      const location = await locationService.getLocationById(locationId);
      if (!location) {
        return res.status(404).json({ message: 'Location not found' });
      }

      await locationService.deleteLocation(locationId);
      res.status(204).send();
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error deleting location' });
    }
  },

  /**
   * Get a specific location by localtown
   * @route {GET} /location/localtown/:localtown
   * @param {string} localtown - The name of the localtown
   * @returns {Object} The location object
   */
  async getLocationByLocaltown(req, res) {
    try {
      const localtown = req.params.localtown;
      const location = await locationService.getLocationByLocaltown(localtown);

      if (!location) {
        return res.status(404).json({ message: 'Location not found' });
      }

      res.status(200).json(location);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching location' });
    }
  }

};



export default locationController;
