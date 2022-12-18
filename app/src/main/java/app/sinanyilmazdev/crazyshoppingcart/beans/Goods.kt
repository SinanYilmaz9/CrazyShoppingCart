package app.sinanyilmazdev.crazyshoppingcart.beans

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import app.sinanyilmazdev.crazyshoppingcart.R
import java.util.*


class Goods(private var context: Context, private var n: Int, private var screenWidth: Int,
            private var screenHeight: Int) {

    private val goods = intArrayOf(
        R.drawable.hat, R.drawable.coat, R.drawable.babyclothes, R.drawable.slipper,
        R.drawable.basketball, R.drawable.banana, R.drawable.shoes
    )

    private var images: Array<Bitmap?> = arrayOfNulls(goods.size)
    private var image: Bitmap
    private var x = 0
    private var y = 0

    private var goodsWidth = 0
    private var goodsHeight = 0

    private var distance = 0

    private var speed = 6

    private val random: Random = Random()

    init {

        for (i in images.indices) {
            images[i] = BitmapFactory.decodeResource(context.resources, goods[i])
        }

        image = images[0]!!
        goodsWidth = image.width
        goodsHeight = image.height
        distance = screenHeight / 4

        x = goodsWidth / 2 + random.nextInt(screenWidth - goodsWidth)
        y = -goodsHeight / 2 - distance * n
        image = images[random.nextInt(goods.size)]!!
    }

    fun draw(canvas: Canvas) = canvas.drawBitmap(image, (x - goodsWidth / 2).toFloat(), (y - goodsHeight / 2).toFloat(), null)

    fun step() {
        y += speed

        if (y >= screenHeight + goodsHeight / 2) {
            image = images[random.nextInt(goods.size)]!!
            x = goodsWidth / 2 + random.nextInt(screenWidth - goodsWidth)
            y = -goodsHeight / 2
        }
    }

    fun hit(shopCart: ShopCart): Boolean {
        if (x + goodsWidth / 2 > shopCart.x - shopCart.cartWidth / 2 && x - goodsWidth / 2 < shopCart.x + shopCart.cartWidth) {
            if (y + goodsHeight / 2 > shopCart.y - shopCart.cartHeight / 2 && y - goodsHeight / 2 < shopCart.y + shopCart.cartHeight / 2) {
                image = images[random.nextInt(goods.size)]!!
                x = goodsWidth / 2 + random.nextInt(screenWidth - goodsWidth)
                y = -goodsHeight / 2
                return true
            }
        }
        return false
    }

    fun setSpeed(speed: Int) {
        this.speed = speed
    }
}