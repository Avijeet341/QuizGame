package com.avi.funquizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.avi.funquizgame.databinding.ActivitySplashWelcomeBinding


class SplashWelcomeActivity : AppCompatActivity() {
    lateinit var splashWelcomeActivity:ActivitySplashWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashWelcomeActivity=ActivitySplashWelcomeBinding.inflate(layoutInflater)
        val view=splashWelcomeActivity.root
        setContentView(view)



        val alphaAnimation= AnimationUtils.loadAnimation(applicationContext,R.anim.splash_anim)
        splashWelcomeActivity.textViewSplash.startAnimation(alphaAnimation)

        val handler= Handler(Looper.getMainLooper())

        handler.postDelayed(object :Runnable{
            override fun run() {
                val intent= Intent(this@SplashWelcomeActivity,LoginQuizActivity::class.java)
                startActivity(intent)
                finish()

            } },5000)
    }
}