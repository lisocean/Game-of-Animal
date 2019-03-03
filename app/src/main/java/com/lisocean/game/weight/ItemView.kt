package com.lisocean.game.weight

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.lisocean.game.R
import com.lisocean.game.model.ItemInfo
import kotlinx.android.synthetic.main.item.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast

/***
 * @param initCallback the interface to init some attribute
 */
class ItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    initCallback : (ItemView.() -> Unit)? = null) : RelativeLayout(context, attrs, defStyleAttr) {

    init {

            View.inflate(context, R.layout.item, this)
            initCallback?.invoke(this)
        }
//    fun setItemClickListener(listener : (view : View) -> Unit ) {
//        itemOfImage.setOnClickListener {
//            listener.invoke(it)
//            isSelected = when(isSelected) {
//                true -> false
//                false -> true
//        }
//        }
//    }
}

