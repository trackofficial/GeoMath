package com.example.geomath

import org.rajawali3d.Object3D
import javax.microedition.khronos.opengles.GL10

class WireframeCube(size: Float) : Object3D() {//новый куб, состоящий только из ребер
    init {
        val half = size / 2
        val vertices = floatArrayOf(
            -half, -half, -half,  // 0
            half, -half, -half,  // 1
            half,  half, -half,  // 2
            -half,  half, -half,  // 3
            -half, -half,  half,  // 4
            half, -half,  half,  // 5
            half,  half,  half,  // 6
            -half,  half,  half   // 7
        )

        val indices = intArrayOf(
            0,1, 1,2, 2,3, 3,0,  // задняя грань
            4,5, 5,6, 6,7, 7,4,  // передняя грань
            0,4, 1,5, 2,6, 3,7   // слияние
        )

        setData(vertices, null, null, null, indices, true)
        drawingMode = GL10.GL_LINES
    }
}