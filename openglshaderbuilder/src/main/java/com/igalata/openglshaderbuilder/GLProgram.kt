package com.igalata.openglshaderbuilder

import android.opengl.GLES20.*
import com.igalata.openglshaderbuilder.Const.FLOAT_SIZE
import com.igalata.openglshaderbuilder.data.Vec4
import com.igalata.openglshaderbuilder.shader.VertexShader
import com.igalata.openglshaderbuilder.shader.VertexShader.A_POSITION
import com.igalata.openglshaderbuilder.shader.VertexShader.A_UV
import com.igalata.openglshaderbuilder.shader.VertexShader.U_MATRIX
import com.igalata.openglshaderbuilder.shader.builder.Shape
import com.igalata.openglshaderbuilder.shader.builder.Transformation
import java.nio.FloatBuffer

/**
 * Created by irinagalata on 6/25/17.
 */
object GLProgram {

    var background: Vec4 = Vec4()
        set(value) {
            field = value
            glClearColor(background.x, background.y, background.z, background.w)
            enableTransparency()
        }

    private var currentProgramId = 0
    private val shapesMap = HashMap<Int, Shape>()
    private val textureVertices = Const.TEXTURE_VERTICES.toFloatBuffer()
    private val vertices = Const.VERTICES.toFloatBuffer()

    /**
     * Attaches fragment and vertex shaders for the specified shape
     * and links them to the new program
     *
     * @param shape a shape to be drawn
     * @return the compiled program
     */
    fun createNewProgram(shape: () -> Shape) = glCreateProgram().apply {
        val shapeObject = shape()

        glAttachShader(this, createShader(GL_VERTEX_SHADER, VertexShader.shader))
        glAttachShader(this, createShader(GL_FRAGMENT_SHADER, shapeObject.fragmentShader))
        glLinkProgram(this)
        shapesMap.put(this, shapeObject)
    }

    /**
     * Should be called on every frame render.
     * In fact, performs just cleaning of the previous frame
     */
    fun onDrawFrame() {
        glClear(GL_COLOR_BUFFER_BIT)
    }

    /**
     * Passes the specified position to the vertex shader of a program by programId.
     * Should be called on every frame update
     *
     * @see android.opengl.GLSurfaceView.Renderer.onDrawFrame
     * @param position the current position of a shape
     * @param programId the id of a program
     */
    fun passVertexShaderData(programId: Int, position: () -> Transformation) {
        useProgram(programId)
        drawShape(shapesMap[programId], position())
    }

    /**
     * Draws a shape with transformations applied
     */
    private fun drawShape(shape: Shape?, transformation: Transformation) {
        shape?.let {
            it.bitmapUnit?.let { unit ->
                glActiveTexture(GL_TEXTURE)
                glBindTexture(GL_TEXTURE_2D, unit)
                glUniform1i(glGetUniformLocation(currentProgramId, Shape.U_TEXTURE), 0)
            }

            glUniform2f(glGetUniformLocation(currentProgramId, Shape.U_SIZE), shape.width, shape.height)
            glUniform4f(glGetUniformLocation(currentProgramId, Shape.U_COLOR), shape.color.x, shape.color.y,
                    shape.color.z, shape.color.w)

            glUniformMatrix4fv(glGetUniformLocation(currentProgramId, U_MATRIX), 1, false, transformation.matrix, 0)
            vertices?.passToShader(currentProgramId, A_POSITION)
            textureVertices?.passToShader(currentProgramId, A_UV)

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        }
    }

    private fun useProgram(id: Int) {
        currentProgramId = id
        glUseProgram(currentProgramId)
    }

    /**
     * Creates a new shader from its source code
     *
     * @param type the type of a shader. Could be fragment or vertex
     * @param shader the source code of a shader
     */
    private fun createShader(type: Int, shader: String) = glCreateShader(type).apply {
        glShaderSource(this, shader)
        glCompileShader(this)
    }

    /**
     * An extension function to pass float array to the specified variable of the vertex shader
     *
     * @param programId the identifier of the program containing needed shader
     * @param name the name of the variable
     */
    private fun FloatBuffer.passToShader(programId: Int, name: String) {
        position(0)
        glGetAttribLocation(programId, name).let {
            glVertexAttribPointer(it, 2, GL_FLOAT, false, 2 * FLOAT_SIZE, this)
            glEnableVertexAttribArray(it)
        }
    }

    private fun enableTransparency() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

}