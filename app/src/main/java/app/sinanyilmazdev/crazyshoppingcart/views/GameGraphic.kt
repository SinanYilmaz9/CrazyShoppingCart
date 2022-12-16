package app.sinanyilmazdev.crazyshoppingcart.views

import android.view.View
import android.widget.LinearLayout

import android.widget.TextView
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import app.sinanyilmazdev.crazyshoppingcart.beans.World


class GameGraphic : View {

    private var world: World? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,attrs, defStyleAttr)

    fun initData(
        context: Context,
        topScore: TextView,
        score: TextView,
        gameover: LinearLayout?,
        magnification: Float
    ) {
        world = World(context, topScore, score, gameover,magnification)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        world?.draw(canvas)
        invalidate()
    }

    fun startGame() = world?.startGame()

    fun gameOver() = world?.gameOver()

    fun setOffset(offset: Float) = world?.setOffset(offset)

    fun setSpeed(speed: Int) = world?.setSpeed(speed)

}