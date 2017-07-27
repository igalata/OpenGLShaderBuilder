package com.igalata.openglshaderbuilder.shader.shapes

/**
 * Created by irinagalata on 7/23/17.
 */
internal object RectangleShader : ShapeShader {

    //language=GLSL
    private val additionalFunctions = """
        bool equal(float a, float b) {
            return abs(a - b) < 0.001;
        }

        float angle(vec2 a, vec2 b) {
            return acos(dot(normalize(a), normalize(b)));
        }

        bool insideRectangle(vec2 uv) {
            vec2 a = vec2(0.5 - u_Size.x / 2.0, 0.5 + u_Size.y / 2.0) - uv;
            vec2 b = vec2(0.5 - u_Size.x / 2.0, 0.5 - u_Size.y / 2.0) - uv;
            vec2 c = vec2(0.5 + u_Size.x / 2.0, 0.5 - u_Size.y / 2.0) - uv;
            vec2 d = vec2(0.5 + u_Size.x / 2.0, 0.5 + u_Size.y / 2.0) - uv;

            return equal(angle(a, b) + angle(b, c) + angle(c, d) + angle(a, d), PI * 2.0);
        }

    """

    //language=GLSL
    override val withColor = com.igalata.openglshaderbuilder.shader.shapes.RectangleShader.additionalFunctions + """
        void main() {
            gl_FragColor = insideRectangle(v_UV) ? u_Color : vec4(0.0);
        }
    """

    //language=GLSL
    override val withTexture = com.igalata.openglshaderbuilder.shader.shapes.RectangleShader.additionalFunctions + """
        void main() {
            float positionX = ((vec2(0.5, 0.5) - v_UV + u_Size.x / 2.0) / u_Size.x).x;
            float positionY = ((vec2(0.5, 0.5) - v_UV + u_Size.y / 2.0) / u_Size.y).y;
            gl_FragColor = insideRectangle(v_UV) ? texture2D(u_Texture, vec2(positionX, positionY)) : vec4(0.0);
        }
    """

}