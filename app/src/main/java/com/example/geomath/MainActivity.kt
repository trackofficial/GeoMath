package com.example.geomath

import android.os.Bundle
import android.view.Surface
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import com.google.android.filament.*
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper

class MainActivity : ComponentActivity() {

    // CRITICAL: Initialize Filament native libraries
    companion object {
        init {
            System.loadLibrary("filament-jni")
            Filament.init()
        }
    }

    private lateinit var surfaceView: SurfaceView
    private lateinit var uiHelper: UiHelper
    private lateinit var displayHelper: DisplayHelper

    private lateinit var engine: Engine
    private lateinit var renderer: Renderer
    private lateinit var scene: Scene
    private lateinit var view: View
    private lateinit var camera: Camera
    private var swapChain: SwapChain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        surfaceView = SurfaceView(this)
        setContentView(surfaceView)
        setupFilament()
        setupSurfaceView()
    }
    private fun setupSurfaceView() {
        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
        uiHelper.renderCallback = SurfaceCallback()
        uiHelper.attachTo(surfaceView)
    }
    private fun setupFilament() {
        engine = Engine.Builder()
            .featureLevel(Engine.FeatureLevel.FEATURE_LEVEL_0)
            .build()
        renderer = engine.createRenderer()
        scene = engine.createScene()
        view = engine.createView()
        camera = engine.createCamera(engine.entityManager.create())
        displayHelper = DisplayHelper(this)
        view.camera = camera
        view.scene = scene
    }

    inner class SurfaceCallback : UiHelper.RendererCallback {
        override fun onNativeWindowChanged(surface: Surface) {
            swapChain?.let { engine.destroySwapChain(it) }

            var flags = uiHelper.swapChainFlags
            if (engine.activeFeatureLevel == Engine.FeatureLevel.FEATURE_LEVEL_0) {
                if (SwapChain.isSRGBSwapChainSupported(engine)) {
                    flags = flags or SwapChainFlags.CONFIG_SRGB_COLORSPACE
                }
            }

            swapChain = engine.createSwapChain(surface, flags)
            displayHelper.attach(renderer, surfaceView.display)
        }

        override fun onDetachedFromSurface() {
            displayHelper.detach()
            swapChain?.let {
                engine.destroySwapChain(it)
                engine.flushAndWait()
                swapChain = null
            }
        }

        override fun onResized(width: Int, height: Int) {
            view.viewport = Viewport(0, 0, width, height)

            val aspect = width.toDouble() / height.toDouble()
            camera.setProjection(45.0, aspect, 0.1, 100.0, Camera.Fov.VERTICAL)

            engine.flushAndWait()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiHelper.detach()
        swapChain?.let { engine.destroySwapChain(it) }

        if (::engine.isInitialized) {
            engine.destroyView(view)
            engine.destroyScene(scene)
            engine.destroyRenderer(renderer)
            engine.destroyEntity(camera.entity)
            engine.destroy()
        }
    }
}