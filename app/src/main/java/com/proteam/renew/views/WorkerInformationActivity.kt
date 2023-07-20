package com.proteam.renew.views

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proteam.renew.R
import com.proteam.renew.responseModel.LocationResponse
import com.proteam.renew.responseModel.LocationResponseItem
import com.proteam.renew.responseModel.statesResponse
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Locale


class WorkerInformationActivity : AppCompatActivity(), OnResponseListener<Any>, DatePickerDialog.OnDateSetListener {

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
    val edt_emergency_contactNumber: EditText by lazy { findViewById<EditText>(R.id.edt_emergency_contactNumber) }
    val iv_profile_image: ImageView by lazy { findViewById(R.id.iv_nav_image) }
    var progressDialog: ProgressDialog? = null
    var stateList = ArrayList<String>()
    var locationList = ArrayList<String>()
    var locatinmaping = ArrayList<LocationResponseItem>()
    var genderList = ArrayList<String>()
    var bloodgroup = ArrayList<String>()

    var statemap = HashMap<String, String>()
    var locationmap = HashMap<String, String>()
    var statemapreverse = HashMap<String, String>()
    var locationmapreverse = HashMap<String, String>()
    private lateinit var bottomNavigationView: BottomNavigationView


    val Emp_Name = "employee_name"
    val Guardian_Name = "guardian_name"
    val Dob = "Dob"
    val Gender = "sp_gender"
    val PhoneNumber = "phone_number"
    val emergency_name = "emergency_contact_name"
    val Nationality = "nationality"
    val Blood_group = "blood_group"
    val Address = "address"
    val State = "state"
    val Location = "location"
    val Pincode = "pincode"
    val emergency_contactNumber = "emergency_contactNumber"
    var userid: String? = ""
    var rollid: String? = ""
    var type: Boolean? = false
    var stateid: String? = ""
    var locationid: String?=""
    var profileimage: String? = ""
    private val calendar: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information)

        addlists()
        callstatedApi()
        val sharedPreferences: SharedPreferences =getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
        type = sharedPreferences.getBoolean("edit", false)!!
        if(type == true){
            getworkerdetails()
        }
        initilize()

        edt_dob.setOnClickListener(View.OnClickListener
        {
            datePicker_diaog()
        })
        bottomnavigation()

    }


    private fun datePicker_diaog()
    {
        val newCalendar: Calendar = Calendar.getInstance()
        val mDatePicker = DatePickerDialog(this,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                edt_dob.setText(sdf.format(newDate.getTime()))
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        mDatePicker.datePicker.maxDate = System.currentTimeMillis()
        mDatePicker.show()
    }
    private fun initilize() {
        tv_next_one.setOnClickListener {
            validateall()
        }
        iv_profile_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 101)
        }
       // cleareditTexts()
        sp_state.setOnClickListener{
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stateList)
            sp_state.setAdapter(adapter)
        }

        edt_dob.setOnClickListener{
            showDatePickerDialog()
        }
        sp_location.setOnClickListener{
            getlocations()
        }

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

    private fun getlocations() {
        var stateid = statemap.get(sp_state.text.toString())
        var locationlistid = ArrayList<String>()
        for(x in locatinmaping){
            if(x.state_id == stateid){
                locationlistid.add(x.city_name)
            }
        }
        if(locationlistid.isEmpty()){
            Toast.makeText(this, "Please select state first", Toast.LENGTH_SHORT)
                .show()
        }
        val adapter2 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, locationlistid)
        sp_location.setAdapter(adapter2)
    }


    private fun validateall() {

        val edt_dob: String = edt_dob.text.toString()
        val currentDate = LocalDate.now()
        var dates = edt_dob.split("-")
        var year = if(dates.get(0).length == 4){
            dates.get(0)
        } else {
            dates.get(2)
        }
        var day = if(dates.get(2).length == 2 ){
            dates.get(2)
        }else{
            dates.get(0)
        }
        val dateOfBirth = LocalDate.of(year.toInt(), dates.get(1).toInt(), day.toInt())
        val age = Period.between(dateOfBirth, currentDate).years

        // Shared Pref saving and getting text in fields
        val edt_employee_name: String = edt_employee_name.text.toString()
        val edt_guardian_name: String = edt_guardian_name.text.toString()
        val sp_gender: String = sp_gender.text.toString()
        val edt_phone_number1: String = edt_phone_number.text.toString()
        val edt_emergency_contact_Name: String = edt_emergency_contact_Name.text.toString()
        val nationality: String = nationality.text.toString()
        val sp_blood_group: String = sp_blood_group.text.toString()
        val edt_address: String = edt_address.text.toString()
        val sp_state: String = sp_state.text.toString()
        val sp_location: String = sp_location.text.toString()
        val edt_pincode: String = edt_pincode.text.toString()
        val edt_emergency_contactNumber: String = edt_emergency_contactNumber.text.toString()


        if (!TextUtils.isEmpty(edt_employee_name)) {
                    if (!TextUtils.isEmpty(sp_gender)) {
                        if (!TextUtils.isEmpty(edt_phone_number1) && edt_phone_number1.length == 10) {
                            if (!TextUtils.isEmpty(edt_emergency_contact_Name)) {
                                if (!TextUtils.isEmpty(nationality)) {
                                    if (!TextUtils.isEmpty(edt_address)) {
                                        if (TextUtils.isEmpty(edt_pincode) || edt_pincode.length == 6) {
                                            if (!TextUtils.isEmpty(edt_dob) && age >= 18) {
                                        if (!TextUtils.isEmpty(edt_employee_name)) {
                                            if (!TextUtils.isEmpty(edt_employee_name)) {
                                                if (!TextUtils.isEmpty(edt_emergency_contactNumber)) {
                                                    val sharedPreferences: SharedPreferences =getSharedPreferences("WorkerInfoPref", Context.MODE_PRIVATE)!!
                                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                                    editor.putString(Emp_Name, edt_employee_name)
                                                    editor.putString(Guardian_Name, edt_guardian_name)
                                                    editor.putString(Dob, edt_dob)
                                                    editor.putString(Gender, sp_gender)
                                                    editor.putString(PhoneNumber, edt_phone_number1)
                                                    editor.putString(emergency_name, edt_emergency_contact_Name)
                                                    editor.putString(Nationality, nationality)
                                                    editor.putString(Blood_group, sp_blood_group)
                                                    editor.putString(Address, edt_address)
                                                    editor.putString(State, statemap.get(sp_state))
                                                    editor.putString(Location, locationmap.get(sp_location))
                                                    editor.putString(Pincode, edt_pincode)
                                                    editor.putString(emergency_contactNumber, edt_emergency_contactNumber)
                                                    editor.putString("profile",profileimage)
                                                    editor.commit()

                                                    val intent = Intent(applicationContext, WorkerInformationNext1Activity::class.java)
                                                    startActivity(intent)
                                                } else {
                                                    Toast.makeText(this, "please enter emergency contact number", Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            } else {
                                                Toast.makeText(this, "please select location", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        } else {
                                            Toast.makeText(this, "please select state", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        } else {
                                            if(!TextUtils.isEmpty(edt_dob)) {
                                                Toast.makeText(this, "please enter date of birth",
                                                    Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(this, "age is less then 18 years",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    } else {
                                        Toast.makeText(this, "please enter Valid PINCODE", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    } else {
                                        Toast.makeText(this, "please enter address", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(this, "please enter nationality", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                Toast.makeText(this, "please enter emergency contact name", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                           // edt_phone_number.error = ""
                            Toast.makeText(this, "please enter valid Phone number", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                        Toast.makeText(this, "please select gender", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
            Toast.makeText(this, "please enter employee name", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun callstatedApi() {
        val webServices = WebServices<Any>(this@WorkerInformationActivity)
        webServices.states(WebServices.ApiType.states)

        val webServices2 = WebServices<Any>(this@WorkerInformationActivity)
        webServices2.location(WebServices.ApiType.location)
    }

    private fun getworkerdetails() {
        val sharedPreferences: SharedPreferences =getSharedPreferences("updateworker", Context.MODE_PRIVATE)!!
        val test = sharedPreferences.getString("Emp_Name", "")
        edt_employee_name.setText(test)!!
        edt_guardian_name.setText(sharedPreferences.getString("guardian_name", ""))!!
        var dob = sharedPreferences.getString("Dob", "")!!
        var dates = dob?.split("-")
       if(dates?.get(0)?.length == 4){
            dob = dates.get(2)+"-"+dates.get(1)+"-"+dates.get(0)
        } else {
            if(dates?.size == 3){
                dob = dates?.get(0)+"-"+dates?.get(1)+"-"+dates?.get(2)
            }
        }
        edt_dob.setText(dob)
        sp_gender.setText( sharedPreferences.getString("Gender", ""))!!
        edt_phone_number.setText( sharedPreferences.getString("PhoneNumber", ""))!!
        edt_emergency_contactNumber.setText(sharedPreferences.getString("emergency_contactNumber", ""))!!
        sp_blood_group.setText( sharedPreferences.getString("Blood_group", ""))!!
        edt_address.setText( sharedPreferences.getString("Address", ""))!!
        stateid = sharedPreferences.getString("State", "")
        nationality.setText(sharedPreferences.getString("Nationality",""))
        locationid = sharedPreferences.getString("Location", "")
        edt_pincode.setText(sharedPreferences.getString("pincode", ""))!!
        edt_emergency_contact_Name.setText(sharedPreferences.getString("emergency_contact_name",""))
        profileimage = sharedPreferences.getString("profile","")
        profileimage = sharedPreferences.getString("profile","")
        if(profileimage != "") {
            val url = "https://gp.proteam.co.in/"+profileimage
            Picasso.get().load(url).into(iv_profile_image)
        }
    }
    private fun addlists() {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageUri: Uri = uri
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                iv_profile_image.setImageBitmap(bitmap)
                profileimage = convertImageUriToBase64(contentResolver, imageUri)
            }
        }
    }

    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                      val intentScan = Intent(this@WorkerInformationActivity, MainActivity::class.java)
                       startActivity(intentScan)
                    true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@WorkerInformationActivity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@WorkerInformationActivity, ApprovalsActivity::class.java)
                        val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("nav", true)
                        editor.commit()
                        startActivity(intentapprovals)
                    }
                    // Handle the Profile action
                    true
                }
                R.id.it_worker -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@WorkerInformationActivity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }

    fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        edt_dob.setText(formattedDate)
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
                            statemap.put(x.state_name,x.id)
                            statemapreverse.put(x.id,x.state_name)
                        }
                        sp_state.setText(statemapreverse.get(stateid))!!
                    } else {
                        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
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
                            locatinmaping = response
                            locationList.add(x.city_name)
                            locationmap.put(x.city_name,x.city_id)
                            locationmapreverse.put(x.city_id,x.city_name)
                        }
                        sp_location.setText(locationmapreverse.get(locationid))!!
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