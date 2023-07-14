package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proteam.renew.R
import com.proteam.renew.responseModel.LocationResponse
import com.proteam.renew.responseModel.statesResponse
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices

class WorkerInformationActivity : AppCompatActivity(), OnResponseListener<Any> {

    val edt_employee_name: EditText by lazy { findViewById<EditText>(R.id.edt_employee_name) }
    val edt_guardian_name: EditText by lazy { findViewById<EditText>(R.id.edt_guardian_name) }
    val edt_dob: EditText by lazy { findViewById<EditText>(R.id.edt_dob) }
    val sp_gender: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_gender) }
    val edt_phone_number: EditText by lazy { findViewById<EditText>(R.id.edt_phone_number) }
    val edt_emergency_contact_Name: EditText by lazy { findViewById<EditText>(R.id.edt_emergency_contact_Name) }
    val nationality: EditText by lazy { findViewById<EditText>(R.id.nationality) }
    val sp_blood_group: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_blood_group) }
    val edt_address: EditText by lazy { findViewById<EditText>(R.id.edt_address) }
    val sp_state: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_state) }
    val sp_location: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_location) }
    val edt_pincode: EditText by lazy { findViewById<EditText>(R.id.edt_pincode) }
    val tv_next_one: TextView by lazy { findViewById<TextView>(R.id.tv_next_one) }

    //TODO Missed this one

    val edt_emergency_contactNumber: EditText by lazy { findViewById<EditText>(R.id.edt_emergency_contactNumber) }

    var progressDialog: ProgressDialog? = null
    var stateList = ArrayList<String>()
    var locationList = ArrayList<String>()
    var genderList = ArrayList<String>()
    var bloodgroup = ArrayList<String>()

    val Emp_Name = "employee_name"
    val Guardian_Name = "guardian_name"
    val Dob = "edt_dob"
    val Gender = "sp_gender"
    val PhoneNumber = "phone_number"
    val emergency_number = "emergency_number"
    val Nationality = "nationality"
    val Blood_group = "blood_group"
    val Address = "address"
    val State = "state"
    val Location = "location"
    val Pincode = "pincode"
    val emergency_contactNumber = "edt_emergency_contactNumber"
    var userid: String? = ""
    var rollid: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information)

        genderList.add("Male")
        genderList.add("Female")
        bloodgroup.add("A+")
        bloodgroup.add("A-")
        bloodgroup.add("B+")
        bloodgroup.add("B-")
        bloodgroup.add("o+")
        bloodgroup.add("o-")
        bloodgroup.add("AB+")
        bloodgroup.add("AB-")

        val sharedPreferences: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences.getString("rollid", "")!!
        userid = sharedPreferences.getString("userid", "")!!


        initilize()
    }

    private fun initilize() {
        tv_next_one.setOnClickListener {
            //val intent = Intent(applicationContext, WorkerInformationNext1Activity::class.java)
            //startActivity(intent)
            validateall()
        }

        callstatedApi()

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, stateList)
        sp_state.setAdapter(adapter)

        val adapter2 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, locationList)
        sp_location.setAdapter(adapter2)

        val adapter3 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, genderList)
        sp_gender.setAdapter(adapter3)

        val adapter4 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, bloodgroup)
        sp_blood_group.setAdapter(adapter4)
    }

    private fun validateall() {

        // Shared Pref saving and getting text in fields
        val edt_employee_name: String = edt_employee_name.text.toString()
        val edt_guardian_name: String = edt_guardian_name.text.toString()
        val edt_dob: String = edt_dob.text.toString()
        val sp_gender: String = sp_gender.text.toString()
        val edt_phone_number: String = edt_phone_number.text.toString()
        val edt_emergency_contact_Name: String = edt_emergency_contact_Name.text.toString()
        val nationality: String = nationality.text.toString()
        val sp_blood_group: String = sp_blood_group.text.toString()
        val edt_address: String = edt_address.text.toString()
        val sp_state: String = sp_state.text.toString()
        val sp_location: String = sp_location.text.toString()
        val edt_pincode: String = edt_pincode.text.toString()
        val edt_emergency_contactNumber: String = edt_emergency_contactNumber.text.toString()

        val sharedPreferences: SharedPreferences =getSharedPreferences("WorkerInfoPref", Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Emp_Name, edt_employee_name)
        editor.putString(Guardian_Name, edt_guardian_name)
        editor.putString(Dob, edt_dob)
        editor.putString(Gender, sp_gender)
        editor.putString(PhoneNumber, edt_phone_number)
        editor.putString(emergency_number, edt_emergency_contact_Name)
        editor.putString(Nationality, nationality)
        editor.putString(Blood_group, sp_blood_group)
        editor.putString(Address, edt_address)
        editor.putString(State, sp_state)
        editor.putString(Location, sp_location)
        editor.putString(Pincode, edt_pincode)
        editor.putString(emergency_contactNumber, edt_emergency_contactNumber)
        editor.commit()

        val intent = Intent(applicationContext, WorkerInformationNext1Activity::class.java)
        startActivity(intent)
    }

    private fun callstatedApi() {
        val webServices = WebServices<Any>(this@WorkerInformationActivity)
        webServices.states(WebServices.ApiType.states)

        val webServices2 = WebServices<Any>(this@WorkerInformationActivity)
        webServices2.location(WebServices.ApiType.location)
        /*progressDialog = ProgressDialog(this@WorkerInformationActivity)
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val webServices = WebServices<Any>(this@WorkerInformationActivity)
                webServices.states(WebServices.ApiType.states)
            } else {

            }
        }*/
    }

    override fun onResponse(
        response: Any?,
        URL: WebServices.ApiType?,
        isSucces: Boolean,
        code: Int
    ) {
        when (URL) {
            WebServices.ApiType.states -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val statesResponseItem = response as statesResponse
                    if (statesResponseItem?.isEmpty() == false) {
                        for (x in statesResponseItem) {
                            stateList.add(x.state_name)
                        }
                    } else {
                        Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            WebServices.ApiType.location -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val LocationResponse = response as LocationResponse
                    if (LocationResponse?.isEmpty() == false) {
                        for (x in LocationResponse) {
                            locationList.add(x.city_name)
                        }
                    } else {
                        Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT)
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