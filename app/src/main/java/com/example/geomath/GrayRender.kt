package com.example.geomath

import android.content.Context
import android.opengl.GLES20
import android.view.MotionEvent
import org.rajawali3d.lights.DirectionalLight
import org.rajawali3d.materials.Material
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.Plane
import org.rajawali3d.renderer.Renderer

//TODO: Сделать нормальное отображение стен и пола пространства
//TODO: Поменять цвет фона
//TODO: Подумать, как реальзовать сетку для граней (словно лист в клетку)
//TODO: Сделать окно для добавления объекта
//TODO: Поумать над тем, как сделать так, чтобы можно было добавлять точки, ребра и т.д

class GrayRenderer(context: Context) : Renderer(context) {
    private var previousDistance = 0f
    private var cameraAngleX = 0.0
    private var cameraAngleY = 0.5
    private var cameraRadius = 8.0
    private var lastX = 0f
    private var lastY = 0f

    override fun initScene() {//Управление сценой
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDisable(GLES20.GL_BLEND)

        val light = DirectionalLight()//Свет
        light.position = Vector3(1.0, 1.0, 1.0)
        currentScene.addLight(light)

        val cube = WireframeCube(1.0f)//Новый куб, только с ребрами
        currentScene.addChild(cube)
        val material = Material()
        material.setColor(0xff0000)
        cube.material = material
        cube.position = Vector3(0.0, -1.5, 0.0)

        // Пол
        val floor = Plane(30f, 30f, 1, 1)
        floor.rotate(Vector3(1.0, 0.0, 0.0), -90.0)
        floor.position = Vector3(0.0, -2.0, 0.0)
        val floorMaterial = Material()
        floorMaterial.setColor(0xc4c4c4)
        floor.material = floorMaterial
        currentScene.addChild(floor)
        floor.isDoubleSided = true
}

    override fun onRender(ellapsedRealtime: Long, deltaTime: Double) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_STENCIL_BUFFER_BIT)
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