package com.navigation.com.nb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gjiazhe.panoramaimageview.GyroscopeObserver
import com.gjiazhe.panoramaimageview.PanoramaImageView

class aryabhatta : AppCompatActivity() {
    private var gyroscopeObserver: GyroscopeObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aryabhatta)
        gyroscopeObserver = GyroscopeObserver()
        val panoramaImageView = findViewById<View>(R.id.panorama_image_view) as PanoramaImageView
        // Set the maximum radian the device should rotate to show image's bounds.
        // It should be set between 0 and π/2.
        // The default value is π/9.
        gyroscopeObserver!!.setMaxRotateRadian(Math.PI / 9)

        // Set GyroscopeObserver for PanoramaImageView.
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver)
    }

    override fun onResume() {
        super.onResume()
        // Register GyroscopeObserver.
        gyroscopeObserver!!.register(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister GyroscopeObserver.
        gyroscopeObserver!!.unregister()
    }
}
