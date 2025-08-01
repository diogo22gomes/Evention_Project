package com.example.evention.data.remote.location

import com.example.evention.model.Location
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationApiService {

    @GET("/location/api/location/localtown/{locality}")
    suspend fun getLocationByLocaltown(@Path("locality")locality: String): Location

    @GET("/location/api/location/{locationId}")
    suspend fun getLocationById(@Path("locationId")locationId: String): Location
}