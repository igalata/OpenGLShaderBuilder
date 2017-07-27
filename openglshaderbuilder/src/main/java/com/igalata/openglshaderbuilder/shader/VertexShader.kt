package com.igalata.openglshaderbuilder.shader

/**
 * Created by irinagalata on 6/19/17.
 */
object VertexShader {

    const val U_MATRIX = "u_Matrix"
    const val A_POSITION = "a_Position"
    const val A_UV = "a_UV"

    // language=GLSL
    val shader = """
        uniform mat4 u_Matrix;

        attribute vec4 a_Position;
        attribute vec2 a_UV;

        varying vec2 v_UV;

        void main()
        {
            gl_Position = u_Matrix * a_Position;
            v_UV = a_UV;
        }
    """

}