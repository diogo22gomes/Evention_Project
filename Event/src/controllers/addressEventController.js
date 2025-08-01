import addressEventService from '../services/addressEventService.js';
import axios from 'axios';
import routesEventService from '../services/routesEventService.js';
import path from 'path';
import { fileURLToPath } from 'url';
import fs from 'fs';
import https from 'https';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const loadErrorMessages = (lang) => {
  const errorMessagesPath = path.join(__dirname, '../config', 'errorMessages.json');
  const errorMessages = JSON.parse(fs.readFileSync(errorMessagesPath, 'utf-8'));
  const languageCode = lang.split(',')[0].split('-')[0];

  return errorMessages[languageCode] || errorMessages.en;
};

const addressEventController = {
  /**
     * Get all address events
     * @route {GET} /address-events
     * @returns {Array} List of address events
     * @description Fetches all address events from the database.
     * If no address events are found, it returns a 404 response.
     */
  async getAllAddressEvents(req, res) {
    const lang = req.headers['accept-language'] || 'en';
    const errorMessages = loadErrorMessages(lang);
    try {
      const addressEvents = await addressEventService.getAllAddressEvents();

      res.status(200).json(addressEvents);

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: errorMessages.ERROR_FETCHING_ADDRESS_EVENTS });
    }
  },

  /**
   * Get a specific address event by ID 
   * @route {GET} /address-events/:id
   * @param {string} id - The ID of the address event
   * @returns {Object} The address event object
   * @description Fetches a specific address event by its ID.
   * If the address event is not found, it returns a 404 response.
   */
  async getAddressEventById(req, res) {
    const lang = req.headers['accept-language'] || 'en';
    const errorMessages = loadErrorMessages(lang);
    try {
      const addressEventId = req.params.id;
      const addressEvent = await addressEventService.getAddressEventById(addressEventId);

      if (!addressEvent) {
        return res.status(404).json({ message: errorMessages.ADDRESS_EVENT_NOT_FOUND });
      }

      res.status(200).json(addressEvent);

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: errorMessages.ERROR_FETCHING_ADDRESS_EVENTS });
    }
  },

  /**
   * Create a new address event
   * @route {POST} /address-events
   * @bodyparam {Object} addressData - The data for creating an address event
   * @param {string} addressData.road - The road name of the address event
   * @param {string} addressData.roadNumber - The road number of the address event
   * @param {string} addressData.postCode - The postal code of the address event
   * @param {string} addressData.localtown - The local town for the address event
   * @param {string} addressData.event_id - The ID of the related event
   * @returns {Object} The newly created address event
   * @description Creates a new address event with the provided data.
   * If any required field is missing, it returns a 400 response.
   * Additionally, it checks if the provided `localtown` exists through an external service.
   */
  async createAddressEvent(req, res) {
    const lang = req.headers['accept-language'] || 'en';
    const errorMessages = loadErrorMessages(lang);
    try {
      const { road, roadNumber, postCode, localtown, event_id } = req.body;

      if (!road || typeof roadNumber !== 'number' || !postCode || !localtown || !event_id) {
        return res.status(400).json({ message: errorMessages.MISSING_REQUIRED_FIELDS });
      }

      // Configuração para ignorar certificados autoassinados (apenas para desenvolvimento)
      const agent = new https.Agent({ rejectUnauthorized: false });

      // const localtownExists = await axios.get(`http://locationservice:5005/api/location/${localtown}`);
      //const localtownExists = await axios.get(`http://nginx-api-gateway:5010/location/api/location/${localtown}`);
      const localtownExists = await axios.get(`https://nginx-api-gateway:5010/location/api/location/${localtown}`, { httpsAgent: agent });  //https api gateway

      if (!localtownExists) {
        return res.status(404).json({ message: errorMessages.LOCATION_NOT_FOUND });
      }

      const newAddressEvent = await addressEventService.createAddressEvent(req.body);
      res.status(201).json(newAddressEvent);

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: errorMessages.ERROR_CREATING_ADDRESS_EVENT });
    }
  },

  /**
   * Update an existing address event
   * @route {PUT} /address-events/:id
   * @param {string} id - The ID of the address event to update
   * @bodyparam {Object} addressData - The new data for the address event
   * @returns {Object} The updated address event
   * @description Updates an existing address event with the provided data.
   * If the address event is not found, it returns a 404 response.
   */
  async updateAddressEvent(req, res) {
    const lang = req.headers['accept-language'] || 'en';
    const errorMessages = loadErrorMessages(lang);
    try {
      const addressEventId = req.params.id;
      const addressData = req.body;

      const addressEvent = await addressEventService.getAddressEventById(addressEventId);
      if (!addressEvent) {
        return res.status(404).json({ message: errorMessages.ADDRESS_EVENT_NOT_FOUND });
      }

      const updatedAddressEvent = await addressEventService.updateAddressEvent(addressEventId, addressData);
      res.status(200).json(updatedAddressEvent);

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: errorMessages.ERROR_UPDATING_ADDRESS_EVENT });
    }
  },

  /**
   * Delete an address event
   * @route {DELETE} /address-events/:id
   * @param {string} id - The ID of the address event to delete
   * @returns {Object} Success message or error
   * @description Deletes an address event by its ID.
   * If the address event is not found, it returns a 404 response.
   */
  async deleteAddressEvent(req, res) {
    const lang = req.headers['accept-language'] || 'en';
    const errorMessages = loadErrorMessages(lang);
    try {
      const addressEventId = req.params.id;

      const addressEvent = await addressEventService.getAddressEventById(addressEventId);
      if (!addressEvent) {
        return res.status(404).json({ message: errorMessages.ADDRESS_EVENT_NOT_FOUND });
      }

      const addresRoutes = await routesEventService.getRoutesEventByAddressId(addressEventId);

      if (addresRoutes) {
        for (const route of addresRoutes) {
          await routesEventService.deleteRoutesEvent(route.routeID);
        }
      }

      await addressEventService.deleteAddressEvent(addressEventId);
      res.status(204).send();

    } catch (error) {
      console.error(error);
      res.status(500).json({ message: errorMessages.ERROR_DELETING_ADDRESS_EVENT });
    }
  }
};

export default addressEventController;
