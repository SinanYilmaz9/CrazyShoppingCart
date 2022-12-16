package app.sinanyilmazdev.crazyshoppingcart.beans

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import app.sinanyilmazdev.crazyshoppingcart.R

class ShopCart(context: Context, private var screenWidth: Int, screenHeight: Int) {

    var x = 0f
    var y = 0f
    var cartWidth = 0
    var cartHeight = 0

    private var shopCart: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.shoppingcart)

    init {
        cartWidth = shopCart.width
        cartHeight = shopCart.height
        x = (screenWidth / 2).toFloat()
        y = (screenHeight / 2 + cartHeight).toFloat()
    }

    fun draw(canvas: Canvas) = canvas.drawBitmap(shopCart, x - cartWidth / 2, y - cartHeight / 2, null)

    fun setOffset(offset: Float) {
        x = if (offset > screenWidth - cartWidth) {
            (screenWidth - cartWidth / 2).toFloat()
        } else if (offset < cartWidth) {
            (cartWidth / 2).toFloat()
        } else {
            offset
        }
    }
}