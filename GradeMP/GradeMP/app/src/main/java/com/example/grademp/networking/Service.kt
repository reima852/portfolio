package com.example.grademp.networking

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * This file contains the network services that can be called by viewModels.
 */

//Retrofit and moshi configuration
object MetropoliaNetworkFetcher {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://users.metropolia.fi/~peterh/")
        .build()

    val service: MetropoliaService by lazy {
        retrofit.create(MetropoliaService::class.java)
    }
}

//Service for retrofit to fetch the list of all MP's data
interface MetropoliaService {
    @GET("mps.json")
    suspend fun getMPs(): List<NetworkMemberOfFinnishParliament>
}

