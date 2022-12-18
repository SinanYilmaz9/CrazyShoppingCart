package app.sinanyilmazdev.crazyshoppingcart.util

import android.content.Context
import android.hardware.Camera
import android.util.Log
import app.sinanyilmazdev.crazyshoppingcart.camera.LensEnginePreview
import app.sinanyilmazdev.crazyshoppingcart.views.GameGraphic
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory
import app.sinanyilmazdev.crazyshoppingcart.transactor.HandKeypointTransactor
import java.io.IOException

class GameUtils {
    private val TAG = "GameUtils"
    private var analyzer: MLHandKeypointAnalyzer? = null
    private var lensEngine: LensEngine? = null
    var width = 0
    var height = 0

    fun createHandAnalyze() {
        analyzer = MLHandKeypointAnalyzerFactory.getInstance().handKeypointAnalyzer
    }

    fun setHandTransactor(gameGraphic: GameGraphic) =
        analyzer?.setTransactor(HandKeypointTransactor(gameGraphic))

    fun getMagnification(): Float {
        val magnification : Int
        val camera: Camera = Camera.open(1)
        val supportedPreviewSizes = camera.parameters.supportedPreviewSizes

        for (i in supportedPreviewSizes.indices.reversed()) {
            width = supportedPreviewSizes[i].width
            height = supportedPreviewSizes[i].height
            if (width >= 300 && height >= 300) {
                break
            }
        }
        camera.release()
        magnification = supportedPreviewSizes[0].height / height

        return magnification.toFloat()
    }

    fun initLensEngine(context: Context?, preview: LensEnginePreview) {
        getMagnification()

        lensEngine = LensEngine.Creator(context, analyzer)
            .setLensType(LensEngine.FRONT_LENS)
            .applyDisplayDimension(width, height)
            .applyFps(30.0f)
            .enableAutomaticFocus(true)
            .create()

        startLensEngine(preview)
    }

    private fun startLensEngine(preview: LensEnginePreview) {
            try {
                preview.start(lensEngine)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start lens engine.", e)
                lensEngine?.release()
                lensEngine = null
            }
    }

    fun stopPreview(mPreview: LensEnginePreview) {
        if (lensEngine != null) {
            mPreview.stop()
        }
    }

    fun releaseAnalyze() {
            lensEngine?.release()
            analyzer?.destroy()
    }
}