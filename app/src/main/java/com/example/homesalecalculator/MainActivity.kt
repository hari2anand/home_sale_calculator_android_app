package com.example.homesalecalculator

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextButtonClicker()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.load_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info_menu -> {
                Toast.makeText(
                    this,
                    "Harish's Home Sale Calculator App V2.0.0",
                    Toast.LENGTH_LONG
                ).show()
            }
            R.id.load_menu -> {
                loadButtonClicker()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    fun nextButtonClicker() {
        val button = findViewById<Button>(R.id.submitButton)
        button.setOnClickListener {
            val intent = Intent(this, GetInputs::class.java)
            intent.putExtra(GetInputs.DISPLAY_MESSAGE, "How much you bought your house for?")
            intent.putExtra(GetInputs.INPUT_UNIT, "SEK ")
            startActivity(intent)
        }
    }

    fun loadButtonClicker() {

        var displayMsg = ""

        val sharedPref = this.getSharedPreferences(
            "savedReport",
            Context.MODE_PRIVATE
        )

        val jsonSaleReportsPrefString = sharedPref.getString("ReportJSON", "")
        val objSaleReport = JSONObject(jsonSaleReportsPrefString.toString())
        val keyListsPrefString = sharedPref.getString("keyLists", "")

        if (jsonSaleReportsPrefString == "" && keyListsPrefString == "") Toast.makeText(
            this,
            "No Saved Reports Found",
            Toast.LENGTH_LONG
        )
        else {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val layoutInflater = LayoutInflater.from(this)
            val popupInputDialogView: View = layoutInflater.inflate(R.layout.base_empty_page, null)
            alertDialogBuilder.setTitle("Saved Reports");
            alertDialogBuilder.setIcon(android.R.drawable.ic_menu_save);
            alertDialogBuilder.setCancelable(true);
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ), intArrayOf(
                    Color.DKGRAY, Color.rgb(242, 81, 112)
                )
            )
            val radioGroup = popupInputDialogView.findViewById<RadioGroup>(R.id.base_radio_group)
            val saveKeyList: Array<String> = TextUtils.split(keyListsPrefString, ",")

            saveKeyList.forEach {
                val rbn1 = RadioButton(this)
                rbn1.buttonTintList = colorStateList
                rbn1.id = View.generateViewId()
                rbn1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.5F)
                rbn1.setTextColor(Color.parseColor("#000000"))
                rbn1.setTypeface(null, Typeface.BOLD)
                rbn1.setTypeface(null, Typeface.BOLD)
                rbn1.text = it.trim()
                radioGroup.addView(rbn1)
            }

            alertDialogBuilder.setView(popupInputDialogView);
            val alertDialog: AlertDialog = alertDialogBuilder.create();
            alertDialog.show();
            radioGroup.setOnCheckedChangeListener { radioGroup, int ->

                val reportSaveKey = findViewById<RadioButton>(int).text.toString().trim()
                displayMsg = objSaleReport[reportSaveKey].toString().trim()
                //getVal.text = text

                Toast.makeText(
                    applicationContext,
                    "Showing Saved Result titled - $reportSaveKey",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val showReportIntent = Intent(this, ShowResult::class.java)
            showReportIntent.putExtra(ShowResult.SALE_REPORT_MESSAGE, displayMsg)
            showReportIntent.putExtra(ShowResult.IS_LOAD, "true")
            startActivity(showReportIntent)
        }

    }
}
