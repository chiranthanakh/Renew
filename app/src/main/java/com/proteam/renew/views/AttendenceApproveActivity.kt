package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.Adapter.AttendenceApproveListAdapter
import com.proteam.renew.R
import com.proteam.renew.requestModels.AttendancApproveRequest
import com.proteam.renew.responseModel.AttendenceListResponsce
import com.proteam.renew.responseModel.generalGesponce
import com.proteam.renew.utilitys.CheckboxListner
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices

class AttendenceApproveActivity : AppCompatActivity(), OnResponseListener<Any>, CheckboxListner {

    var userid: String = ""
    var rollid : String = ""
    var progressDialog: ProgressDialog? = null
    val rv_attendance: RecyclerView by lazy { findViewById(R.id.rv_attendance) }
    val tv_attendance_approve: TextView by lazy { findViewById(R.id.tv_attendance_approve) }
    val checkbox = HashMap<String,Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence_approve)
        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences2.getString("rollid", "")!!
        userid = sharedPreferences2.getString("userid", "")!!

        rv_attendance.layoutManager = LinearLayoutManager(this)
        callListapi()

        tv_attendance_approve.setOnClickListener {
            callAttendanceApprove()
        }

    }

    private fun callAttendanceApprove() {
        progressDialog = ProgressDialog(this@AttendenceApproveActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()

                var attendance : String = ""
                checkbox.forEach { (key, value) ->
                    if(value){
                        attendance = attendance+","+key
                    }
                    println("$key = $value")
                }
                var approve = AttendancApproveRequest(attendance.toString())
                val webServices2 = WebServices<Any>(this@AttendenceApproveActivity)
                webServices2.AttendanceApprove(WebServices.ApiType.attendanceapprove,approve)
            } else {
            }
        }    }

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
        when (URL) {

            WebServices.ApiType.attendancelist -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val attendancelist = response as AttendenceListResponsce
                    if (attendancelist?.isEmpty() == false) {
                        for (x in attendancelist) {

                        }
                        val adapter = AttendenceApproveListAdapter(attendancelist,this,getApplicationContext())
                        rv_attendance.adapter = adapter
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.attendanceapprove -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val attendanceres = response as generalGesponce
                    if (attendanceres.status == "200" ) {
                        Toast.makeText(this, "Attendance Approved successfully ", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            else -> {}
        }
    }

    override fun onCheckboxChanged(id: String, isChecked: Boolean) {
        checkbox.put(id,isChecked)
    }
}