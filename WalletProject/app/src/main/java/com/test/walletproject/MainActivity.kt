package com.test.walletproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.test.walletproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.thirdCard.setOnClickListener(getCardClickListener(R.id.thirdCardOnTop))
        activityMainBinding.secondCard.setOnClickListener(getCardClickListener(R.id.secondCardOnTop))
        activityMainBinding.firstCard.setOnClickListener(getCardClickListener(R.id.firstCardOnTop))
    }

    private fun getCardClickListener(@IdRes endStateId: Int) = OnClickListener {
        with(activityMainBinding){
            when(root.currentState){
                R.id.fanOut -> {
                    root.setTransition(R.id.fanOut, R.id.firstCardOnTop)
                    root.transitionToEnd()
                    collapsedCardCompletedListener(endStateId)
                }
                R.id.thirdCardOnTop -> {
                    if (R.id.thirdCardOnTop == endStateId) {

                    }else{
                        root.setTransition(R.id.thirdCardOnTop, endStateId)
                        root.transitionToEnd()
                    }
                }
                R.id.secondCardOnTop -> {
                    if (R.id.secondCardOnTop == endStateId) {

                    } else {
                        root.setTransition(R.id.secondCardOnTop, endStateId)
                        root.transitionToEnd()
                    }
                }
                R.id.firstCardOnTop -> {
                    if (R.id.secondCardOnTop == endStateId) {

                    } else {
                        root.setTransition(R.id.firstCardOnTop, endStateId)
                        root.transitionToEnd()
                    }
                }
            }
        }
    }

    private fun collapsedCardCompletedListener(@IdRes endStateId: Int) {
        with(activityMainBinding){
            root.setTransitionListener(object: TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    super.onTransitionCompleted(motionLayout, currentId)
                    if (currentId == R.id.firstCardOnTop) {
                        root.setTransition(R.id.firstCardOnTop,endStateId)
                        root.transitionToEnd()
                    }
                    root.setTransitionListener(null)
                }
            })
        }
    }
}