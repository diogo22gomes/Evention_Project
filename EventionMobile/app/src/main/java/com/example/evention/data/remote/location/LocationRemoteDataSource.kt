package com.example.evention.data.remote.location

import com.example.evention.model.Location

class LocationRemoteDataSource(private val api: LocationApiService) {

    suspend fun getLocationByLocaltown(locality: String): Location = api.getLocationByLocaltown(locality)

    suspend fun getLocationById(locationId: String): Location = api.getLocationById(locationId)
}