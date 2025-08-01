import { prisma } from '../src/prismaClient.js';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { v4 as uuidv4 } from 'uuid';

// Suporte a __dirname em ES Modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const loadLocationsFromFile = () => {
  return new Promise((resolve, reject) => {
    const filePath = path.join(__dirname, 'freguesias-metadata.json');

    fs.readFile(filePath, 'utf8', (err, data) => {
      if (err) {
        return reject(err);
      }
      try {
        const parsedData = JSON.parse(data);
        resolve(parsedData.d);
      } catch (parseError) {
        reject(parseError);
      }
    });
  });
};

const seedDatabase = async () => {
  try {
    console.log('🌱 Starting seeding process...');

    const locationsData = await loadLocationsFromFile();

    const formattedLocations = locationsData.map(location => ({
      locationId: uuidv4(),
      localtown: location.concelho,
      city: location.freguesia,
    }));

    for (const location of formattedLocations) {
      await prisma.location.create({
        data: location,
      });
    }

    console.log('🌱 Seeding completed successfully!');
  } catch (error) {
    console.error('❌ Error during seeding:', error);
    process.exit(1);
  } finally {
    await prisma.$disconnect();
  }
};

seedDatabase();
