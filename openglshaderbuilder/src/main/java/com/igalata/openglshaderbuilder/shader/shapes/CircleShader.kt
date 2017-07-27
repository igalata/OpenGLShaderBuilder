package com.igalata.openglshaderbuilder.shader.shapes

/**
 * Created by irinagalata on 7/23/17.
 */
internal object CircleShader : ShapeShader {

    //language=GLSL
    override val withColor = """
        void main() {
            float distance = distance(vec2(0.5, 0.5), v_UV);
            gl_FragColor = mix(u_Color, vec4(0.0), smoothstep(u_Size.x / 2.0 - 0.0005, u_Size.x / 2.0, distance));
        }
    """

    //language=GLSL
    override val withTexture = """
        void main() {
            float distance = distance(vec2(0.5, 0.5), v_UV);
            float positionX = ((vec2(0.5, 0.5) - v_UV + u_Size.x / 2.0) / u_Size.x).x;
            float positionY = ((vec2(0.5, 0.5) - v_UV + u_Size.y / 2.0) / u_Size.y).y;
            gl_FragColor = mix(texture2D(u_Texture, vec2(positionX, positionY)), vec4(0.0),
            smoothstep(u_Size.x / 2.0 - 0.0005, u_Size.x / 2.0, distance));
        }
    """

}