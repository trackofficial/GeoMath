    package com.example.geomath

    import android.content.Context
    import android.opengl.GLES20
    import android.view.MotionEvent
    import org.rajawali3d.lights.DirectionalLight
    import org.rajawali3d.materials.Material
    import org.rajawali3d.math.vector.Vector3
    import org.rajawali3d.primitives.Cube
    import org.rajawali3d.renderer.Renderer
    import javax.microedition.khronos.opengles.GL10

    //TODO: Сделать движение камеры
    //TODO: Изменить фон
    //TODO: Добавить отображение векторов

    class GrayRenderer(context: Context) : Renderer(context) {

        override fun initScene() {//Рабoта сцены
            GLES20.glClearColor(0.3f, 0.3f, 0.35f, 1.0f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)// Устновил увет

            val light = DirectionalLight()//Устновил свет и камеру
            light.position = Vector3(1.0, 1.0, 1.0)
            currentScene.addLight(light)
            currentCamera.position = Vector3(5.0, 5.0, 10.0)
            currentCamera.setLookAt(0.0, 0.0, 0.0)

            val cube = Cube(1.0f)//добавил куб
            val material = Material()
            material.setColor(0xff0000)
            cube.material = material
            currentScene.addChild(cube)
        }

        override fun onRender(ellapsedRealtime: Long, deltaTime: Double) {

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            super.onRender(ellapsedRealtime, deltaTime)
            currentCamera.rotateAround(Vector3(0.0, 0.0, 0.0), 0.5)
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {

        }

        override fun onTouchEvent(event: MotionEvent?) {
            when(event?.action) {
                MotionEvent.ACTION_MOVE -> {
                    currentCamera.rotateAround(Vector3(1.0, 0.0, 0.0), event.y * 0.5)
                    currentCamera.rotateAround(Vector3(0.0, 1.0, 0.0), event.x * 0.5)
                }
            }
        }

    }