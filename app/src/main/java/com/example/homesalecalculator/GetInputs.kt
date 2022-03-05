package com.example.homesalecalculator

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.homesalecalculator.R.*
import java.lang.Float.parseFloat

class GetInputs : AppCompatActivity() {

    var paidAmnt: String = ""
    var soldAmnt: String = ""
    var bPercent: String = ""
    var taxPercent: String = ""
    var bankMrtg: String = ""
    var misc: String = ""
    var brokerComissionType: String = "PERCENTAGE"
    var brokerCommissionUnit: String = "%"

    companion object {
        const val DISPLAY_MESSAGE = "DISPLAY_MESSAGE"
        const val INPUT_UNIT = "INPUT_UNIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.get_inputs)

        val displayMsg = intent.getStringExtra("DISPLAY_MESSAGE")
        val inputUnit = intent.getStringExtra("INPUT_UNIT")

        val dispMsg = findViewById<TextView>(id.disp_msg)
        val getVal = findViewById<TextView>(id.get_val)
        val unitDisp = findViewById<TextView>(id.unit_disp)

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

            val button = findViewById<Button>(id.nextbtn)

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

                setContentView(layout.get_inputs)

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
                            brokerCommissionUnit = "%"
                            inputUnit = "%"
                        } else {
                            inputUnit = "SEK"
                            brokerCommissionUnit = "Kr"
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


                val dispMsg = findViewById<TextView>(id.disp_msg)
                val getVal = findViewById<TextView>(id.get_val)
                val unitDisp = findViewById<TextView>(id.unit_disp)

                dispMsg.text = displayMsg
                unitDisp.text = inputUnit
                if (nxtinputCategory == "SALE_PRICE") {
                    getVal.setVisibility(View.GONE)
                    getVal.isEnabled = false

                    val rgp = findViewById<View>(id.radio_group) as RadioGroup

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

                    with(rgp) {
                        rbn1.buttonTintList = colorStateList
                        rbn2.buttonTintList = colorStateList
                        rbn1.id = View.generateViewId()
                        rbn1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.5F)
                        rbn1.setTextColor(Color.parseColor("#000000"))
                        rbn1.setTypeface(null, Typeface.BOLD)
                        rbn1.setTypeface(null, Typeface.BOLD)
                        rbn2.id = View.generateViewId()
                        rbn2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.5F)
                        rbn2.setTextColor(Color.parseColor("#000000"))
                        rbn2.setTypeface(null, Typeface.BOLD)
                        rbn2.setTypeface(null, Typeface.BOLD)
                        rbn1.text = "Fixed Commission \n \n OR"
                        rbn2.text = "\nPercentage on Sale Value"
                        addView(rbn1)
                        addView(rbn2)
                    }

                    rgp.setOnCheckedChangeListener { group, checkedId ->
                        val radioButtonTxt = group.findViewById<RadioButton>(checkedId).text.toString().trim()
                        val text = if (radioButtonTxt == "Percentage on Sale Value") "PERCENTAGE" else "FIXED_COMMISSION"
                        brokerComissionType = text
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }

                }
                buttonClicker(nextInputCategory, getVal)

            }
        } else {


            val brokerage: Float =
                if (brokerComissionType == "PERCENTAGE") (parseFloat(bPercent) * parseFloat(soldAmnt)) / 100 else parseFloat(
                    bPercent
                )
            val profitAmount: Float = if (parseFloat(soldAmnt) > parseFloat(paidAmnt)) {
                parseFloat(soldAmnt) - parseFloat(paidAmnt)
            } else parseFloat("0")

            val taxAmount: Float =
                if (parseFloat(soldAmnt) > parseFloat(paidAmnt) && profitAmount > brokerage)
                    ((parseFloat(soldAmnt) - parseFloat((paidAmnt)) - brokerage) * parseFloat(
                        taxPercent
                    ) / 100)
                else
                    parseFloat("0")


            val totToPay: Float = parseFloat(bankMrtg) + taxAmount + brokerage + parseFloat(misc)

            val moneyFromHome = parseFloat(soldAmnt) - totToPay

            val displaySaleReportMsg: String = "You Bought your house for SEK $paidAmnt \n" +
                    "and If you sell your house for SEK $soldAmnt with a brokerage on $brokerComissionType basis of $bPercent $brokerCommissionUnit and with the House Sale Tax of $taxPercent% on the profit SEK $profitAmount (excl. Brokerage)\n" +
                    "then, \n You need to pay SEK $brokerage as the broker commission and SEK $taxAmount to the Skatteverket \n" +
                    "with that, after deducting your Mortgage SEK ${bankMrtg} and miscellaneous expenditure (Hemnet/Advert Fee) SEK ${misc}, you will end up with SEK ${moneyFromHome} from your home \n  "

            val showReportIntent = Intent(this, ShowResult::class.java)
            showReportIntent.putExtra(ShowResult.SALE_REPORT_MESSAGE, displaySaleReportMsg)
            showReportIntent.putExtra(ShowResult.IS_LOAD, "false")
            showReportIntent.putExtra(ShowResult.SALE_AMOUNT, soldAmnt)
            startActivity(showReportIntent)

        }
    }
}
