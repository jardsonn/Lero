package com.jalloft.lero

import android.app.Application
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LeroApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

}