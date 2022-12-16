package app.sinanyilmazdev.crazyshoppingcart.views.overlay

import android.graphics.Canvas
import com.huawei.hms.mlsdk.common.LensEngine;

abstract class BaseGraphic(private val graphicOverlay: GraphicOverlay?) {

    abstract fun draw(canvas: Canvas?)

    open fun scaleX(x: Float): Float {
        return x * graphicOverlay?.getWidthScaleValue()!!
    }

    open fun scaleY(y: Float): Float {
        return y * graphicOverlay?.getHeightScaleValue()!!
    }

    open fun translateX(x: Float): Float {
        return if (graphicOverlay?.getCameraFacing() == LensEngine.FRONT_LENS) {
            graphicOverlay.width - scaleX(x)
        } else {
            scaleX(x)
        }
    }

    open fun translateY(y: Float): Float {
        return scaleY(y)
    }
}