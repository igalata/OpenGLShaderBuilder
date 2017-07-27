package com.igalata.shaderbuilderdemo

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by irinagalata on 7/24/17.
 */
class GLView : GLSurfaceView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8,8,8,8,16,0)
        setRenderer(ShapesRenderer(context))
        renderMode = RENDERMODE_CONTINUOUSLY
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)
    }

}