package com.lisocean.game.ui.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import com.lisocean.game.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

@SuppressLint("Registered")
class MainActivity : AppCompatActivity(), ViewPropertyAnimatorListener {
    private var isAnimateFinish = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scaleTo(1.0f)
        //jump to Game Activity
        playGameButton.setOnClickListener {
            startActivity<GameActivity>()
            finish()
        }
        //kill app
        exitGameButton.setOnClickListener {
            System.exit(0)
        }
    }


    private fun scaleTo(value : Float, duration: Long = 1500){
        ViewCompat.animate(bgImageView).apply {
            scaleX(value)
            scaleY(value)
            this.duration = duration
            setListener(this@MainActivity)
        }
    }

    override fun onAnimationEnd(p0: View?) {
        isAnimateFinish = if(isAnimateFinish){
            scaleTo(1.2f)
            false
        } else{
            scaleTo(1.0f)
            true
        }

    }

    override fun onAnimationCancel(p0: View?) {
    }

    override fun onAnimationStart(p0: View?) {

    }

}
