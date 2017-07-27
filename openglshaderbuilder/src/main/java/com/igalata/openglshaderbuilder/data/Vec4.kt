package com.igalata.openglshaderbuilder.data

/**
 * Created by irinagalata on 6/19/17.
 */
data class Vec4(val x: Float = 1f, val y: Float = x, val z: Float = x, val w: Float = x) {

    fun toArray() = floatArrayOf(x, y, z, w)

}