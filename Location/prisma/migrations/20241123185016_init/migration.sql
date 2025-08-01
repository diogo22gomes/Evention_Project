-- CreateTable
CREATE TABLE "Locations" (
    "locationId" UUID NOT NULL,
    "localtown" VARCHAR(100) NOT NULL,
    "city" VARCHAR(100) NOT NULL,

    CONSTRAINT "Locations_pkey" PRIMARY KEY ("locationId")
);
