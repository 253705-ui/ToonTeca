package com.lexziewyn.toonteca.domain.repository

import com.lexziewyn.toonteca.domain.model.Weebcomic

interface WeebcomicRepository {
    suspend fun getComics(): List<Weebcomic>
    suspend fun getComicById(id: Int): Weebcomic
    suspend fun insertComic(comic: Weebcomic): Weebcomic
    suspend fun updateComic(id: Int, comic: Weebcomic): Weebcomic
    suspend fun deleteComic(id: Int)
}