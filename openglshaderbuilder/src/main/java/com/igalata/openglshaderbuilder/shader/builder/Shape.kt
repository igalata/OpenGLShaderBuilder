package com.igalata.openglshaderbuilder.shader.builder

import android.graphics.Bitmap
import android.opengl.GLES20.glGenTextures
import com.igalata.openglshaderbuilder.data.Vec4
import com.igalata.openglshaderbuilder.shader.shapes.*
import com.igalata.openglshaderbuilder.toTexture

/**
 * Created by irinagalata on 6/20/17.
 */
class Shape private constructor(val fragmentShader: String,
                                val bitmapUnit: Int?,
                                val height: Float,
                                val width: Float,
                                val color: Vec4,
                                val shapeType: ShapeType) {

    companion object {

        fun create(init: Builder.() -> Unit) = Builder(init).build()

        internal const val U_TEXTURE = "u_Texture"
        internal const val U_SIZE = "u_Size"
        internal const val U_COLOR = "u_Color"

        private var textureIds = IntArray(16)
        private var texturesCount = 0

    }

    class Builder private constructor() {

        private var bitmapUnit: Int? = null
        private var height = 0.5f
        private var width = 0.5f
        private var color: Vec4 = Vec4()
        private var shape: ShapeType = ShapeType.NONE

        // language=GLSL
        private val variablesDefinitions = """
            precision highp float;
            uniform vec2 u_Size;
            varying vec2 v_UV;
            const float PI = 3.1415926535897932384626433832795;
        """

        // language=GLSL
        private val textureDefinition = "uniform sampler2D u_Texture;" + '\n'

        // language=GLSL
        private val colorDefinition = "uniform vec4 u_Color;" + '\n'

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        /**
         * Sets the texture of an object. Cannot be used along with color
         */
        fun bitmap(init: Builder.() -> Bitmap) = apply { bitmapUnit = bindTexture(init()) }

        /**
         * Sets the height of an object. Please, note the coordinate system of an OpenGL view.
         * The left border of your screen is -1.0 * screenRatio, and the right one is 1.0 * screenRatio
         */
        fun height(init: Builder.() -> Float) = apply { height = init() }

        /**
         * Sets the width of an object. Please, note the coordinate system of an OpenGL view.
         * The left border of your screen is -1.0, and the right one is 1.0
         */
        fun width(init: Builder.() -> Float) = apply { width = init() }

        /**
         * Sets the color of an object. Cannot be used along with bitmap
         */
        fun color(init: Builder.() -> Vec4) = apply { color = init() }

        /**
         * Sets the shape of an object
         */
        fun shape(init: Builder.() -> ShapeType) = apply { shape = init() }

        internal fun build() = Shape(generateShader(), bitmapUnit, height, width,
                color, shape)

        private fun generateShader() = """
                $variablesDefinitions
                ${if (bitmapUnit != null) textureDefinition else ""}
                ${if (bitmapUnit == null) colorDefinition else ""}
                ${generateMain()}
            """

        private fun generateMain() = getShader(when (shape) {
            ShapeType.CIRCLE -> CircleShader
            ShapeType.RECTANGLE -> RectangleShader
            ShapeType.TRIANGLE -> TriangleShader
            ShapeType.NONE -> DefaultShader
        })

        private fun getShader(shapeShader: ShapeShader) = bitmapUnit?.let { shapeShader.withTexture } ?:
                shapeShader.withColor

        private fun bindTexture(bitmap: Bitmap): Int {
            if (texturesCount >= textureIds.size) {
                val temp = textureIds
                textureIds = IntArray(temp.size * 2)
                temp.forEachIndexed { index, i -> textureIds[index] = i }
            }
            bitmap.apply {
                glGenTextures(1, textureIds, texturesCount)
                toTexture(textureIds[texturesCount])
                texturesCount++
            }

            return textureIds[texturesCount - 1]
        }

    }

}