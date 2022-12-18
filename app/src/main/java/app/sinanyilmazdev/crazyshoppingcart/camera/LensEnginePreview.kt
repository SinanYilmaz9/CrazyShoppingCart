package app.sinanyilmazdev.crazyshoppingcart.camera

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import app.sinanyilmazdev.crazyshoppingcart.R
import app.sinanyilmazdev.crazyshoppingcart.util.CrazyShoppingSurfaceHolder
import app.sinanyilmazdev.crazyshoppingcart.views.overlay.GraphicOverlay
import java.io.IOException
import com.huawei.hms.common.size.Size
import com.huawei.hms.mlsdk.common.LensEngine


class LensEnginePreview(context: Context?, attrs: AttributeSet?) : ViewGroup(context,attrs) {

 /*   class LensEnginePreview(var mContext: Context, attrs: AttributeSet) : ViewGroup(mContext, attrs) {

        private val TAG = "LensEnginePreview"

        private var mSurfaceView: SurfaceView? = null
        private var mStartRequested = false
        private var mSurfaceAvailable = false

        private var mLensEngine: LensEngine? = null
        private val mOverlay: GraphicOverlay? = null

        init {
            mContext = context
            mStartRequested = false
            mSurfaceAvailable = false
            mSurfaceView = SurfaceView(context)
            mSurfaceView!!.holder.addCallback(object : CrazyShoppingSurfaceHolder() {

                override fun surfaceCreated(holder: SurfaceHolder) {
                    super.surfaceCreated(holder)
                    mSurfaceAvailable = true
                    try {
                        startIfReady()
                    } catch (e: IOException) {
                        Log.e(TAG, mContext.getString(R.string.not_start_camera), e)
                    }
                }
                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    super.surfaceDestroyed(holder)
                    mSurfaceAvailable = false
                }
            })
            this.addView(mSurfaceView)
        }

        fun stop() {
            mLensEngine?.close()
        }

        @Throws(IOException::class)
        private fun startIfReady() {
            if (mStartRequested && mSurfaceAvailable) {
                mLensEngine?.run(mSurfaceView!!.holder)

                mOverlay?.let {
                    val size: Size = mLensEngine?.displayDimension!!
                    val min = min(size.width, size.height)
                    val max = max(size.width, size.height)
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        // Swap width and height sizes when in portrait, since it will be rotated by
                        // 90 degrees
                        mLensEngine?.lensType?.let { it1 -> it.setCameraInfo(min, max, it1) }
                    } else {
                        mLensEngine?.lensType?.let { it1 -> it.setCameraInfo(max, min, it1) }
                    }
                    it.clear()
                }
                mStartRequested = false
            }
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            var previewWidth = 320
            var previewHeight = 240

            this.mLensEngine?.let {
                val size: Size = this.mLensEngine!!.displayDimension
                previewWidth = size.width
                previewHeight = size.height
            }

            // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val tmp = previewWidth
                previewWidth = previewHeight
                previewHeight = tmp
            }

            val viewWidth = right - left
            val viewHeight = bottom - top

            val childWidth: Int
            val childHeight: Int
            var childXOffset = 0
            var childYOffset = 0
            val widthRatio = viewWidth.toFloat() / previewWidth.toFloat()
            val heightRatio = viewHeight.toFloat() / previewHeight.toFloat()


            // To fill the view with the camera preview, while also preserving the correct aspect ratio,
            // it is usually necessary to slightly oversize the child and to crop off portions along one
            // of the dimensions. We scale up based on the dimension requiring the most correction, and
            // compute a crop offset for the other dimension.
            if (widthRatio > heightRatio) {
                childWidth = viewWidth
                childHeight = (previewHeight.toFloat() * widthRatio).toInt()
                childYOffset = (childHeight - viewHeight) / 2
            } else {
                childWidth = (previewWidth.toFloat() * heightRatio).toInt()
                childHeight = viewHeight
                childXOffset = (childWidth - viewWidth) / 2
            }

            for (i in 0 until this.childCount) {
                // One dimension will be cropped. We shift child over or up by this offset and adjust
                // the size to maintain the proper aspect ratio.
                getChildAt(i).layout(-1 * childXOffset, -1 * childYOffset,
                    childWidth - childXOffset, childHeight - childYOffset)
            }

            try {
                this.startIfReady()
            } catch (e: IOException) {
                Log.e(TAG, mContext.getString(R.string.not_start_camera), e)
            }
        }
    }

    */

    private var mSurfaceView: SurfaceView? = null
    private var mLensEngine: LensEngine? = null
    private var mOverlay: GraphicOverlay? = null

    init {
        mSurfaceView = SurfaceView(context)
        mSurfaceView?.holder?.addCallback(object: CrazyShoppingSurfaceHolder() {
            override fun surfaceCreated(holder: SurfaceHolder) {
                super.surfaceCreated(holder)
                mSurfaceAvailable = true
                try {
                    startIfReady()
                }
                catch (e: IOException) {
                    Log.e(TAG, e.localizedMessage.toString())
                }
            }
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                super.surfaceDestroyed(holder)
                mSurfaceAvailable = false
            }
        } )
        this.addView(mSurfaceView)
    }


    @Throws(IOException::class)
    fun start(lensEngine: LensEngine?) {
        if (lensEngine == null) {
            stop()
        }
        mLensEngine = lensEngine
        if (mLensEngine != null) {
            mStartRequested = true
            startIfReady()
        }
    }

    fun stop() = mLensEngine?.close()

    @Throws(IOException::class)
    private fun startIfReady() {
        if (mStartRequested && mSurfaceAvailable) {
            mLensEngine?.run(mSurfaceView!!.holder)

            if (mOverlay != null) {
                val size: Size = mLensEngine!!.displayDimension
                val min: Int = size.width.coerceAtMost(size.height)
                val max: Int = size.width.coerceAtLeast(size.height)
                if (context?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    mOverlay!!.setCameraInfo(min, max, mLensEngine!!.lensType)
                } else {
                    mOverlay!!.setCameraInfo(max, min, mLensEngine!!.lensType)
                }
                mOverlay!!.clear()
            }
            mStartRequested = false
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var previewWidth = 480
        var previewHeight = 360
        if (mLensEngine != null) {
            val size: Size = mLensEngine!!.displayDimension
            previewWidth = size.width
            previewHeight = size.height
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            val tmp = previewWidth
            previewWidth = previewHeight
            previewHeight = tmp
        }
        val viewWidth = right - left
        val viewHeight = bottom - top
        val childWidth: Int
        val childHeight: Int
        var childXOffset = 0
        var childYOffset = 0
        val widthRatio = viewWidth.toFloat() / previewWidth.toFloat()
        val heightRatio = viewHeight.toFloat() / previewHeight.toFloat()

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions. We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.
        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight = (previewHeight.toFloat() * widthRatio).toInt()
            childYOffset = (childHeight - viewHeight) / 2
        } else {
            childWidth = (previewWidth.toFloat() * heightRatio).toInt()
            childHeight = viewHeight
            childXOffset = (childWidth - viewWidth) / 2
        }
        for (i in 0 until this.childCount) {
            // One dimension will be cropped. We shift child over or up by this offset and adjust
            // the size to maintain the proper aspect ratio.
            this.getChildAt(i).layout(
                -1 * childXOffset, -1 * childYOffset, childWidth - childXOffset,
                childHeight - childYOffset
            )
        }
        try {
            startIfReady()
        } catch (e: IOException) {
            Log.e(TAG, "Could not start camera source.", e)
        }
    }

    private fun isPortraitMode(): Boolean {
        val orientation: Int = context?.resources?.configuration!!.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true
        }
        Log.d(TAG, "isPortraitMode returning false by default")
        return false
    }

    companion object {
        private const val TAG = "LensEnginePreview"

        private var mStartRequested = false
        private var mSurfaceAvailable = false
    }


}