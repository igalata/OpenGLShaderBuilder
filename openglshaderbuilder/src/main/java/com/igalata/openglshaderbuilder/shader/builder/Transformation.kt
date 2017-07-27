package com.igalata.openglshaderbuilder.shader.builder

import android.opengl.Matrix
import com.igalata.openglshaderbuilder.data.Vec4

/**
 * Created by irinagalata on 6/25/17.
 */
class Transformation private constructor(val matrix: FloatArray) {

    companion object {
        fun apply(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor() {

        private var scaleX = 1f
        private var scaleY = 1f
        private var scaleZ = 1f

        private var rotateX = 0f
        private var rotateY = 0f
        private var rotateZ = 0f
        private var rotateAngle = 0f

        private var translateX = 0f
        private var translateY = 0f
        private var translateZ = 0f

        private var viewportWidth = 1f
        private var viewportHeight = 1f
        private var aspectRatio = 0f

        private var initialPosition: Vec4? = null

        private val modelMatrix = FloatArray(16)
        private val projectionMatrix = FloatArray(16)

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        /**
         * Scales an object by specified number in X direction
         */
        fun scaleX(init: Builder.() -> Float) = apply { scaleX = init() }

        /**
         * Scales an object by specified number in Y direction
         */
        fun scaleY(init: Builder.() -> Float) = apply { scaleY = init() }

        /**
         * Scales an object by specified number in Z direction
         */
        fun scaleZ(init: Builder.() -> Float) = apply { scaleZ = init() }

        /**
         * Sets the X component of the rotation axis
         */
        fun rotateX(init: Builder.() -> Float) = apply { rotateX = init() }

        /**
         * Sets the Y component of the rotation axis
         */
        fun rotateY(init: Builder.() -> Float) = apply { rotateY = init() }

        /**
         * Sets the Z component of the rotation axis
         */
        fun rotateZ(init: Builder.() -> Float) = apply { rotateZ = init() }

        /**
         * Rotates an object by specified angle in degrees
         */
        fun rotateAngle(init: Builder.() -> Float) = apply { rotateAngle = init() }

        /**
         * Translates an object of specified units in X direction
         */
        fun translateX(init: Builder.() -> Float) = apply { translateX = init() }

        /**
         * Translates an object of specified units in Y direction
         */
        fun translateY(init: Builder.() -> Float) = apply { translateY = init() }

        /**
         * Translates an object of specified units in Z direction
         */
        fun translateZ(init: Builder.() -> Float) = apply { translateZ = init() }

        /**
         * Should be called in the onSurfaceChanged() method of your renderer to pass the height of a viewport.
         * Is required to avoid screen aspect ratio issues
         */
        fun viewportHeight(init: Builder.() -> Int) = apply { viewportHeight = init().toFloat() }

        /**
         * Should be called in the onSurfaceChanged() method of your renderer to pass the width of a viewport.
         * Is required to avoid screen aspect ratio issues
         */
        fun viewportWidth(init: Builder.() -> Int) = apply { viewportWidth = init().toFloat() }

        /**
         * Sets an object in specific position
         */
        fun initialPosition(init: Builder.() -> Vec4) = apply { initialPosition = init() }

        internal fun build() = Transformation(FloatArray(16).apply {
            projectionMatrix.calculateProjectionMatrix()
            modelMatrix.calculateModelMatrix()

            Matrix.multiplyMM(this, 0, projectionMatrix, 0, modelMatrix, 0)
        })

        private fun FloatArray.calculateProjectionMatrix() {
            aspectRatio = when (viewportWidth > viewportHeight) {
                true -> viewportWidth / viewportHeight
                false -> viewportHeight / viewportWidth
            }

            if (viewportWidth > viewportHeight) {
                Matrix.orthoM(this, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
            } else {
                Matrix.orthoM(this, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
            }
        }

        private fun FloatArray.calculateModelMatrix() {
            Matrix.setIdentityM(this, 0)

            Matrix.translateM(this, 0, initialPosition?.x ?: 0f, initialPosition?.y ?: 0f,
                    initialPosition?.z ?: 0f)
            Matrix.translateM(this, 0, translateX, translateY, translateZ)
            if (rotateAngle != 0f) Matrix.rotateM(modelMatrix, 0, rotateAngle, rotateX, rotateY, rotateZ)
            Matrix.scaleM(this, 0, scaleX, scaleY, scaleY)
        }

    }

}