package com.lisocean.game.model

import android.graphics.Point

data class ItemInfo(var point : Point = Point(-1,-1),
                    var itemType : Int = -1,
                    var isSelect : Boolean = false,
                    var isExist : Boolean = false)