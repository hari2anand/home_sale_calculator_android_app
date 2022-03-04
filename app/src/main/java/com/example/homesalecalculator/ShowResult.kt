package com.example.homesalecalculator

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ShowResult : AppCompatActivity() {
    val intSaveSize: Int = 20

    companion object {
        const val SALE_REPORT_MESSAGE: String = "SALE_REPORT_MESSAGE"
        const val SALE_AMOUNT: String = "SALE_AMOUNT"
        const val IS_LOAD: String = "IS_LOAD"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_result)

        val displayMsg = intent.getStringExtra("SALE_REPORT_MESSAGE")
        val isLoad = intent.getStringExtra("IS_LOAD") ?: "false"
        val soldAmnt = intent.getStringExtra("SALE_AMOUNT") ?: ""


        val dispMsg = findViewById<TextView>(R.id.disp_msg)
        dispMsg.setTypeface(null, Typeface.BOLD)
        dispMsg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.5F)
        dispMsg.text = displayMsg


        val homeButton = findViewById<ImageButton>(R.id.home_btn)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val saveButton = findViewById<ImageButton>(R.id.saveButton)
        if (isLoad != "IS_LOAD") {
            //TODO: Celebration animation with sound

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
                    val saveKeyValue: String = saveKeyField.text.toString()

                    val sharedPref = this.getSharedPreferences(
                        "savedReport",
                        Context.MODE_PRIVATE
                    )

                    var jsonSaleReportsPrefString = sharedPref.getString("ReportJSON", "")
                    val keyListsPrefString = sharedPref.getString("keyLists", "")

                    if (jsonSaleReportsPrefString == "") jsonSaleReportsPrefString = "{}"

                    var saveKeyList: Array<String> = TextUtils.split(keyListsPrefString, ",")

                    try {
                        val objSaleReport = JSONObject(jsonSaleReportsPrefString.toString())
                        saveKeyList = arrAppend(saveKeyList, saveKeyValue)

                        if (saveKeyList.size > intSaveSize) {
                            Toast.makeText(
                                this,
                                "Can keep only ${intSaveSize} Records, So discarding the report named: ${saveKeyList[0].trim()}",
                                Toast.LENGTH_LONG
                            ).show()
                            objSaleReport.remove(saveKeyList[0].trim())
                            saveKeyList = arrRemove(saveKeyList, 0)
                        }

                        objSaleReport.put(saveKeyValue, displayMsg)
                        with(sharedPref.edit()) {
                            putString("ReportJSON", objSaleReport.toString())
                            putString(
                                "keyLists",
                                saveKeyList.contentDeepToString().replace("[", "").replace("]", "")
                            )
                            commit()
                        }
                        Toast.makeText(
                            this,
                            "Record: ${saveKeyValue} Saved to Memory",
                            Toast.LENGTH_LONG
                        ).show();
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    alertDialog.cancel()
                    saveButton.isVisible = false
                    saveButton.isEnabled = false
                })
                cancelDialogButton.setOnClickListener(View.OnClickListener {
                    alertDialog.cancel()
                })
            }
        }
        else {
            saveButton.isEnabled = false
            saveButton.isVisible = false
        }

    }
}

fun arrAppend(arr: Array<String>, element: String): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

fun arrRemove(arr: Array<String>, index: Int): Array<String> {
    if (index < 0 || index >= arr.size) {
        return arr
    }

    val result = arr.toMutableList()
    result.removeAt(index)
    return result.toTypedArray()
}
