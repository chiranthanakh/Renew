package com.proteam.renew.views

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.proteam.renew.R
import com.proteam.renew.requestModels.AttendanceRequest
import com.proteam.renew.responseModel.EmployeedetailResponsce
import com.proteam.renew.responseModel.ViewActivityMasterResponsce
import com.proteam.renew.responseModel.ViewProjectMaster
import com.proteam.renew.responseModel.generalGesponce
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices
import java.io.ByteArrayOutputStream
import java.util.Calendar

class ScanIdActivity : AppCompatActivity(), OnResponseListener<Any> {
    var etChooseTime_from: EditText? = null
    var etChooseTime_to: EditText? = null
    var timePickerDialogfrom: TimePickerDialog? = null
    var timePickerDialogto: TimePickerDialog? = null
    var calendar: Calendar? = null
    var currentHour = 0
    var currentMinute = 0
    var amPm: String? = null
    private lateinit var qrCodeScanner: IntentIntegrator
    val qr_scan: TextView by lazy { findViewById(R.id.qr_scan) }
    var userid: String = ""
    var rollid : String = ""
    private lateinit var bottomNavigationView: BottomNavigationView
    var progressDialog: ProgressDialog? = null
    var projectslist = ArrayList<String>()
    var projectmap = HashMap<String, String>()
    var projectsreverselist = HashMap<String, String>()
    var activitylist = ArrayList<String>()
    var activitymap = HashMap<String, String>()
    var activityreverselist = HashMap<String, String>()
    val sp_project: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.project) }
    val sp_activity: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_activity) }
    val img_work_permit: ImageView by lazy { findViewById(R.id.img_work_permit) }
    var workPermit : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_id)
        etChooseTime_from = findViewById(R.id.etChooseTime_from)
        etChooseTime_from?.setInputType(InputType.TYPE_NULL)
        etChooseTime_from?.requestFocus()
        etChooseTime_to = findViewById(R.id.etChooseTime_to)
        etChooseTime_to?.setInputType(InputType.TYPE_NULL)
        etChooseTime_to?.requestFocus()
        qrCodeScanner = IntentIntegrator(this)

        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences2.getString("rollid", "")!!
        userid = sharedPreferences2.getString("userid", "")!!


        initilize()
        bottomnavigation()
        callApis()

    }

    private fun callApis() {

        val webServices = WebServices<Any>(this@ScanIdActivity)
        webServices.projects(WebServices.ApiType.projects)

        val webServices2 = WebServices<Any>(this@ScanIdActivity)
        webServices2.activity(WebServices.ApiType.activitys)
    }

    private fun initilize() {

        img_work_permit.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(intent, 101)
        }

        qr_scan.setOnClickListener{
            scanQRCode()
        }
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
        })    }

    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {

                    val intentScan = Intent(this@ScanIdActivity, MainActivity::class.java)
                    startActivity(intentScan)
                    finish()

                    true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@ScanIdActivity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@ScanIdActivity, ApprovalsActivity::class.java)
                        val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("nav", true)
                        editor.commit()
                        startActivity(intentapprovals)
                        finish()
                    }
                    // Handle the Profile action
                    true
                }
                R.id.it_worker -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@ScanIdActivity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }

    fun scanQRCode() {
        qrCodeScanner.setOrientationLocked(false)
        qrCodeScanner.createScanIntent()
        qrCodeScanner.initiateScan()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageUri: Uri = uri
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                img_work_permit.setImageBitmap(bitmap)
                workPermit = convertImageUriToBase64(contentResolver, imageUri)
            }
        } else {
            val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents != null) {
                    val scannedData = result.contents
                    Toast.makeText(this, scannedData, Toast.LENGTH_SHORT)
                        .show()
                    callempdetail(scannedData)
                   // showCustomDialog()
                    //validation(scannedData)
                } else {

                }
            }
        }
    }

    private fun validation(details : EmployeedetailResponsce){
        var projectid = projectmap.get(sp_project.text.toString())
        var activityid = activitymap.get(sp_activity.text.toString())
        if (!TextUtils.isEmpty(projectid)) {
            if (!TextUtils.isEmpty(activityid)) {
                if (!TextUtils.isEmpty(projectid)) {
                    if (!TextUtils.isEmpty(projectid)) {
                        if (!TextUtils.isEmpty(projectid)) {
                            if (!TextUtils.isEmpty(projectid)) {
                                callAttendanceApi(details, projectid, activityid)
                            } else {
                                Toast.makeText(this, "please select project id", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else {
                            Toast.makeText(this, "please select project id", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                        Toast.makeText(this, "please select project id", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(this, "please select project id", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(this, "please select Activity", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            Toast.makeText(this, "please select project", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun callempdetail(scannedData: String) {
        progressDialog = ProgressDialog(this@ScanIdActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                 val webServices = WebServices<Any>(this@ScanIdActivity)
                 webServices.empdetails(WebServices.ApiType.empdetails, scannedData)
            } else {
            }
        }
    }
    private fun callAttendanceApi(
        details: EmployeedetailResponsce,
        projectid: String?,
        activityid: String?
    ) {
        progressDialog = ProgressDialog(this@ScanIdActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val attendancerequest = AttendanceRequest(
                    activityid.toString(),
                    "P",
                    details.employer_contractor_name,
                    "",
                    details.id,
                    etChooseTime_from?.text.toString(),
                    projectid.toString(),
                    etChooseTime_from?.text.toString(),
                    userid,
                    workPermit
                )
                val webServices = WebServices<Any>(this@ScanIdActivity)
                webServices.attendance(WebServices.ApiType.attendence, attendancerequest)
            } else {
            }
        }
    }

    fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }



    private fun showCustomDialog(details: EmployeedetailResponsce) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.attenence_dilaog)
        dialog.setCancelable(false)
        var profile = dialog?.findViewById<ImageView>(R.id.iv_profile)
        var name = dialog.findViewById<TextView>(R.id.tv_name)
        var empid = dialog.findViewById<TextView>(R.id.tv_id)
        var save = dialog.findViewById<TextView>(R.id.tv_save)
        var cancel = dialog.findViewById<TextView>(R.id.tv_cancel)

        name.setText(details.father_name)
        empid.setText(details.id)

        save.setOnClickListener {
            validation(details)
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResponse(
        response: Any?,
        URL: WebServices.ApiType?,
        isSucces: Boolean,
        code: Int
    ) {
        when (URL) {

            WebServices.ApiType.projects -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val viewProjectMaster = response as ViewProjectMaster
                    if (viewProjectMaster?.isEmpty() == false) {
                        for (x in viewProjectMaster) {
                            projectslist.add(x.project_name)
                            projectmap.put(x.project_name,x.id)
                            projectsreverselist.put(x.id,x.project_name)
                        }
                        sp_project.setOnClickListener {
                            val adapter2 = ArrayAdapter(this,
                                android.R.layout.simple_list_item_1, projectslist)
                            sp_project.setAdapter(adapter2)
                        }
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.activitys -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val viewActivityresponsce = response as ViewActivityMasterResponsce
                    if (viewActivityresponsce?.isEmpty() == false) {
                        for (x in viewActivityresponsce) {
                            activitylist.add(x.activity_name)
                             activitymap.put(x.activity_name,x.activity_id)
                            activityreverselist.put(x.activity_id,x.activity_name)
                        }
                        sp_activity.setOnClickListener {
                            val adapter2 = ArrayAdapter(this,
                                android.R.layout.simple_list_item_1, activitylist)
                            sp_activity.setAdapter(adapter2)
                        }
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.empdetails -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val empdetail = response as EmployeedetailResponsce
                    if (!empdetail.full_name.isEmpty()) {
                        showCustomDialog(empdetail)
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.attendence -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val attendanceres = response as generalGesponce
                    if (attendanceres.status == "200" ) {
                        Toast.makeText(this, "Attendance added successfully ", Toast.LENGTH_SHORT)
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
}