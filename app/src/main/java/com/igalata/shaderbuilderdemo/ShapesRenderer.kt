package com.igalata.shaderbuilderdemo

import android.content.Context
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import com.igalata.openglshaderbuilder.GLProgram
import com.igalata.openglshaderbuilder.data.Vec4
import com.igalata.openglshaderbuilder.rgba
import com.igalata.openglshaderbuilder.shader.builder.Transformation
import com.igalata.openglshaderbuilder.shader.builder.Shape
import com.igalata.openglshaderbuilder.shader.shapes.ShapeType
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by irinagalata on 7/24/17.
 */
class ShapesRenderer(val context: Context) : GLSurfaceView.Renderer {

    private var circle: Int = 0
    private var rectangle: Int = 0
    private var triangle: Int = 0

    private var scale = 1.0f
    private var translate = 0.0f
    private var rotate = 0.0f

    private var width: Int = 0
    private var height: Int = 0

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLProgram.background = R.color.colorPrimary.rgba(context)

        rectangle = GLProgram.createNewProgram {
            Shape.create {
                shape { ShapeType.RECTANGLE }
                height { 0.1f }
                width { 0.1f }
                color { R.color.red.rgba(context) }
            }
        }
        circle = GLProgram.createNewProgram {
            Shape.create {
                shape { ShapeType.CIRCLE }
                height { 0.1f }
                width { 0.1f }
                color { R.color.colorAccent.rgba(context) }
            }
        }
        triangle = GLProgram.createNewProgram {
            Shape.create {
                shape { ShapeType.TRIANGLE}
                height { 0.1f }
                width { 0.1f }
                color { android.R.color.white.rgba(context) }
            }
        }
    }

    override fun onDrawFrame(p0: GL10?) {
        GLProgram.onDrawFrame()

        scale += 0.01f
        translate += 0.001f
        rotate += 1f

        GLProgram.passVertexShaderData(rectangle) {
            Transformation.apply {
                scaleX { scale }
                scaleY { scale }
                translateX { translate }
                translateY { translate }
                rotateAngle { rotate }
                rotateY { 1f }
                rotateZ { 1f }
                rotateX { 1f }
                viewportHeight { height }
                viewportWidth { width }
                initialPosition { Vec4(0.3f) }
            }
        }

        GLProgram.passVertexShaderData(circle) {
            Transformation.apply {
                scaleX { scale }
                scaleY { scale }
                scaleZ { scale }
                translateX { translate }
                translateY { translate }
                rotateAngle { -rotate }
                rotateY { -1f }
                rotateZ { 1f }
                rotateX { 1f }
                viewportHeight { height }
                viewportWidth { width }
                initialPosition { Vec4(0.0f) }
            }
        }

        GLProgram.passVertexShaderData(triangle) {
            Transformation.apply {
                scaleX { scale }
                scaleY { scale }
                translateX { translate }
                translateY { translate }
                rotateAngle { -rotate }
                rotateY { 1f }
                rotateZ { -1f }
                rotateX { -1f }
                viewportHeight { height }
                viewportWidth { width }
                initialPosition { Vec4(-0.5f) }
            }
        }
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        glViewport(0, 0, width, height)
    }

}