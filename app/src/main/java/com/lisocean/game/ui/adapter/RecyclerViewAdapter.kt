package com.lisocean.game.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lisocean.game.logic.LogicOfGame
import com.lisocean.game.model.ItemInfo
import com.lisocean.game.weight.ItemView
import kotlinx.android.synthetic.main.activity_game.view.*
import kotlinx.android.synthetic.main.item.view.*
import org.jetbrains.anko.imageBitmap

class RecyclerViewAdapter(val textView: TextView) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private var firstView : View? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(ItemView(p0.context))
    }

    override fun getItemCount(): Int {
        return 81
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.apply {
            val x = p1 / 9
            val y = p1 % 9
            LogicOfGame.allInfo[x][y] =
                ItemInfo(
                    point = Point(x,y),
                    itemType = LogicOfGame.animalMap[x][y],
                    isExist = true)
            //keep info
            itemOfImage.tag = LogicOfGame.allInfo[x][y]

            itemOfImage.setOnClickListener {
                //set a id of this to make i can find it by id
                it.id = p1 + 1
                if(it.visibility == View.INVISIBLE)
                    return@setOnClickListener
                //no one was selected
                if(!LogicOfGame.firstSelectOfItem.isSelect){
                    //change the background
                    it.isSelected = true
                    //set first selected item and change the item info state
                    LogicOfGame.firstSelectOfItem = it.tag as ItemInfo
                    LogicOfGame.firstSelectOfItem.isSelect = true
                    it.tag = LogicOfGame.firstSelectOfItem
                    firstView = it

                }// first has been selected
                else{
                    val itInfo = it.tag as ItemInfo
                    //is the same as the first
                    if(itInfo.isSelect){
                        it.isSelected = false
                        LogicOfGame.firstSelectOfItem.isSelect = false
                        it.tag = LogicOfGame.firstSelectOfItem
                        LogicOfGame.firstSelectOfItem = ItemInfo()
                    }//first is selected and it is not the same one
                    else{
                        it.isSelected = true
                        LogicOfGame.secondSelectOfItem = it.tag as ItemInfo
                        LogicOfGame.secondSelectOfItem.isSelect = true
                        println(LogicOfGame.firstSelectOfItem)
                        println(LogicOfGame.secondSelectOfItem)
                        println(LogicOfGame.block)
                        if (LogicOfGame.canRemove){
                            //todo == line to next
                            //todo in the main thread next to async
                            LogicOfGame.score += 2

                            this@RecyclerViewAdapter.textView.text = "Score : ${LogicOfGame.score}"
                            it.visibility = View.INVISIBLE
                            firstView?.visibility = View.INVISIBLE
                            firstView = null
                            val pointOne = LogicOfGame.firstSelectOfItem.point
                            val pointTwo = LogicOfGame.secondSelectOfItem.point
                            LogicOfGame.firstSelectOfItem = ItemInfo()
                            LogicOfGame.secondSelectOfItem = ItemInfo()

                        }else{
                            //init this view
                            it.isSelected = false
                            LogicOfGame.secondSelectOfItem.isSelect = false
                            it.tag = LogicOfGame.secondSelectOfItem
                            LogicOfGame.secondSelectOfItem = ItemInfo()
                            //init first view
//                            val view =  findViewById<View>(pointOfFirst.x * LogicOfGame.maxLine + pointOfFirst.y + 1)
//                            println(view.id)
                            firstView?.let{view ->
//                                println(view.tag as ItemInfo)
                                view.isSelected = false
                                LogicOfGame.firstSelectOfItem.isSelect = false
                                view.tag = LogicOfGame.firstSelectOfItem
                                LogicOfGame.firstSelectOfItem = ItemInfo()
                                firstView = null
                            }
//                            view.isSelected = false
//                            LogicOfGame.firstSelectOfItem.isSelect = false
//                            imageOfView.tag =  LogicOfGame.firstSelectOfItem
//                            LogicOfGame.firstSelectOfItem = ItemInfo()
                        }
                    }

                }

            }
            /**
             * from image type to set the image of item
             */
            itemOfImage.imageBitmap = BitmapFactory.decodeResource(resources, LogicOfGame.resOfAnimal[LogicOfGame.allInfo[x][y].itemType])
        }
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}