package app.sinanyilmazdev.crazyshoppingcart.views.overlay

import android.content.Context
import android.graphics.Canvas
import android.hardware.Camera
import android.util.AttributeSet
import android.view.View

class GraphicOverlay(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()
    private var previewWidth = 0
    private var previewHeight = 0
    private var widthScaleValue = 1.0f
    private var heightScaleValue = 1.0f
    private var cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_BACK
    private val graphics: MutableList<BaseGraphic> = ArrayList()

    fun clear() {
        synchronized(this.lock) { this.graphics.clear() }
        this.postInvalidate()
    }

    fun addGraphic(graphic: BaseGraphic) {
        synchronized(this.lock) { this.graphics.add(graphic) }
    }

    fun removeGraphic(graphic: BaseGraphic) {
        synchronized(this.lock) { this.graphics.remove(graphic) }
        this.postInvalidate()
    }

    fun setCameraInfo(width: Int, height: Int, facing: Int) {
        synchronized(this.lock) {
            this.previewWidth = width
            this.previewHeight = height
            this.cameraFacing = facing
        }
        this.postInvalidate()
    }

    fun getCameraFacing(): Int {
        return this.cameraFacing
    }

    fun getWidthScaleValue(): Float {
        return this.widthScaleValue
    }

    fun getHeightScaleValue(): Float {
        return this.heightScaleValue
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(this.lock) {
            if (this.previewWidth != 0 && this.previewHeight != 0) {
                this.widthScaleValue =
                    width.toFloat() / this.previewWidth.toFloat()
                this.heightScaleValue =
                    height.toFloat() / this.previewHeight.toFloat()
            }
            for (graphic in this.graphics) {
                graphic.draw(canvas)
            }
        }
    }
}