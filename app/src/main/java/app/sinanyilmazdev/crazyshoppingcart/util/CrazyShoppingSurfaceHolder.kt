package app.sinanyilmazdev.crazyshoppingcart.util

import android.view.SurfaceHolder

open class CrazyShoppingSurfaceHolder: SurfaceHolder.Callback {
    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}