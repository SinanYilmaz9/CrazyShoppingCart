package app.sinanyilmazdev.crazyshoppingcart.transactor

import android.util.SparseArray
import app.sinanyilmazdev.crazyshoppingcart.views.GameGraphic
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoint.TYPE_FOREFINGER_FOURTH
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints


class HandKeypointTransactor(gameGraphic: GameGraphic) : MLAnalyzer.MLTransactor<MLHandKeypoints> {

    private var gameGraphic: GameGraphic? = null

    init {
        this.gameGraphic = gameGraphic
    }

    override fun transactResult(results: MLAnalyzer.Result<MLHandKeypoints?>) {
        val analyseList: SparseArray<MLHandKeypoints?>? = results.analyseList

        if (analyseList == null || analyseList.size() <= 0) {
            return
        }

        val center = analyseList[0]?.getHandKeypoint(TYPE_FOREFINGER_FOURTH)
        val centerX = center!!.pointX
        gameGraphic!!.setOffset(centerX)
        gameGraphic!!.invalidate()

    }

    override fun destroy() {}
}

