package com.example.geomath

import org.rajawali3d.Object3D
import javax.microedition.khronos.opengles.GL10

class WireframePlane(width: Float, height: Float) : Object3D() {
    init {
        val halfW = width / 2
        val halfH = height / 2

        val vertices = floatArrayOf(
            -halfW, 0f, -halfH,  // 0 - лево-низ
            halfW, 0f, -halfH,  // 1 - право-низ
            halfW, 0f,  halfH,  // 2 - право-верх
            -halfW, 0f,  halfH   // 3 - лево-верх
        )

        val indices = intArrayOf(
            0,1, 1,2, 2,3, 3,0   // четыре ребра
        )

        setData(vertices, null, null, null, indices, true)
        drawingMode = GL10.GL_LINES
    }
}