package com.example.examensegundoparcial.data.network

import com.example.examensegundoparcial.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}