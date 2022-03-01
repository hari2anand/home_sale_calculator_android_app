package com.example.homesalecalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Float.parseFloat
import java.util.*

class getInputs : AppCompatActivity() {
    var paidAmnt: String = ""
    var soldAmnt: String = ""
    var bPercent: String = ""
    var taxPercent: String = ""
    var bankMrtg: String = ""
    var misc: String = ""

    companion object {
        const val DISPLAY_MESSAGE = "DISPLAY_MESSAGE" // What is the sale price?
        const val INPUT_UNIT = "INPUT_UNIT" // eg., SEK or %
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_inputs)

        val display_msg = intent.getStringExtra("DISPLAY_MESSAGE")
        val inputUnit = intent.getStringExtra("INPUT_UNIT")

        val disp_msg = findViewById<TextView>(R.id.disp_msg)
        val get_val = findViewById<TextView>(R.id.get_val)
        val unit_disp = findViewById<TextView>(R.id.unit_disp)

        disp_msg.text = display_msg
        unit_disp.text = inputUnit

        buttonClicker("PURCHASE_PRICE", get_val)
    }

    fun buttonClicker(nxtinputCategory: String, inputObject: TextView) {
        val inputVal = inputObject.text
        var display_msg: String = ""
        var inputUnit: String = ""
        var nextInputCategory: String = ""

        if (nxtinputCategory != "FINAL_PAGE") {

            val button = findViewById<Button>(R.id.nextbtn)

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    button.setEnabled(inputObject.text.toString().length > 0)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (start == 10) {
                        Toast.makeText(
                            applicationContext,
                            "Are you Sure about the input?",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            inputObject.addTextChangedListener(textWatcher)
            button.setOnClickListener {

                setContentView(R.layout.get_inputs)

                when (nxtinputCategory) {
                    "PURCHASE_PRICE" -> {
                        paidAmnt = inputVal.toString();
                        inputUnit = "SEK"
                        display_msg =
                            "You bought your house for ${paidAmnt} \n And for How much are you planning to Sell it?"
                        nextInputCategory = "SALE_PRICE"

                    }
                    "SALE_PRICE" -> {
                        soldAmnt = inputVal.toString()
                        inputUnit = "%"
                        display_msg = "What is the Broker Commission (in %)?"
                        nextInputCategory = "BROKERAGE"

                    }
                    "BROKERAGE" -> {
                        bPercent = inputVal.toString()
                        inputUnit = "%"
                        display_msg =
                            "What is the local Sales Tax (in %) (on Profit excl Brokerage)?"
                        nextInputCategory = "SALE_TAX"

                    }
                    "SALE_TAX" -> {
                        taxPercent = inputVal.toString()
                        inputUnit = "SEK"
                        display_msg = "Do you still have Mortgage on the house? How Much?"
                        nextInputCategory = "MORTGAGE"

                    }
                    "MORTGAGE" -> {
                        bankMrtg = inputVal.toString()
                        inputUnit = "SEK"
                        display_msg =
                            "Do you want to add miscellaneous expenditures (like Advt/hemnet charges)"
                        nextInputCategory = "MISC"

                    }
                    "MISC" -> {
                        misc = inputVal.toString()
                        inputUnit = "SEK"
                        display_msg =
                            "Do you want to add miscellaneous expenditures (like Advt/hemnet charges)"
                        nextInputCategory = "FINAL_PAGE"

                    }
                    else -> {
                        println("Debug Failure on unexpected case")
                    }
                }


                val disp_msg = findViewById<TextView>(R.id.disp_msg)
                val get_val = findViewById<TextView>(R.id.get_val)
                val unit_disp = findViewById<TextView>(R.id.unit_disp)

                disp_msg.text = display_msg
                unit_disp.text = inputUnit
                buttonClicker(nextInputCategory, get_val)
                //finish()
            }
        } else {

            var brokerage: Float = (parseFloat(bPercent) * parseFloat(soldAmnt)) / 100

            var taxAmount: Float = if (parseFloat(soldAmnt) > parseFloat(paidAmnt))
                ((parseFloat(soldAmnt) - parseFloat((paidAmnt)) - brokerage) * parseFloat(taxPercent) / 100)
            else
                parseFloat("0")


            var totToPay: Float = parseFloat(bankMrtg) + taxAmount + brokerage + parseFloat(misc)

            var moneyFromHome = parseFloat(soldAmnt) - totToPay

            //setContentView(R.layout.show_result)

            setContentView(R.layout.show_result)

            val display_msg:String = "You Bought your house for SEK ${paidAmnt} \n" +
                    "and If you sell your house for SEK ${soldAmnt} with a brokerage percentage of ${bPercent} and with the House Sale Tax of ${taxPercent}% on the profit\n" +
                    "then, \n You need to pay SEK ${brokerage} as the broker commission and SEK ${taxAmount} to the Skatteverket \n" +
                    "with that, after deducting your Mortgage SEK ${bankMrtg} and miscellaneous expenditure (Hemnet/Advert Fee) SEK ${misc}, you will end up with SEK ${moneyFromHome} from your home"

            val disp_msg = findViewById<TextView>(R.id.disp_msg)

            disp_msg.text = display_msg


        }
    }
}