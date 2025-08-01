import { prisma } from '../src/prismaClient.js';

const userInEventsData = [
  {
    ticketID: '5a1f9d3e-b12d-42fd-87f3-9f8f7c6c1a01', 
    user_id: '12345678-abcd-1234-ef00-1234567890ab', 
    event_id: '98765432-4321-dcba-0987-abcdef123456', 
    participated: true,
    createdAt: new Date(),
  },
  {
    ticketID: '6b2f1e4f-c23e-53fe-98f4-af9e8d7e2b12', 
    user_id: '23456789-cdef-2345-ab01-2345678901bc', 
    event_id: '87654321-1234-cdef-1234-abcdef987654',
    participated: false,
    createdAt: new Date(),
  },
];

const feedbacksData = [
  {
    feedbackID: 'a1b2c3d4-e5f6-1234-5678-9abcdef01234',
    rating: 5,
    commentary: 'Amazing event, very well organized!',
  },
  {
    feedbackID: 'b2c3d4e5-f6a7-2345-6789-abcdef123456',
    rating: 3,
    commentary: 'It was okay, but the location was hard to reach.',
  },
];

const seedDatabase = async () => {
  try {
    console.log('🌱 Starting seeding process...');

    // Seed Feedbacks
    for (const feedback of feedbacksData) {
      const createdFeedback = await prisma.feedback.create({
        data: feedback,
      });
      console.log(`✅ Created feedback with ID: ${createdFeedback.feedbackID}`);
    }

    // Seed UserInEvents
    for (const userInEvent of userInEventsData) {
      const createdUserInEvent = await prisma.userInEvent.create({
        data: userInEvent,
      });
      console.log(`✅ Created UserInEvent with ticket ID: ${createdUserInEvent.ticketID}`);
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
