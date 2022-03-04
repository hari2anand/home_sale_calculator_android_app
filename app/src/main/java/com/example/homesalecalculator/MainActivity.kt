package com.example.homesalecalculator

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var text:TextView ? = null
    val move = 200f
    var ratio = 1.0f
    var bastDst = 0
    var baseratio = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.disp_msg)
        //text.textSize = ratio + 15
        buttonClicker()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
            val action = event.action
            val mainaction = action and MotionEvent.ACTION_MASK
            if (mainaction == MotionEvent.ACTION_POINTER_DOWN) {
                bastDst = getDistance(event)
                baseratio = ratio
            } else {
                // if ACTION_POINTER_UP then after finding the distance
                // we will increase the text size by 15
                val scale: Float = (getDistance(event) - bastDst) / move
                val factor = Math.pow(2.0, scale.toDouble()).toFloat()
                ratio = Math.min(1024.0f, Math.max(0.1f, baseratio * factor))
                //text.setTextSize(ratio + 15)
            }
        }
        return true
    }

    // get distance between the touch event
    private fun getDistance(event: MotionEvent): Int {
        val dx = (event.getX(0) - event.getX(1)).toInt()
        val dy = (event.getY(0) - event.getY(1)).toInt()
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }

    fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
    fun buttonClicker(){
        val button= findViewById<Button>(R.id.submitButton)
        button.setOnClickListener{
           val intent = Intent(this, GetInputs::class.java)
            intent.putExtra(GetInputs.DISPLAY_MESSAGE, "How much you bought your house for?")
            intent.putExtra(GetInputs.INPUT_UNIT, "SEK ")
            startActivity(intent)
            //finish()
        }
    }
}
