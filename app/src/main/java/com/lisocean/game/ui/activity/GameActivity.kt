package com.lisocean.game.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import com.lisocean.game.R
import com.lisocean.game.logic.LogicOfGame
import com.lisocean.game.ui.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.find

@SuppressLint("Registered")
class GameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initRecyclerView()
    }

    /**
     * init the recycler view with StaggeredGridLayoutManager
     * 9 x 9
     * object : StaggeredGridLayoutManager  : set scroll enable
     */
    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = object : StaggeredGridLayoutManager(LogicOfGame.maxLine,StaggeredGridLayoutManager.VERTICAL){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = RecyclerViewAdapter(this@GameActivity)
        }

    }
}