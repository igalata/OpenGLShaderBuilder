package com.igalata.openglshaderbuilder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.GLES20.*
import android.opengl.GLUtils.texImage2D
import android.support.v4.content.ContextCompat
import com.igalata.openglshaderbuilder.data.Vec4
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by irinagalata on 6/25/17.
 */

internal fun FloatArray.toFloatBuffer() = ByteBuffer
        .allocateDirect(size * Const.FLOAT_SIZE)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()?.put(this)

internal fun Bitmap.toTexture(textureUnit: Int) {
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textureUnit)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    texImage2D(GL_TEXTURE_2D, 0, this, 0)
    recycle()
    glBindTexture(GL_TEXTURE_2D, 0)
}

fun Int.rgba(context: Context) = ContextCompat.getColor(context, this).rgba()

fun Int.rgba(): Vec4 = Vec4(
        Color.red(this).toFloat() / 255f,
        Color.green(this).toFloat() / 255f,
        Color.blue(this).toFloat() / 255f,
        Color.alpha(this).toFloat() / 255f)
