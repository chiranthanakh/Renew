package com.proteam.renew.views

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.proteam.renew.R
import java.util.Calendar

class ScanIdActivity : AppCompatActivity() {
    var etChooseTime_from: EditText? = null
    var etChooseTime_to: EditText? = null
    var timePickerDialogfrom: TimePickerDialog? = null
    var timePickerDialogto: TimePickerDialog? = null
    var calendar: Calendar? = null
    var currentHour = 0
    var currentMinute = 0
    var amPm: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_id)
        etChooseTime_from = findViewById(R.id.etChooseTime_from)
        etChooseTime_from?.setInputType(InputType.TYPE_NULL)
        etChooseTime_from?.requestFocus()
        etChooseTime_to = findViewById(R.id.etChooseTime_to)
        etChooseTime_to?.setInputType(InputType.TYPE_NULL)
        etChooseTime_to?.requestFocus()
        etChooseTime_from?.setOnClickListener(View.OnClickListener {
            calendar = Calendar.getInstance()
            currentHour = calendar?.get(Calendar.HOUR_OF_DAY)!!
            currentMinute = calendar?.get(Calendar.MINUTE)!!
            timePickerDialogfrom =
                TimePickerDialog(this@ScanIdActivity, { timePicker, hourOfDay, minutes ->
                    amPm = if (hourOfDay >= 12) {
                        "PM"
                    } else {
                        "AM"
                    }
                    etChooseTime_from?.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm)
                }, currentHour, currentMinute, false)
            timePickerDialogfrom!!.show()
        })
        etChooseTime_to?.setOnClickListener(View.OnClickListener {
            calendar = Calendar.getInstance()
            currentHour = calendar?.get(Calendar.HOUR_OF_DAY)!!
            currentMinute = calendar?.get(Calendar.MINUTE)!!
            timePickerDialogto =
                TimePickerDialog(this@ScanIdActivity, { timePicker, hourOfDay, minutes ->
                    amPm = if (hourOfDay >= 12) {
                        "PM"
                    } else {
                        "AM"
                    }
                    etChooseTime_to?.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm)
                }, currentHour, currentMinute, false)
            timePickerDialogto!!.show()
        })
    }
}