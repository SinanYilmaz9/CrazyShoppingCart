package app.sinanyilmazdev.crazyshoppingcart.beans

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.getSystemService
import app.sinanyilmazdev.crazyshoppingcart.R


class World(
    private var context: Context, private var topScore: TextView, private var score: TextView,
    private var gameover: LinearLayout?,
    magnification: Float
) {

    private var screenWidth = 0
    private var screenHeight = 0
    private var gameStart = false

    private var shopCart: ShopCart? = null
    private var goods1: Goods? = null
    private var goods2: Goods? = null
    private var goods3: Goods? = null
    private var goods4: Goods? = null

    private var magnification = 1f

    private var scoreStr: String? = null
    private var scoreNum = 0

    init {
        scoreStr = context.getString(R.string.score)
        topScore.text = scoreStr + scoreNum
        score.text = scoreNum.toString() + ""

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels

        this.magnification = magnification

        start()
    }

    private fun start() {
        shopCart = ShopCart(context, screenWidth, screenHeight)

        goods1 = Goods(context, 0, screenWidth, screenHeight)
        goods2 = Goods(context, 1, screenWidth, screenHeight)
        goods3 = Goods(context, 2, screenWidth, screenHeight)
        goods4 = Goods(context, 3, screenWidth, screenHeight)

        scoreNum = 0
    }

    @SuppressLint("SetTextI18n")
    fun draw(canvas: Canvas?) {
        if (gameStart) {
            canvas?.let {
                shopCart?.draw(it)
                goods1?.draw(it)
                goods2?.draw(it)
                goods3?.draw(it)
                goods4?.draw(it)
            }
            goods1?.step()
            goods2?.step()
            goods3?.step()
            goods4?.step()

            shopCart?.let { shop->
                if (goods1?.hit(shop) == true) {
                    scoreNum++
                    topScore.text = scoreStr + scoreNum
                    score.text = scoreNum.toString() + ""
                }
                if (goods2?.hit(shop) == true) {
                    scoreNum++
                    topScore.text = scoreStr + scoreNum
                    score.text = scoreNum.toString() + ""
                }
                if (goods3?.hit(shop) == true) {
                    scoreNum++
                    topScore.text = scoreStr + scoreNum
                    score.text = scoreNum.toString() + ""
                }
                if (goods4?.hit(shop) == true) {
                    scoreNum++
                    topScore.text = scoreStr + scoreNum
                    score.text = scoreNum.toString() + ""
                }
            }
        }
    }

    fun startGame() {
        start()
        gameStart = true
    }

    fun gameOver() {
        gameStart = false
        gameover?.visibility = View.VISIBLE
    }

    fun setOffset(offset: Float) {
        if (gameStart) shopCart?.setOffset(screenWidth - offset * magnification)
    }

    fun setSpeed(speed: Int) {
        goods1?.setSpeed(speed)
        goods2?.setSpeed(speed)
        goods3?.setSpeed(speed)
        goods4?.setSpeed(speed)
    }
}