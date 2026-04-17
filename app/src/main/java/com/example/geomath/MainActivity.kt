package com.example.geomath

import android.os.Bundle
import androidx.activity.ComponentActivity
import org.rajawali3d.view.TextureView

class MainActivity : ComponentActivity() {

    private lateinit var surfaceView: TextureView
    private lateinit var renderer: GrayRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceView = TextureView(this)
        renderer = GrayRenderer(this)
        surfaceView.setSurfaceRenderer(renderer)
        setContentView(surfaceView)
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