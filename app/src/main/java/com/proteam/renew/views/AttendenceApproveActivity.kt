package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proteam.renew.R
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices

class AttendenceApproveActivity : AppCompatActivity(), OnResponseListener<Any> {

    var userid: String = ""
    var rollid : String = ""
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence_approve)
        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences2.getString("rollid", "")!!
        userid = sharedPreferences2.getString("userid", "")!!

        callListapi()

    }

    private fun callListapi() {
        progressDialog = ProgressDialog(this@AttendenceApproveActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val webServices2 = WebServices<Any>(this@AttendenceApproveActivity)
                webServices2.attendance_list(WebServices.ApiType.attendancelist, userid)
            } else {
            }
        }
    }

    override fun onResponse(
        response: Any?,
        URL: WebServices.ApiType?,
        isSucces: Boolean,
        code: Int
    ) {
        TODO("Not yet implemented")
    }
}