import { Router } from "express";
import locationController from "../controllers/locationController.js";

const router = Router();

router.get('/location', locationController.getAllLocations);
router.post('/location', locationController.createLocation);
router.get('/location/:id', locationController.getLocationById);
router.put('/location/:id', locationController.updateLocation);
router.delete('/location/:id', locationController.deleteLocation);
router.get('/location/localtown/:localtown', locationController.getLocationByLocaltown);


export default router;