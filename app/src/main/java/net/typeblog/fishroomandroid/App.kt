package net.typeblog.fishroomandroid

import android.app.Application
import com.karumi.dexter.Dexter

/**
 * Created by peter on 6/14/16.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Dexter.initialize(this)
    }
}