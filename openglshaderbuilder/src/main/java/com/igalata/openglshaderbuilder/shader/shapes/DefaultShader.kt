package com.igalata.openglshaderbuilder.shader.shapes

/**
 * Created by irinagalata on 7/24/17.
 */
internal object DefaultShader : ShapeShader {

    //language=GLSL
    override val withTexture = """
        void main() {
            gl_FragColor = texture2D(u_Texture, v_UV);
        }
    """

    //language=GLSL
    override val withColor = """
        void main() {
            gl_FragColor = u_Color;
        }
    """

}