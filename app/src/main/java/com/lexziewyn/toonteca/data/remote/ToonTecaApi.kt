package com.lexziewyn.toonteca.data.remote

import com.lexziewyn.toonteca.data.remote.dto.WeebcomicDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToonTecaApi {

    @GET("weebcomics")
    suspend fun getComics(): List<WeebcomicDto>

    @GET("weebcomics/{id}")
    suspend fun getComicById(
        @Path("id") id: Int
    ): WeebcomicDto

    @POST("weebcomics")
    suspend fun insertComic(
        @Body comic: WeebcomicDto
    ): WeebcomicDto

    @PUT("weebcomics/{id}")
    suspend fun updateComic(
        @Path("id") id: Int,
        @Body comic: WeebcomicDto
    ): WeebcomicDto

    @DELETE("weebcomics/{id}")
    suspend fun deleteComic(
        @Path("id") id: Int
    )
}