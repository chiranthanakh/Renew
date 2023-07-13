package com.proteam.renew.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import com.proteam.renew.R

class WorkerDetailsActivity : AppCompatActivity() {

    val edt_employee_name_details: EditText by lazy { findViewById<EditText>(R.id.edt_employee_name_details) }
    val edt_guardian_name_details: EditText by lazy { findViewById<EditText>(R.id.edt_guardian_name_details) }
    val edt_dob_details: EditText by lazy { findViewById<EditText>(R.id.edt_dob_details) }
    val sp_dop_details: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_dop_details) }
    val edt_address_details: EditText by lazy { findViewById<EditText>(R.id.edt_address_details) }
    val edt_state_details: EditText by lazy { findViewById<EditText>(R.id.edt_state_details) }


    val submit_approve: TextView by lazy { findViewById<TextView>(R.id.submit_approve) }
    val tv_reject: TextView by lazy { findViewById<TextView>(R.id.tv_reject) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_details)
    }
}