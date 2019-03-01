package com.color.kid.paint.shaders;

public class Shaders {
    public static final String TEXTURE_FRAGMENT_SHADER = "precision mediump float; \nuniform sampler2D u_TextureUnit; \nvarying vec2 v_TextureCoordinates; \nvoid main() {\ngl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates); \n}";
    public static final String TEXTURE_VERTEX_SHADER = "uniform mat4 u_Matrix; \nattribute vec4 a_Position; \nattribute vec2 a_TextureCoordinates; \nvarying vec2 v_TextureCoordinates; \nvoid main() { \n v_TextureCoordinates = a_TextureCoordinates;\ngl_Position = u_Matrix * a_Position;\n}";
}
