package com.proteam.renew.views

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.proteam.renew.R
import com.proteam.renew.requestModels.OnBoarding
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices
import java.io.ByteArrayOutputStream

class WorkerInformationNext2Activity : AppCompatActivity(), OnResponseListener<Any> {

    private val PICK_FILE_AADHAR = 101
    private val PICK_FILE_MEDICAL = 102
    private val PICK_FILE_DRIVING = 103


    val select_aadhar_card: ImageView by lazy { findViewById<ImageView>(R.id.select_aadhar_card) }
    val select_driving_licence: ImageView by lazy { findViewById<ImageView>(R.id.select_driving_licence) }
    val select_medical_certificate: ImageView by lazy { findViewById<ImageView>(R.id.select_medical_certificate) }
    val tv_previous_two: TextView by lazy { findViewById<TextView>(R.id.tv_previous_two) }
    val tv_submit: TextView by lazy { findViewById<TextView>(R.id.tv_submit) }

    var name: String = ""
    var gaurdian: String = ""
    var Dob: String = ""
    var gender: String = ""
    var phone_number: String = ""
    var emergency_number: String = ""
    var blood_group: String = ""
    var address: String = ""
    var state: String = ""
    var location: String = ""
    var pincode: String = ""
    var Project: String = ""
    var skill_type: String = ""
    var skill_set: String = ""
    var worker_designation: String = ""
    var doj: String = ""
    var Supervisor_name: String = ""
    var sub_contractor: String = ""
    var contractor_contact_number: String = ""
    var induction_status: String = ""
    var aadhaar_card: String = ""
    var medical_test_status: String = ""
    var report_is_ok: String = ""
    var adharimage : String = ""
    var medicalimage: String = ""
    var drivingimage: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information_next2)

        getAlldata()

        tv_previous_two.setOnClickListener {
            val intent = Intent(applicationContext, WorkerInformationNext1Activity::class.java)
            startActivity(intent)
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
            callmasterAps()
        }

    }

    private fun getAlldata() {
        val sharedPreferences = getSharedPreferences("WorkerInfoPref", Context.MODE_PRIVATE)
         name = sharedPreferences.getString("employee_name", "").toString()
         gaurdian = sharedPreferences.getString("guardian_name", "")!!
         Dob = sharedPreferences.getString("Dob", "")!!
         gender = sharedPreferences.getString("sp_gender", "")!!
         phone_number = sharedPreferences.getString("phone_number", "")!!
         emergency_number = sharedPreferences.getString("emergency_number", "")!!
         blood_group = sharedPreferences.getString("blood_group", "")!!
         address = sharedPreferences.getString("address", "")!!
         state = sharedPreferences.getString("state", "")!!
         location = sharedPreferences.getString("location", "")!!
         pincode = sharedPreferences.getString("pincode", "")!!

        val sharedPreferences2 = getSharedPreferences("WorkerInfoOnePref2", Context.MODE_PRIVATE)
        Project = sharedPreferences2.getString("Project", "")!!
        skill_type = sharedPreferences2.getString("skill_type", "")!!
        skill_set = sharedPreferences2.getString("skill_set", "")!!
        worker_designation = sharedPreferences2.getString("worker_designation", "")!!
        doj = sharedPreferences2.getString("doj", "")!!
        Supervisor_name = sharedPreferences2.getString("Supervisor_name", "")!!
        sub_contractor = sharedPreferences2.getString("sub_contractor", "")!!
        contractor_contact_number = sharedPreferences2.getString("contractor_contact_number", "")!!
        induction_status = sharedPreferences2.getString("induction_status", "")!!
        aadhaar_card = sharedPreferences2.getString("aadhaar_card", "")!!
        medical_test_status = sharedPreferences2.getString("medical_test_status", "")!!
        report_is_ok = sharedPreferences2.getString("report_is_ok", "")!!

    }

    private fun callmasterAps() {
        val onboarding = OnBoarding(
            adharimage, aadhaar_card, address, blood_group, "2", Dob, doj, "ced",
            drivingimage, gaurdian, emergency_number, contractor_contact_number, "1", "1", "testing2", name,
            gender, "cdj", induction_status, medicalimage, "2023-07-02", medical_test_status, phone_number, "sc",
            pincode, "", "1", "1", skill_type, "1", contractor_contact_number, sub_contractor, "1", worker_designation
        )
        val webServices = WebServices<Any>(this@WorkerInformationNext2Activity)
        webServices.onBoarding(WebServices.ApiType.onBoarding, onboarding)
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
                        Toast.makeText(this, "New user added", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
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
}