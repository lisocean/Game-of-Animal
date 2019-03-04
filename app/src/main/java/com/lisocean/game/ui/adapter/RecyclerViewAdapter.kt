package com.lisocean.game.ui.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lisocean.game.logic.LogicOfGame
import com.lisocean.game.model.ItemInfo
import com.lisocean.game.weight.ItemView
import com.lisocean.game.weight.UniformLine
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.imageBitmap


class RecyclerViewAdapter(val activity : AppCompatActivity) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
    private val MSG_DRAWLINE = 0x00
    private val msg_removeLine = 0x01
    private val msg_nanshou =0x10
    val xline = 105
    val yline = 120
    private var handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                MSG_DRAWLINE -> drawLine()
                msg_removeLine -> removeLine()
                msg_nanshou -> nanshou()
            }
        }
    }

    val lineList = mutableListOf<UniformLine>()
    private var firstView : View? = null
    private var secondView : View? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(ItemView(p0.context))
    }

    override fun getItemCount(): Int {
        return 81
    }
    private fun removeLine(){
        lineList.forEach {
            activity.layout.removeView(it)
        }
        handler.removeCallbacksAndMessages(null)
        lineList.clear()

    }
    private fun drawLine(){
        val size =  LogicOfGame.listOfLine.size
        var location1 : IntArray = intArrayOf(0,0)
        firstView?.getLocationOnScreen(location1)
        var location2 : IntArray = intArrayOf(0,0)
        secondView?.getLocationOnScreen(location2)

        when(size){
            0->lineList.add(UniformLine(activity.baseContext, location1[0] + 50,location1[1] - 30, location2[0] + 50, location2[1] - 30))
            1->{
                var location3 = intArrayOf(0,0)
                activity.find<View>(LogicOfGame.listOfLine[0].x * 9 + LogicOfGame.listOfLine[0].y + 1).getLocationInWindow(location3)
                lineList.add(UniformLine(activity.baseContext, location1[0] + 50,location1[1] - 30, location3[0] + 50, location3[1] - 30))
                lineList.add(UniformLine(activity.baseContext, location3[0] + 50,location3[1] - 30, location2[0] + 50, location2[1] - 30))
            }
            2->{
                println(LogicOfGame)
                if(LogicOfGame.listOfLine[0].x == -1 && LogicOfGame.listOfLine[1].x == -1)
                    return
                if(LogicOfGame.listOfLine[0].x == 9 && LogicOfGame.listOfLine[1].x == 9)
                    return
                if(LogicOfGame.listOfLine[0].y == -1 && LogicOfGame.listOfLine[1].y == -1)
                    return
                if(LogicOfGame.listOfLine[0].y == 9 && LogicOfGame.listOfLine[1].y == 9)
                    return
                var location3 = intArrayOf(0,0)
                activity.find<View>(LogicOfGame.listOfLine[0].x  * 9+ LogicOfGame.listOfLine[0].y + 1).getLocationInWindow(location3)
                var location4 = intArrayOf(0,0)
                activity.find<View>(LogicOfGame.listOfLine[1].x * 9 + LogicOfGame.listOfLine[1].y + 1).getLocationInWindow(location4)
                lineList.add(UniformLine(activity.baseContext, location1[0] + 50,location1[1] - 30, location3[0] + 50, location3[1] - 30))
                lineList.add(UniformLine(activity.baseContext, location3[0] + 50,location3[1] - 30, location4[0] + 50, location4[1] - 30))
                lineList.add(UniformLine(activity.baseContext, location4[0] + 50,location4[1] - 30, location2[0] + 50, location2[1] - 30))
            }
        }
        lineList.forEach {
            this@RecyclerViewAdapter.activity.layout.addView(it)
        }

        secondView?.visibility = View.INVISIBLE
        firstView?.visibility = View.INVISIBLE
        firstView = null
        secondView = null
        LogicOfGame.firstSelectOfItem = ItemInfo()
        LogicOfGame.secondSelectOfItem = ItemInfo()
    }
    private fun nanshou(){
        if(LogicOfGame.listOfLine.size == 2){
            secondView?.visibility = View.INVISIBLE
            firstView?.visibility = View.INVISIBLE
            firstView = null
            secondView = null
            LogicOfGame.firstSelectOfItem = ItemInfo()
            LogicOfGame.secondSelectOfItem = ItemInfo()
        }
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.apply {
            id = p1 + 1
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
                        if (LogicOfGame.canRemove){
                            //todo == line to next
                            //todo in the main thread next to async
                            this@RecyclerViewAdapter.secondView = it
                            this@RecyclerViewAdapter.handler.sendEmptyMessage(MSG_DRAWLINE)
                            this@RecyclerViewAdapter.handler.sendEmptyMessageDelayed(msg_removeLine,
                                (300 * (LogicOfGame.listOfLine.size + 1)).toLong()
                            )
                            this@RecyclerViewAdapter.handler.sendEmptyMessageDelayed(msg_nanshou,
                                30.toLong())
                 //           this@RecyclerViewAdapter.activity.layout.addView(UniformLine(activity.baseContext,300, 500, 600, 200))
                            LogicOfGame.score += 2

                            this@RecyclerViewAdapter.activity.textView.text = "Score : ${LogicOfGame.score}"


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