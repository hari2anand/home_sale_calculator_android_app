package com.example.homesalecalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonClicker()
    }
    fun buttonClicker(){
        val button= findViewById<Button>(R.id.submitButton)
        button.setOnClickListener{
           val intent = Intent(this, getInputs::class.java)
            intent.putExtra(getInputs.DISPLAY_MESSAGE, "How much you bought your house for?")
            intent.putExtra(getInputs.INPUT_UNIT, "SEK ")
            startActivity(intent)
            //finish()
        }
    }
}
