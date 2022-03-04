package com.example.homesalecalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.json.JSONException
import org.json.JSONObject
import java.lang.Float.parseFloat
import java.text.SimpleDateFormat
import java.util.*

class GetInputs : AppCompatActivity() {

    var paidAmnt: String = ""
    var soldAmnt: String = ""
    var bPercent: String = ""
    var taxPercent: String = ""
    var bankMrtg: String = ""
    var misc: String = ""
    var brokerComissionType: String = "PERCENTAGE"

    companion object {
        const val DISPLAY_MESSAGE = "What is the sale price?"
        const val INPUT_UNIT = "SEK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_inputs)

        val displayMsg = intent.getStringExtra("DISPLAY_MESSAGE")
        val inputUnit = intent.getStringExtra("INPUT_UNIT")

        val dispMsg = findViewById<TextView>(R.id.disp_msg)
        val getVal = findViewById<TextView>(R.id.get_val)
        val unitDisp = findViewById<TextView>(R.id.unit_disp)

        dispMsg.text = displayMsg
        unitDisp.text = inputUnit

        buttonClicker("PURCHASE_PRICE", getVal)
    }

    fun buttonClicker(nxtinputCategory: String, inputObject: TextView) {
        val inputVal = inputObject.text
        var displayMsg = ""
        var inputUnit = ""
        var nextInputCategory = ""

        if (nxtinputCategory != "FINAL_PAGE") {

            val button = findViewById<Button>(R.id.nextbtn)

            if (nxtinputCategory == "BROKERAGE_SELECTION") button.setEnabled(true)

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
                        paidAmnt = inputVal.toString()
                        inputUnit = "SEK"
                        displayMsg =
                            "You bought your house for ${paidAmnt} \n And for How much are you planning to Sell it?"
                        nextInputCategory = "SALE_PRICE"

                    }
                    "SALE_PRICE" -> {
                        soldAmnt = inputVal.toString()
                        displayMsg =
                            "What's your agreement with the Real Estate Agent"
                        nextInputCategory = "BROKERAGE_SELECTION"

                    }
                    "BROKERAGE_SELECTION" -> {
                        //  brokerComissionType = inputVal.toString()
                        if (brokerComissionType == "PERCENTAGE") {
                            displayMsg = "What is the Broker Commission? (till ex., 2.8 %)"
                            inputUnit = "%"
                        } else {
                            inputUnit = "SEK"
                            displayMsg = "What is the Broker Commission? (till ex., SEK 44000)"
                        }
                        nextInputCategory = "BROKERAGE"

                    }
                    "BROKERAGE" -> {
                        bPercent = inputVal.toString()
                        inputUnit = "%"
                        displayMsg =
                            "What is the local Sales Tax (in %) (till ex., 22% on Profit excl Brokerage)?"
                        nextInputCategory = "SALE_TAX"

                    }
                    "SALE_TAX" -> {
                        taxPercent = inputVal.toString()
                        inputUnit = "SEK"
                        displayMsg = "Do you still have Mortgage on the house? How Much?"
                        nextInputCategory = "MORTGAGE"

                    }
                    "MORTGAGE" -> {
                        bankMrtg = inputVal.toString()
                        inputUnit = "SEK"
                        displayMsg =
                            "Do you want to add miscellaneous expenditures (like Advt/hemnet charges)"
                        nextInputCategory = "MISC"

                    }
                    "MISC" -> {
                        misc = inputVal.toString()
                        inputUnit = "SEK"
                        displayMsg =
                            "Do you want to add miscellaneous expenditures (like Advt/hemnet charges)"
                        nextInputCategory = "FINAL_PAGE"

                    }
                    else -> {
                        println("Debug Failure on unexpected case")
                    }
                }


                val dispMsg = findViewById<TextView>(R.id.disp_msg)
                val getVal = findViewById<TextView>(R.id.get_val)
                val unitDisp = findViewById<TextView>(R.id.unit_disp)

                dispMsg.text = displayMsg
                unitDisp.text = inputUnit
                if (nxtinputCategory == "SALE_PRICE") {
                    getVal.setVisibility(View.GONE)
                    getVal.isEnabled = false

                    val rgp = findViewById<View>(R.id.radio_group) as RadioGroup

                    val colorStateList = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked),
                            intArrayOf(android.R.attr.state_checked)
                        ), intArrayOf(
                            Color.DKGRAY, Color.rgb(242, 81, 112)
                        )
                    )


                    val rbn1 = RadioButton(this)
                    val rbn2 = RadioButton(this)
                    rbn1.buttonTintList = colorStateList
                    rbn2.buttonTintList = colorStateList
                    rbn1.id = View.generateViewId()
                    rbn1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.5F)
                    rbn1.setTextColor(Color.parseColor("#000000"))
                    rbn1.setTypeface(null, Typeface.BOLD)
                    rbn1.setTypeface(null, Typeface.BOLD)
                    rbn1.text = "Fixed Commission \n \n OR"
                    rbn2.id = View.generateViewId()
                    rbn2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.5F)
                    rbn2.setTextColor(Color.parseColor("#000000"))
                    rbn2.setTypeface(null, Typeface.BOLD)
                    rbn2.setTypeface(null, Typeface.BOLD)
                    rbn2.text = "\nPercentage on Sale Value"
                    rgp.addView(rbn1)
                    rgp.addView(rbn2)

                    rgp.setOnCheckedChangeListener { group, checkedId ->
                        var text = if (checkedId == 1) "FIXED_COMISSION" else "PERCENTAGE"
                        //getVal.text = text
                        brokerComissionType = text
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }

                }
                buttonClicker(nextInputCategory, getVal)

            }
        } else {

            //TODO: Celebration animation with sound

            var brokerage: Float =
                if (brokerComissionType == "PERCENTAGE") (parseFloat(bPercent) * parseFloat(soldAmnt)) / 100 else parseFloat(
                    bPercent
                )

            var taxAmount: Float = if (parseFloat(soldAmnt) > parseFloat(paidAmnt))
                ((parseFloat(soldAmnt) - parseFloat((paidAmnt)) - brokerage) * parseFloat(taxPercent) / 100)
            else
                parseFloat("0")


            var totToPay: Float = parseFloat(bankMrtg) + taxAmount + brokerage + parseFloat(misc)

            var moneyFromHome = parseFloat(soldAmnt) - totToPay

            val displayMsg: String = "You Bought your house for SEK ${paidAmnt} \n" +
                    "and If you sell your house for SEK ${soldAmnt} with a brokerage percentage of ${bPercent} and with the House Sale Tax of ${taxPercent}% on the profit\n" +
                    "then, \n You need to pay SEK ${brokerage} as the broker commission and SEK ${taxAmount} to the Skatteverket \n" +
                    "with that, after deducting your Mortgage SEK ${bankMrtg} and miscellaneous expenditure (Hemnet/Advert Fee) SEK ${misc}, you will end up with SEK ${moneyFromHome} from your home \n  "

            val showReportIntent = Intent(this, ShowResult::class.java)
            showReportIntent.putExtra(ShowResult.SALE_REPORT_MESSAGE, displayMsg)
            showReportIntent.putExtra(ShowResult.IS_LOAD,"false")
            showReportIntent.putExtra(ShowResult.SALE_AMOUNT, soldAmnt)
            startActivity(showReportIntent)

        }
    }
}
