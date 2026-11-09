package com.lexziewyn.toonteca.data.remote

import com.lexziewyn.toonteca.data.remote.dto.AuthResponse
import com.lexziewyn.toonteca.data.remote.dto.LoginRequest
import com.lexziewyn.toonteca.data.remote.dto.RegisterRequest
import com.lexziewyn.toonteca.data.remote.dto.WeebcomicDto
import retrofit2.http.*

interface ToonTecaApi {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("weebcomics")
    suspend fun getComics(): List<WeebcomicDto>

    @GET("weebcomics/{id}")
    suspend fun getComicById(@Path("id") id: Int): WeebcomicDto

    @POST("weebcomics")
    suspend fun insertComic(@Body comic: WeebcomicDto): WeebcomicDto

    @PUT("weebcomics/{id}")
    suspend fun updateComic(@Path("id") id: Int, @Body comic: WeebcomicDto): WeebcomicDto

    @DELETE("weebcomics/{id}")
    suspend fun deleteComic(@Path("id") id: Int)
}