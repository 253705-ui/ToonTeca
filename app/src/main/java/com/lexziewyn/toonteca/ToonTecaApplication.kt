package com.lexziewyn.toonteca

import android.app.Application
import com.lexziewyn.toonteca.data.local.SessionManager
import com.lexziewyn.toonteca.data.remote.RetrofitInstance
import com.lexziewyn.toonteca.data.repository.WeebcomicRepositoryImpl
import com.lexziewyn.toonteca.domain.repository.WeebcomicRepository

interface AppContainer {
    val weebcomicRepository: WeebcomicRepository
    val sessionManager: SessionManager
}

class DefaultAppContainer(private val applicationContext: Application) : AppContainer {
    override val sessionManager: SessionManager by lazy {
        SessionManager(applicationContext)
    }

    override val weebcomicRepository: WeebcomicRepository by lazy {
        WeebcomicRepositoryImpl(RetrofitInstance.api)
    }
}

class ToonTecaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        RetrofitInstance.token = container.sessionManager.getToken() ?: ""
    }
}
