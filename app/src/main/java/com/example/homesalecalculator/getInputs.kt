package com.example.homesalecalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import java.lang.Float.parseFloat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class getInputs : AppCompatActivity() {

    var paidAmnt: String = ""
    var soldAmnt: String = ""
    var bPercent: String = ""
    var taxPercent: String = ""
    var bankMrtg: String = ""
    var misc: String = ""
    var brokerComissionType: String = "PERCENTAGE"

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

    @SuppressLint("WrongViewCast")
    fun buttonClicker(nxtinputCategory: String, inputObject: TextView) {
        val inputVal = inputObject.text
        var display_msg: String = ""
        var inputUnit: String = ""
        var nextInputCategory: String = ""

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
                        paidAmnt = inputVal.toString();
                        inputUnit = "SEK"
                        display_msg =
                            "You bought your house for ${paidAmnt} \n And for How much are you planning to Sell it?"
                        nextInputCategory = "SALE_PRICE"

                    }
                    "SALE_PRICE" -> {
                        soldAmnt = inputVal.toString()
                        display_msg =
                            "What's your agreement with the Real Estate Agent"
                        nextInputCategory = "BROKERAGE_SELECTION"

                    }
                    "BROKERAGE_SELECTION" -> {
                        //  brokerComissionType = inputVal.toString()
                        if (brokerComissionType == "PERCENTAGE") {
                            display_msg = "What is the Broker Commission? (till ex., 2.8 %)"
                            inputUnit = "%"
                        } else {
                            inputUnit = "SEK"
                            display_msg = "What is the Broker Commission? (till ex., SEK 44000)"
                        }
                        nextInputCategory = "BROKERAGE"

                    }
                    "BROKERAGE" -> {
                        bPercent = inputVal.toString()
                        inputUnit = "%"
                        display_msg =
                            "What is the local Sales Tax (in %) (till ex., 22% on Profit excl Brokerage)?"
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
                if (nxtinputCategory == "SALE_PRICE") {
                    get_val.setVisibility(View.GONE)
                    get_val.isEnabled = false

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
                        //get_val.text = text
                        brokerComissionType = text
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }

                }
                buttonClicker(nextInputCategory, get_val)

            }
        } else {

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

            //setContentView(R.layout.show_result)

            setContentView(R.layout.show_result)

            val display_msg: String = "You Bought your house for SEK ${paidAmnt} \n" +
                    "and If you sell your house for SEK ${soldAmnt} with a brokerage percentage of ${bPercent} and with the House Sale Tax of ${taxPercent}% on the profit\n" +
                    "then, \n You need to pay SEK ${brokerage} as the broker commission and SEK ${taxAmount} to the Skatteverket \n" +
                    "with that, after deducting your Mortgage SEK ${bankMrtg} and miscellaneous expenditure (Hemnet/Advert Fee) SEK ${misc}, you will end up with SEK ${moneyFromHome} from your home \n  "

            val disp_msg = findViewById<TextView>(R.id.disp_msg)
            disp_msg.setTypeface(null, Typeface.BOLD)
            disp_msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.5F)
            disp_msg.text = display_msg

            val saveButton = findViewById<ImageButton>(R.id.saveButton)


            saveButton.setOnClickListener {

                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                val layoutInflater = LayoutInflater.from(this)
                var popupInputDialogView: View = layoutInflater.inflate(R.layout.save_dialog, null)
                alertDialogBuilder.setTitle("Do you want to Save this Report ? ");
                alertDialogBuilder.setIcon(android.R.drawable.ic_menu_save);
                alertDialogBuilder.setCancelable(true);
                val saveTextName =
                    popupInputDialogView.findViewById<TextView>(R.id.saveKey) as EditText
                alertDialogBuilder.setView(popupInputDialogView);
                val alertDialog: AlertDialog = alertDialogBuilder.create();
                alertDialog.show();
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                saveTextName.setText("calculation_on_sale_sek_${soldAmnt}_" + timeStamp)
                val saveDataBtn = popupInputDialogView.findViewById<Button>(R.id.save_btn)
                val cancelDialogButton = popupInputDialogView.findViewById<Button>(R.id.cancel_btn)
                saveDataBtn.setOnClickListener(View.OnClickListener {
                    val saveKeyField = popupInputDialogView.findViewById<TextView>(R.id.saveKey)
                    val saveKey: String = saveKeyField.getText().toString()

                    val saveKeyNameArray = arrayOf("Save Key", "Calculation Info")
                    val dataArr = arrayOf(saveKey, display_msg)

                    //TODO: Fetch array from pref (if not create one)
                    //TODO: check for the size and Delete/Remove the first item in array if exceeds size 14
                    //TODO: Add new key to the array
                    //TODO: Fetch map(key value pair) from pref
                    //TODO: Delete key/value pair (if exceeds 15) shifted key
                    //TODO: Add new key value pai
                    //TODO: OnloadData find the name and fetch it
                    val sharedPref = this.getSharedPreferences(
                        "savedReport",
                        Context.MODE_PRIVATE
                    ) //?: return ArrayList<Map<String, Any?>>()

                    //val itemDataList = ArrayList<Map<String, Any?>>()
                    val serialized = sharedPref.getString("ReportMap", "Save Key")
                    Toast.makeText(this, serialized, Toast.LENGTH_LONG).show();
                    val itemDataList: MutableList<Array<String>> =
                        (mutableListOf(TextUtils.split(serialized, ",")))
                    itemDataList.add(dataArr)
                    //val saveKeyLen = saveKeyNameArray.size
                   /* for (i in 0 until saveKeyLen) {
                        val listItemMap: MutableMap<String, Any?> = HashMap()
                        listItemMap["title"] = saveKeyNameArray[i]
                        listItemMap["data"] = dataArr[i]
                        itemDataList.add(listItemMap)
                    }*/
                    //TODO: Save the array back to Preference


                    with(sharedPref.edit()) {
                        putString("ReportMap", TextUtils.join(",", itemDataList));
                        commit()
                    }

                    val checkitemDataList = sharedPref.getString("ReportMap", "Save Key")

                    Toast.makeText(this, checkitemDataList, Toast.LENGTH_LONG).show()
                    Toast.makeText(this, serialized, Toast.LENGTH_LONG).show()

                    /*val sharedPref = this?.getSharedPreferences("savedReport", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putStringArrayList("test", values)(itemDataList.toString(), "savedReport")
                        apply()
                    }*/

                    alertDialog.cancel()
                })
                cancelDialogButton.setOnClickListener(View.OnClickListener {
                    alertDialog.cancel()
                })
            }

        }

    }

    fun saveCalculation() {

    }

    fun remove(arr: IntArray, index: Int): IntArray {
        if (index < 0 || index >= arr.size) {
            return arr
        }

        val result = arr.toMutableList()
        result.removeAt(index)
        return result.toIntArray()
    }

}

