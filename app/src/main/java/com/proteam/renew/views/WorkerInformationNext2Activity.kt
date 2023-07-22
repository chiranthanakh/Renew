package com.proteam.renew.views

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proteam.renew.requestModels.ApprovalRequest
import com.proteam.renew.requestModels.RejectRequest
import com.proteam.renew.R
import com.proteam.renew.requestModels.OnBoarding
import com.proteam.renew.responseModel.generalGesponce
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class WorkerInformationNext2Activity : AppCompatActivity(), OnResponseListener<Any> {

    private val PICK_FILE_AADHAR = 101
    private val PICK_FILE_MEDICAL = 102
    private val PICK_FILE_DRIVING = 103
    var progressDialog: ProgressDialog? = null

    val select_aadhar_card: ImageView by lazy { findViewById<ImageView>(R.id.select_aadhar_card) }
    val select_driving_licence: ImageView by lazy { findViewById<ImageView>(R.id.select_driving_licence) }
    val select_medical_certificate: ImageView by lazy { findViewById<ImageView>(R.id.select_medical_certificate) }
    val tv_previous_two: TextView by lazy { findViewById<TextView>(R.id.tv_previous_two) }
    val tv_submit: TextView by lazy { findViewById<TextView>(R.id.tv_submit) }
    val ll_driving_lic : LinearLayout by lazy { findViewById(R.id.ll_driving_lic) }
    val tv_update: TextView by lazy { findViewById((R.id.tv_update)) }

    var name: String = ""
    var gaurdian: String = ""
    var Dob: String = ""
    var profilepic = ""
    var gender: String = ""
    var phone_number: String = ""
    var nationality: String = ""
    var emergency_number: String = ""
    var emergency_name: String = ""
    var blood_group: String = ""
    var address: String = ""
    var state: String = ""
    var location: String = ""
    var pincode: String = ""
    var Project: String = ""
    var skill_type: String = ""
    var skill_set: String = ""
    var worker_designation: String = ""
    var driving_license: String = ""
    var doj: String = ""
    var ex_month: String = ""
    var ex_year: String = ""
    var Supervisor_name: String = ""
    var sub_contractor: String = ""
    var contractor_contact_number: String = ""
    var induction_status: String = ""
    var aadhaar_card: String = ""
    var medical_test_status: String = ""
    var report_is_ok: String = ""
    var training: String = ""
    var adharimage : String = ""
    var medicalimage: String = ""
    var drivingimage: String = ""
    var userid: String = ""
    var rollid: String = ""
    var induction_date = ""
    var workerid = ""
    var remarks = ""
    var medical_date = ""
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information_next2)

        getAlldata()
        val sharedPreferences: SharedPreferences =getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
        val type = sharedPreferences.getBoolean("edit", false)!!
        val approval = sharedPreferences.getBoolean("approval",false)
        val workerid = sharedPreferences.getString("workerid","")

        val sharedPreferences1: SharedPreferences =getSharedPreferences("updateworker", Context.MODE_PRIVATE)!!
         var aadharurl = sharedPreferences1.getString("aadharpic", "")!!
        var drivingurl = sharedPreferences1.getString("drivinglicpic", "")!!
        var medicalurl = sharedPreferences1.getString("medicalpic", "")!!


        if(type) {
            if (aadharurl != "") {
                val url = "https://gp.proteam.co.in/" + aadharurl
                Picasso.get().load(url).into(select_aadhar_card)
            }
            if (drivingurl != "") {
                val url = "https://gp.proteam.co.in/" + drivingurl
                Picasso.get().load(url).into(select_driving_licence)
            }
            if (medicalurl != "") {
                val url = "https://gp.proteam.co.in/" + medicalurl
                Picasso.get().load(url).into(select_medical_certificate)
            }
        }
        bottomnavigation()
        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        userid = sharedPreferences2.getString("userid", "")!!
        rollid = sharedPreferences2.getString("rollid", "")!!


        if(approval){
            tv_previous_two.text = "Reject"
            tv_submit.text = "Approve"
        }else{

        }
        if(type == false && !approval){
            tv_update.visibility = View.GONE
        }
        tv_update.setOnClickListener {
            callupdateApi(workerid)
        }

        tv_previous_two.setOnClickListener {
            if(approval){
                workerid?.let { it1 -> callrejectApi(it1) }
            }else {
                val intent = Intent(applicationContext, WorkerInformationNext1Activity::class.java)
                startActivity(intent)
            }
        }

        select_aadhar_card.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_AADHAR)
        }

        select_driving_licence.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_DRIVING)
        }

        select_medical_certificate.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_MEDICAL)
        }

        tv_submit.setOnClickListener {
            if(approval){
                workerid?.let { it1 -> callaproveapi(it1) }
            } else {
                if (type == true) {
                    callupdateApi(workerid)
                } else {
                    callmasterAps()

                }
            }
        }

    }


    private fun getAlldata() {
        val sharedPreferences = getSharedPreferences("WorkerInfoPref", Context.MODE_PRIVATE)
         name = sharedPreferences.getString("employee_name", "").toString()
         gaurdian = sharedPreferences.getString("guardian_name", "")!!
         Dob = sharedPreferences.getString("Dob", "")!!
         gender = sharedPreferences.getString("sp_gender", "")!!
         phone_number = sharedPreferences.getString("phone_number", "")!!
         emergency_number = sharedPreferences.getString("emergency_contactNumber", "")!!
         blood_group = sharedPreferences.getString("blood_group", "")!!
         address = sharedPreferences.getString("address", "")!!
         nationality = sharedPreferences.getString("nationality","")!!
         state = sharedPreferences.getString("state", "")!!
         location = sharedPreferences.getString("location", "")!!
         pincode = sharedPreferences.getString("pincode", "")!!
         profilepic = sharedPreferences.getString("profile","")!!
         emergency_name = sharedPreferences.getString("emergency_contact_name","")!!

        val sharedPreferences2 = getSharedPreferences("WorkerInfoOnePref2", Context.MODE_PRIVATE)
        Project = sharedPreferences2.getString("project", "")!!
        skill_type = sharedPreferences2.getString("skill_type", "")!!
        skill_set = sharedPreferences2.getString("sp_skill_set", "")!!
        worker_designation = sharedPreferences2.getString("worker_designation", "")!!
        doj = sharedPreferences2.getString("doj", "")!!
        Supervisor_name = sharedPreferences2.getString("Supervisor_name", "")!!
        sub_contractor = sharedPreferences2.getString("sub_contractor", "")!!
        contractor_contact_number = sharedPreferences2.getString("contractor_contact_number", "")!!
        induction_status = sharedPreferences2.getString("induction_status", "")!!
        aadhaar_card = sharedPreferences2.getString("aadhaar_card", "")!!
        medical_test_status = sharedPreferences2.getString("medical_test_status", "")!!
        report_is_ok = sharedPreferences2.getString("report_is_ok", "")!!
        training = sharedPreferences2.getString("training", "")!!
        driving_license = sharedPreferences2.getString("driving_licence", "")!!
        ex_year = sharedPreferences2.getString("exp_years", "")!!
        ex_month = sharedPreferences2.getString("exp_months", "")!!
        induction_date = sharedPreferences2.getString("induction_date", "")!!
        workerid = sharedPreferences2.getString("workerid", "")!!
        remarks = sharedPreferences2.getString("edt_remarks","")!!
        medical_date = sharedPreferences2.getString("medical_test_date","")!!


        if(driving_license == ""){
            ll_driving_lic.visibility = View.GONE
        }
    }

    private fun callaproveapi(workerid: String) {
        progressDialog = ProgressDialog(this@WorkerInformationNext2Activity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val approveRequest = ApprovalRequest(workerid, induction_date,induction_status,report_is_ok,userid )
                val webServices = WebServices<Any>(this@WorkerInformationNext2Activity)
                webServices.Approve(WebServices.ApiType.approve, approveRequest)
            } else {
            }
        }
    }

    private fun callrejectApi(workerid: String) {
        progressDialog = ProgressDialog(this@WorkerInformationNext2Activity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val rejectrequest = RejectRequest(workerid, remarks,userid )
                val webServices = WebServices<Any>(this@WorkerInformationNext2Activity)
                webServices.Reject(WebServices.ApiType.reject, rejectrequest)
            } else {
            }
        }
    }

    private fun callmasterAps() {
        progressDialog = ProgressDialog(this@WorkerInformationNext2Activity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val onboarding = OnBoarding(
                    adharimage, aadhaar_card, address, blood_group, location, Dob, doj, driving_license,
                    drivingimage, emergency_name, emergency_number, contractor_contact_number, ex_month, ex_year, gaurdian, name,
                    gender, induction_date, induction_status, medicalimage, medical_date, medical_test_status, phone_number, nationality,
                    pincode, profilepic, Project, skill_set, skill_type, state, contractor_contact_number, sub_contractor, userid, worker_designation
                )
                val webServices = WebServices<Any>(this@WorkerInformationNext2Activity)
                webServices.onBoarding(WebServices.ApiType.onBoarding, onboarding)
            } else {
            }
        }
    }

    private fun callupdateApi(workerid: String?) {
        progressDialog = ProgressDialog(this@WorkerInformationNext2Activity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val onboarding = OnBoarding(
                    adharimage, aadhaar_card, address, blood_group, location, Dob, doj, driving_license,
                    drivingimage, emergency_name, emergency_number, contractor_contact_number, ex_month, ex_month, gaurdian, name,
                    gender, induction_date, induction_status, medicalimage, medical_date, medical_test_status, phone_number, nationality,
                    pincode, profilepic, Project, skill_set, skill_type, state, contractor_contact_number, sub_contractor, userid, worker_designation
                )
                val webServices = WebServices<Any>(this@WorkerInformationNext2Activity)
                webServices.workerUpdate(WebServices.ApiType.update, onboarding,workerid)
            } else {
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_AADHAR && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageUri: Uri = uri
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                select_aadhar_card.setImageBitmap(bitmap)
                 adharimage = convertImageUriToBase64(contentResolver, imageUri)
            }
        }else if (requestCode == PICK_FILE_DRIVING && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageUri: Uri = uri
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                select_driving_licence.setImageBitmap(bitmap)
                drivingimage = convertImageUriToBase64(contentResolver, imageUri)
            }
        } else  if (requestCode == PICK_FILE_MEDICAL && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageUri: Uri = uri
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                select_medical_certificate.setImageBitmap(bitmap)
                medicalimage = convertImageUriToBase64(contentResolver, imageUri)
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
            WebServices.ApiType.onBoarding -> {
                if (isSucces) {
                    if(response == true){
                        showSuccessDialog("New user added")
                        Toast.makeText(this, "New user added", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        showFailureDialog("Unable to add user")
                        Toast.makeText(this, "User not added", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.approve -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces && response != null) {
                    val gresponse = response as generalGesponce
                    if (gresponse?.status == "200") {
                        showSuccessDialog("Approved Successfully")
                    } else {
                        Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your login credentials ", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            WebServices.ApiType.update -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces && response != null) {
                    val gresponse = response as generalGesponce
                    if (gresponse?.status == "200") {
                        showSuccessDialog("updated Successfully")
                    } else {
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT)
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

    fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }

    fun showSuccessDialog(s: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage(s)
        builder.setPositiveButton("OK") { dialog, which ->
            val intent = Intent(this@WorkerInformationNext2Activity, MainActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intentScan = Intent(this@WorkerInformationNext2Activity, MainActivity::class.java)
                    startActivity(intentScan)
                    finish()
                    true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@WorkerInformationNext2Activity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@WorkerInformationNext2Activity, ApprovalsActivity::class.java)
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
                        val intentScan = Intent(this@WorkerInformationNext2Activity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }


    fun showFailureDialog(s: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Failure!")
        builder.setMessage("s")
        builder.setPositiveButton("OK") { dialog, which ->
            // Perform any desired action after the user clicks the OK button
            dialog.dismiss() // Dismiss the dialog
        }
        val dialog = builder.create()
        dialog.show()
    }

}