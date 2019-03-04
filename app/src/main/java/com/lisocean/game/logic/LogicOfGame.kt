package com.lisocean.game.logic

import android.graphics.Point
import com.lisocean.game.R
import com.lisocean.game.model.ItemInfo
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object LogicOfGame {
    const val maxLine = 9
    var score = 0

    val allInfo  = MutableList(maxLine){ MutableList(maxLine){ ItemInfo() }}
    val resOfAnimal = listOf(
        R.mipmap.bearartboard1,
        R.mipmap.catartboard1,
        R.mipmap.elephantartboard1,
        R.mipmap.fishartboard1,
        R.mipmap.flowerartboard1,
        R.mipmap.giraffeartboard1,
        R.mipmap.honeyartboard1,
        R.mipmap.hypoartboard1,
        R.mipmap.kangarooartboard1,
        R.mipmap.leoartboard1,
        R.mipmap.lionartboard1,
        R.mipmap.pigartboard1,
        R.mipmap.rhinoartboard1,
        R.mipmap.sunartboard1,
        R.mipmap.tigerartboard1,
        R.mipmap.wolfartboard1)
    /**
     * test map of animal
     */
    val animalMap = listOf(
        listOf( 0,10, 2, 3, 6, 4, 6, 2, 7),
        listOf(10, 0,15, 3, 6, 4, 6, 3,11),
        listOf(12, 0, 2, 3, 6, 4, 6, 2, 7),
        listOf(10,10, 2, 9, 6, 4,12, 4, 7),
        listOf(13, 0, 6, 3, 6, 9, 6, 2,14),
        listOf(11, 0, 2, 3, 9, 9, 8, 2, 7),
        listOf( 0, 3, 6, 9,11, 4, 6,13, 7),
        listOf(12, 0, 2, 3,12, 4, 8, 2, 7),
        listOf( 0, 14,2,11, 6, 4, 6, 2,15)
    )
    val block = MutableList(11 ){y ->
        MutableList(11) {x ->
        if (x == 0 || y ==0 || x== 10 || y == 10)
            0
        else
            1
    } }

    var firstSelectOfItem = ItemInfo()
    var secondSelectOfItem = ItemInfo()
    private val p1 : Point
    get() = firstSelectOfItem.point
    private val p2 : Point
        get() = secondSelectOfItem.point

    private val x1 : Int
        get() = p1.x + 1
    private val x2 : Int
        get() = p2.x + 1

    private val y1 : Int
        get() = p1.y + 1
    private val y2 : Int
        get() = p2.y + 1

    val listOfLine = mutableListOf<Point>()
    val point = Point(-1,-1)
    /**
     * juice if first and second can be remove
     * vertical,horizon,turn_once,turn_twice
     */
    val canRemove : Boolean
        get() = remove()

    private fun remove() : Boolean{
        listOfLine.clear()
        if(firstSelectOfItem.itemType != secondSelectOfItem.itemType)
            return false
        block[x1][y1] = 0
        block[x2][y2] = 0
        //0 z
        if(matchBlock(x1,y1, x2, y2)){
            return true
        }
        if(matchBlockOne(x1, y1, x2, y2)){
            listOfLine.add(point)
            return true
        }
        //2 z
        if(matchBlockTwo())
            return true
        block[x1][y1] = 1
        block[x2][y2] = 1
        return false
    }
    private fun matchBlock(x1 : Int,y1 : Int,x2 : Int,y2 : Int) : Boolean{
        if(x1 != x2 && y1 != y2)
            return false
        if(y1 == y2)
        {
            val start_x = min(x1,x2)
            val end_x = max(x1,x2)
            for(i in start_x..end_x)
                if(block[i][y1] == 1)
                    return false

        }else{
            val start_y = min(y1, y2)
            val end_y = max(y1,y2)
            for( j in start_y..end_y)
                if(block[x1][j] == 1)
                    return false
        }
        return true
    }
    //1 z
    private fun matchBlockOne(x1 : Int,y1 : Int,x2 : Int,y2 : Int) : Boolean{

        //0 z
        if(x1 == x2 || y1 == y2)
            return false
        //turn
        if(block[x1][y2] == 0){
            val stMatch = matchBlock(x1,y1, x1, y2)
            val tdMatch = matchBlock(x1,y2, x2, y2)
            if(stMatch && tdMatch)
            {
                point.x = x1 - 1
                point.y = y2 - 1
                return true
            }

        }
        if(block[x2][y1] == 0){
            val stMatch = matchBlock(x1,y1, x2, y1)
            val tdMatch = matchBlock(x2,y1, x2, y2)
            if(stMatch && tdMatch)
            {
                point.x = x2 - 1
                point.y = y1 - 1
                return true
            }

        }
        return  false
    }
    //2 z

    private fun matchBlockTwo() : Boolean{


        //scan first y
        for(i in (y1 + 1)..10){
            if(block[x1][i] == 0){
                val p = Point(x1 - 1, i - 1)
                val dest = matchBlockOne(x1,i, x2, y2)
                if (dest) {
                    listOfLine.add(p)
                    listOfLine.add(point)
                    return true
                }
            }
            else
                break
        }
        for(i in (y1 - 1) downTo  0){
            if(block[x1][i] == 0){
                val p = Point(x1 - 1, i - 1)
                val dest = matchBlockOne(x1,i, x2, y2)
                if (dest) {
                    listOfLine.add(p)
                    listOfLine.add(point)
                    return true
                }
            }
            else
                break
        }
        //scan first x
        for(i in (x1 + 1)..10){
            if(block[i][y1] == 0){
                val p = Point( i - 1, y1 - 1)
                val dest = matchBlockOne(i, y1, x2, y2)
                if (dest) {
                    listOfLine.add(p)
                    listOfLine.add(point)
                    return true
                }
            }
            else
                break
        }
        for( i in (x1 - 1) downTo 0){
            if(block[i][y1] == 0){
                val p = Point( i - 1, y1 - 1)
                val dest = matchBlockOne(i, y1, x2, y2)
                if (dest) {
                    listOfLine.add(p)
                    listOfLine.add(point)
                    return true
                }
            }
            else
                break
        }

        return false
    }
}