package com.igalata.openglshaderbuilder.data

/**
 * Created by irinagalata on 6/25/17.
 */
data class Vec2(val x: Float = 1f, val y: Float = x) {

    fun toArray() = floatArrayOf(x, y)

}