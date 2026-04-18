package com.example.geomath

import android.content.Context
import android.opengl.GLES20
import android.view.MotionEvent
import org.rajawali3d.lights.DirectionalLight
import org.rajawali3d.materials.Material
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.Cube
import org.rajawali3d.renderer.Renderer

class GrayRenderer(context: Context) : Renderer(context) {
    private var previousDistance = 0f
    private var cameraAngleX = 0.0
    private var cameraAngleY = 0.5
    private var cameraRadius = 8.0
    private var lastX = 0f
    private var lastY = 0f

    override fun initScene() {//Управление сценой
        GLES20.glClearColor(0.3f, 0.3f, 0.35f, 1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        val light = DirectionalLight()//Свет
        light.position = Vector3(1.0, 1.0, 1.0)
        currentScene.addLight(light)

        val cube = WireframeCube(1.0f)//Новый куб, только с ребрами
        currentScene.addChild(cube)
        val material = Material()
        material.setColor(0xff0000)
        cube.material = material

        currentCamera.position = Vector3(0.0, 2.0, 8.0)
        currentCamera.setLookAt(0.0, 0.0, 0.0)
    }
    override fun onRender(ellapsedRealtime: Long, deltaTime: Double) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        super.onRender(ellapsedRealtime, deltaTime)

        val x = cameraRadius * Math.sin(cameraAngleX) * Math.cos(cameraAngleY)//
        val z = cameraRadius * Math.cos(cameraAngleX) * Math.cos(cameraAngleY)
        val y = cameraRadius * Math.sin(cameraAngleY)
        currentCamera.position = Vector3(x, y, z)
        currentCamera.setLookAt(0.0, 0.0, 0.0)
    }
    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) {}

    override fun onTouchEvent(event: MotionEvent?) {//Управление камерой
        when(event?.actionMasked) {

            MotionEvent.ACTION_POINTER_DOWN -> {
                previousDistance = getDistance(event)
            }
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1) {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY
                    cameraAngleX -= deltaX * 0.0015
                    cameraAngleY -= deltaY * 0.0015
                    lastX = event.x
                    lastY = event.y
                }
                else if (event.pointerCount == 2) {
                    val newDistance = getDistance(event)
                    val delta = newDistance - previousDistance
                    cameraRadius = (cameraRadius - delta * 0.05).coerceIn(2.0, 20.0)
                    previousDistance = newDistance
                }
            }
        }
    }
    private fun getDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }
}