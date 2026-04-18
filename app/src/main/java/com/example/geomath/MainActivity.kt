package com.example.geomath

import android.os.Bundle
import androidx.activity.ComponentActivity
import org.rajawali3d.view.TextureView
import org.rajawali3d.view.SurfaceView
class MainActivity : ComponentActivity() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var renderer: GrayRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceView = SurfaceView(this)
        renderer = GrayRenderer(this)

        surfaceView.setSurfaceRenderer(renderer)
        setContentView(surfaceView)
        surfaceView.requestFocus()
        surfaceView.setSystemUiVisibility(
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )
        surfaceView.setOnTouchListener { _, event ->
            renderer.onTouchEvent(event)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }
}