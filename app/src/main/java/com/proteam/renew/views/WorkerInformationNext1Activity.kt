package com.proteam.renew.views

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.proteam.renew.R
import com.proteam.renew.responseModel.ContractorListResponsce
import com.proteam.renew.responseModel.ContractorListResponsceItem
import com.proteam.renew.responseModel.SupervisorListResponsce
import com.proteam.renew.responseModel.ViewProjectMaster
import com.proteam.renew.responseModel.ViewSkillsetMaster
import com.proteam.renew.responseModel.ViewTrainingMaster
import com.proteam.renew.responseModel.statesResponse
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class WorkerInformationNext1Activity : AppCompatActivity(),OnResponseListener<Any>, DatePickerDialog.OnDateSetListener {

    val sp_project: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_project) }
    val sp_skill_type: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_skill_type) }
    val sp_skill_set: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_skill_set) }
    val edt_worker_designation: EditText by lazy { findViewById<EditText>(R.id.edt_worker_designation) }
    val sp_status: AutoCompleteTextView by lazy { findViewById(R.id.sp_status) }
    val sp_feed_back: AutoCompleteTextView by lazy { findViewById(R.id.sp_feed_back) }
    val edt_doj: EditText by lazy { findViewById<EditText>(R.id.edt_doj) }
    val sp_Supervisor_name: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_Supervisor_name) }
    val sp_sub_contractor: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_sub_contractor) }
    val edt_contractor_contact_number: EditText by lazy { findViewById<EditText>(R.id.edt_contractor_contact_number) }
    val sp_induction_status: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_induction_status) }
    val edt_aadhaar_card: EditText by lazy { findViewById<EditText>(R.id.edt_aadhaar_card) }
    val sp_medical_test_status: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_medical_test_status) }
    val sp_report_is_ok: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_report_is_ok) }
    val sp_year_exp: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_year_exp) }
    val sp_months_exp: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_months_exp) }
    val ll_exp: LinearLayout by lazy { findViewById(R.id.ll_exp) }
    val edt_driving_licence : EditText by lazy { findViewById<EditText>(R.id.edt_driving_licence) }
    val edt_medical_date : EditText by lazy { findViewById<EditText>(R.id.edt_medical_date) }
    val con_licence_date: ConstraintLayout by lazy { findViewById(R.id.con_licence_date) }
    val edt_licence_expire : EditText by lazy { findViewById<EditText>(R.id.edt_licence_expire) }
    val tv_previous: TextView by lazy { findViewById<TextView>(R.id.tv_previous) }
    val tv_next_two: TextView by lazy { findViewById<TextView>(R.id.tv_next_two) }
    val dri_const: ConstraintLayout by lazy { findViewById(R.id.dri_const) }
    val con_medical_date: ConstraintLayout by lazy { findViewById(R.id.con_medical_date) }

    val con_induction_date: ConstraintLayout by lazy { findViewById(R.id.con_induction_date) }
    val con_remarks: ConstraintLayout by lazy { findViewById(R.id.con_remarks) }
    val ti_report_ok: TextInputLayout by lazy { findViewById(R.id.ti_report_ok) }
    val edt_worker_id: EditText by lazy { findViewById(R.id.edt_worker_id) }
    val ti_state: TextInputLayout by lazy { findViewById(R.id.ti_state) }
    val ti_performance_feedback: TextInputLayout by lazy { findViewById(R.id.ti_performance_feedback) }
    val edt_remarks: EditText by lazy { findViewById<EditText>(R.id.edt_remarks) }
    val ti_induction_state: TextInputLayout by lazy { findViewById(R.id.ti_induction_state) }
    val induction_date: EditText by lazy { findViewById<EditText>(R.id.edt_induction_date) }
    val con_worker_id: ConstraintLayout by lazy { findViewById(R.id.con_worker_id) }
    var progressDialog: ProgressDialog? = null
    var skillsList = ArrayList<String>()
    var projectslist = ArrayList<String>()
    var projectsreverselist = HashMap<String, String>()
    var skillsetreverse = HashMap<String, String>()
    var traininglist = ArrayList<String>()
    var skillType = ArrayList<String>()
    var generaloption = ArrayList<String>()
    var contractorList = ArrayList<String>()
    var supervisorList = ArrayList<String>()
    var experienceyearsList = ArrayList<String>()
    var experiencemonthList = ArrayList<String>()
    var status = ArrayList<String>()
    var feedback = ArrayList<String>()

    //Shared Pref  string
    val Project = "project"
    val skill_type = "skill_type"
    val skill_set = "sp_skill_set"
    val worker_designation = "worker_designation"
    val doj = "doj"
    val Supervisor_name = "Supervisor_name"
    val sub_contractor = "sub_contractor"
    val contractor_contact_number = "contractor_contact_number"
    val induction_status = "induction_status"
    val aadhaar_card = "aadhaar_card"
    val medical_test_status = "medical_test_status"
    val report_is_ok = "report_is_ok"

    var userid: String? = ""
    var rollid: String? = ""
    var type: Boolean? = false
    var prijectid: String = ""
    var skillid: String = ""
    var conId: String = ""
    var supId = ""
    var dateview : Int = 0


    val projectMap = HashMap<String, String>()
    val skillsetMap = HashMap<String, String>()
    val trainingMap = HashMap<String, String>()
    var supervisorreverse = HashMap<String, String>()
    var contractorreverse = HashMap<String, String>()
    var contractormap = HashMap<String, String>()
    var supervisormap = HashMap<String, String>()
    var contractormapinglist = ArrayList<ContractorListResponsceItem>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information_next1)

        callArray()
        bottomnavigation()
        val sharedPreferences: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences.getString("rollid", "")!!
        userid = sharedPreferences.getString("userid", "")!!

       /* val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        val rollid1 = sharedPreferences2.getString("rollid", "")!!
        val userid2 = sharedPreferences2.getString("userid", "")!!*/

        val sharedPreferences1: SharedPreferences =getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
        type = sharedPreferences1.getBoolean("edit", false)!!

        val sharedPreferences4: SharedPreferences =getSharedPreferences("onboard", Context.MODE_PRIVATE)!!
        var nav = sharedPreferences4.getBoolean("nav", false)!!

        dri_const.visibility = View.GONE
        con_licence_date.visibility = View.GONE
        if(rollid == "1" || nav == false){
            con_worker_id.visibility = View.GONE
            con_induction_date.visibility = View.GONE
            ti_report_ok.visibility = View.GONE
            ti_induction_state.visibility = View.GONE
            con_remarks.visibility = View.GONE
            ti_state.visibility = View.GONE
            ti_performance_feedback.visibility = View.GONE
            con_induction_date.visibility = View.GONE
        }
        tv_next_two.setOnClickListener {
            validateform()
        }
        edt_licence_expire.setOnClickListener{
            dateview = 3
            showDatePickerDialog()
        }

        tv_previous.setOnClickListener {
            val intent = Intent(applicationContext, WorkerInformationActivity::class.java)
            startActivity(intent)
        }
        edt_doj.setOnClickListener {
            showDatePickerDialog()
        }
        sp_project.setOnClickListener {
            val adapter2 = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, projectslist)
            sp_project.setAdapter(adapter2)
        }

        sp_skill_set.setOnClickListener {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, skillsList)
            sp_skill_set.setAdapter(adapter)
        }
        sp_medical_test_status.setText("Yes")
        edt_medical_date.setOnClickListener {
            dateview = 1
            showDatePickerDialog()
        }

        induction_date.setOnClickListener {
            dateview = 2
            showDatePickerDialog()
        }
        if(type == true) {
            val sharedPreferences2: SharedPreferences =getSharedPreferences("updateworker", Context.MODE_PRIVATE)!!
            prijectid = sharedPreferences2.getString("Project", "")!!
            sp_skill_type.setText(sharedPreferences2.getString("skill_type", ""))!!
            skillid = sharedPreferences2.getString("skill_set", "")!!
            edt_worker_designation.setText( sharedPreferences2.getString("worker_designation", ""))!!
            edt_doj.setText( sharedPreferences2.getString("doj", ""))!!
            var doj = sharedPreferences2.getString("doj", "")
            var dates = doj?.split("-")
            if(dates?.get(0)?.length == 4){
                doj = dates?.get(2)+"-"+dates.get(1)+"-"+dates.get(0)
            } else {
                doj = dates?.get(0)+"-"+dates?.get(1)+"-"+dates?.get(2)
            }
            edt_doj.setText(doj)
             supId = sharedPreferences2.getString("Supervisor_name", "")!!
            conId = sharedPreferences2.getString("sub_contractor", "")!!
            edt_contractor_contact_number.setText( sharedPreferences2.getString("contractor_contact_number", ""))!!
            induction_date.setText(sharedPreferences2.getString("inductiondate", ""))
            edt_worker_id.setText(sharedPreferences2.getString("workerid", ""))
            sp_year_exp.setText(sharedPreferences2.getString("exp_years", ""))
            sp_months_exp.setText(sharedPreferences2.getString("exp_months", ""))
            edt_remarks.setText(sharedPreferences2.getString("edt_remarks",""))
            edt_driving_licence.setText(sharedPreferences2.getString("drivinglic",""))
            edt_licence_expire.setText(sharedPreferences2.getString("licence_exp",""))
            edt_medical_date.setText(sharedPreferences2.getString("medical_test_date",""))

            if( sharedPreferences2.getString("induction_status", "") == "0"){
                sp_induction_status.setText("Yes")
                con_induction_date.visibility = View.VISIBLE
            }else{
                sp_induction_status.setText("no")
                con_induction_date.visibility = View.GONE
            }
            edt_aadhaar_card.setText( sharedPreferences2.getString("aadhaar_card", ""))!!
            if( sharedPreferences2.getString("medical_test_status", "") == "0"){
                sp_medical_test_status.setText("Yes")
                con_medical_date.visibility = View.VISIBLE
            }else{
                sp_medical_test_status.setText("no")
                con_medical_date.visibility = View.GONE
            }

            if( sharedPreferences2.getString("report_is_ok", "") == "0"){
                sp_report_is_ok.setText("Yes")
            }else{
                sp_report_is_ok.setText("no")
            }

            if(sp_skill_type.text.toString().lowercase() == "skilled"){
                ll_exp.visibility = View.VISIBLE
            }
            if(sp_skill_set.text.toString().lowercase() != "driver"){
                dri_const.visibility = View.GONE
                con_licence_date.visibility = View.GONE
            }
            if(sharedPreferences2.getString("approvalstatus", "") == "0" && rollid != "1") {
                con_remarks.visibility = View.GONE
                ti_performance_feedback.visibility = View.GONE
            }
        }



        sp_skill_type.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if(selectedItem.lowercase() == "skilled"){
                ll_exp.visibility = View.VISIBLE
            }else{
                ll_exp.visibility = View.GONE
            }
        }

        sp_skill_set.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if(selectedItem.lowercase() != "driver"){
                dri_const.visibility = View.GONE
                con_licence_date.visibility = View.GONE
            } else {
                dri_const.visibility = View.VISIBLE
                con_licence_date.visibility = View.VISIBLE
            }
        }

        sp_induction_status.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if(selectedItem.lowercase() == "yes"){
                con_induction_date.visibility = View.VISIBLE
            } else {
                con_induction_date.visibility = View.GONE
            }
        }

        sp_sub_contractor.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            for(x in contractormapinglist){
                if(selectedItem == x.username){
                    edt_contractor_contact_number.setText(x.phone)
                }
            }
        }
        sp_medical_test_status.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if(selectedItem.lowercase() == "yes"){
                con_medical_date.visibility = View.VISIBLE
            } else {
                con_medical_date.visibility = View.GONE
            }
        }
        callmasterAps()
        setspinneradaptors()
    }

    private fun validateform() {
        val edt_doj1: String = edt_doj.text.toString()
        val currentDate = LocalDate.now()
        var differenceInMonths: Long = 0
        if (!TextUtils.isEmpty(edt_doj.text)) {
        var dates = edt_doj1.split("-")
        var year = if (dates.get(2).length == 4 && !TextUtils.isEmpty(edt_doj.text)) {
            dates.get(2)
        } else {
            dates.get(0)
        }
        var day = if (dates.get(0).length == 2) {
            dates.get(0)
        } else {
            dates.get(2)
        }

        if (dates.size == 3 && year != "0000" && day != "00") {
            val receivedDate = LocalDate.of(
                year.toInt(),
                dates.get(1).toInt(),
                day.toInt()
            ) // Replace with the actual received date
            differenceInMonths = ChronoUnit.MONTHS.between(receivedDate, currentDate)
        } else {
            Toast.makeText(this, "enter valid DOJ", Toast.LENGTH_SHORT)
                .show()
        }
    }
        val sharedPreferences: SharedPreferences =getSharedPreferences("WorkerInfoOnePref2", Context.MODE_PRIVATE)!!
        val sp_project: String = sp_project.text.toString()
        val sp_skill_type: String = sp_skill_type.text.toString()
        val sp_skill_set: String = sp_skill_set.text.toString()
        val edt_worker_designation: String = edt_worker_designation.text.toString()
        val edt_doj: String = edt_doj.text.toString()
        val sp_Supervisor_name: String = sp_Supervisor_name.text.toString()
        val sp_sub_contractor: String = sp_sub_contractor.text.toString()
        val edt_contractor_contact_number: String = edt_contractor_contact_number.text.toString()
        val sp_induction_status: String = sp_induction_status.text.toString()
        val edt_aadhaar_card: String = edt_aadhaar_card.text.toString()
        val sp_medical_test_status: String = sp_medical_test_status.text.toString()
        val sp_report_is_ok: String = sp_report_is_ok.text.toString()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        if (!TextUtils.isEmpty(sp_project)) {
            if (!TextUtils.isEmpty(edt_doj)) {
                if (!TextUtils.isEmpty(sp_skill_type) && sp_skill_type != "Skilled" ) {
                    if (!TextUtils.isEmpty(sp_skill_set)) {
                        if (!TextUtils.isEmpty(edt_doj) && differenceInMonths < 1) {
                            if (!TextUtils.isEmpty(sp_Supervisor_name)) {
                                if (!TextUtils.isEmpty(sp_sub_contractor)) {
                                    if (!TextUtils.isEmpty(edt_contractor_contact_number)) {
                                        if (!TextUtils.isEmpty(edt_aadhaar_card) && edt_aadhaar_card.length == 12) {
                                            if (!TextUtils.isEmpty(sp_medical_test_status)) {
                                                if (con_medical_date.isVisible && !TextUtils.isEmpty(edt_medical_date.text) || rollid == "1") {
                                                    if (!TextUtils.isEmpty(edt_contractor_contact_number) && edt_contractor_contact_number.length == 10) {
                                                    editor.putString(Project, projectMap.get(sp_project))
                                                    editor.putString(skill_type, sp_skill_type)
                                                    editor.putString(skill_set, skillsetMap.get(sp_skill_set))
                                                    editor.putString(worker_designation, edt_worker_designation)
                                                    editor.putString(doj, edt_doj)
                                                    editor.putString(Supervisor_name, sp_Supervisor_name)
                                                    editor.putString(sub_contractor, sp_sub_contractor)
                                                    editor.putString(contractor_contact_number, edt_contractor_contact_number)
                                                    editor.putString(induction_status, sp_induction_status)
                                                    editor.putString(aadhaar_card, edt_aadhaar_card)
                                                    editor.putString(medical_test_status, sp_medical_test_status)
                                                    editor.putString(report_is_ok, sp_report_is_ok)
                                                    editor.putString("exp_years",sp_year_exp.text.toString())
                                                    editor.putString("exp_months",sp_months_exp.text.toString())
                                                    editor.putString("driving_licence", edt_driving_licence.text.toString())
                                                    editor.putString("licence_exp",edt_licence_expire.text.toString())
                                                    editor.putString("induction_date",induction_date.text.toString())
                                                    editor.putString("workerid",edt_worker_id.text.toString())
                                                    editor.putString("edt_remarks",edt_remarks.text.toString())
                                                    editor.putString("medical_test_date",edt_medical_date.text.toString())
                                                    editor.commit()
                                                    val intent = Intent(applicationContext, WorkerInformationNext2Activity::class.java)
                                                    startActivity(intent)

                                                } else {
                                                    Toast.makeText(this, "please enter valid contractor contact number", Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            } else {
                                                Toast.makeText(this, "please enter medical text date", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            } else {
                                                Toast.makeText(this, "please select medical", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        } else {
                                            Toast.makeText(this, "please enter valid aadhaar number", Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    } else {
                                        Toast.makeText(this, "please contractor contact number", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(this, "please select subcontractor name", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                Toast.makeText(this, "please select supervisor name", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            if(differenceInMonths > 1){
                                Toast.makeText(this, "DOJ is more then a month. ", Toast.LENGTH_SHORT)
                                    .show()
                            }else{
                                Toast.makeText(this, "please enter valid DOJ", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "please select skill set", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "please select skill type", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(this, "please select date of joining", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "please select project", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun callmasterAps() {
        val webServices = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices.projects(WebServices.ApiType.projects)

        val webServices2 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices2.skills(WebServices.ApiType.skills)

        val webServices3 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices3.training(WebServices.ApiType.training)

        if(rollid == "1"){
            val webServices4 = WebServices<Any>(this@WorkerInformationNext1Activity)
            webServices4.Conctractors(WebServices.ApiType.contractors, userid)

            val webServices5 = WebServices<Any>(this@WorkerInformationNext1Activity)
            webServices5.Supervisor(WebServices.ApiType.supervisors, "0")

        }else {
            val webServices4 = WebServices<Any>(this@WorkerInformationNext1Activity)
            webServices4.Conctractors(WebServices.ApiType.contractors, "0")

            val webServices5 = WebServices<Any>(this@WorkerInformationNext1Activity)
            webServices5.Supervisor(WebServices.ApiType.supervisors, userid)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResponse(
        response: Any?,
        URL: WebServices.ApiType?,
        isSucces: Boolean,
        code: Int
    ) {
        when (URL) {
            WebServices.ApiType.training -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val viewTrainingMaster = response as ViewTrainingMaster
                    if (viewTrainingMaster?.isEmpty() == false) {
                        for (x in viewTrainingMaster) {
                            traininglist.add(x.training_name)
                            trainingMap.put(x.training_name,x.training_master_id)
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
            WebServices.ApiType.skills -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val viewSkillsetMaster = response as ViewSkillsetMaster
                    if (viewSkillsetMaster?.isEmpty() == false) {
                        for (x in viewSkillsetMaster) {
                            skillsList.add(x.skill_set_name)
                            skillsetMap.put(x.skill_set_name,x.skill_set_id)
                            skillsetreverse.put(x.skill_set_id,x.skill_set_name)
                        }
                        sp_skill_set.setText(skillsetreverse.get(skillid))
                        if(skillsetreverse.get(skillid)?.lowercase() != "driver"){
                            dri_const.visibility = View.GONE
                            con_licence_date.visibility = View.GONE
                        } else {
                            dri_const.visibility = View.VISIBLE
                            con_licence_date.visibility = View.VISIBLE
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
                            projectMap.put(x.project_name,x.id)
                            projectsreverselist.put(x.id,x.project_name)
                        }
                        sp_project.setText(projectsreverselist.get(prijectid))
                    } else {
                        Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            WebServices.ApiType.contractors -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val contractorListResponsce = response as ContractorListResponsce
                    if (contractorListResponsce?.isEmpty() == false) {
                        contractormapinglist = response
                        for (x in contractorListResponsce) {
                            contractorList.add(x.username)
                            contractorreverse.put(x.id,x.username)
                            contractormap.put(x.username,x.id)
                        }
                        sp_sub_contractor.setText(contractorreverse.get(conId))
                        if(rollid == "1"){
                            contractorList.clear()
                            contractorList.add(contractorreverse.get(userid).toString())
                            sp_sub_contractor.setText(contractorreverse.get(userid))
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
            WebServices.ApiType.supervisors -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val supervisorsresponsce = response as SupervisorListResponsce
                    if (supervisorsresponsce?.isEmpty() == false) {
                        for (x in supervisorsresponsce) {
                            supervisorList.add(x.username)
                            supervisorreverse.put(x.id, x.username)
                            supervisormap.put(x.username,x.id)
                        }
                        sp_Supervisor_name.setText(supervisorreverse.get(supId))
                        if(rollid == "2"){
                            supervisorList.clear()
                            supervisorList.add(supervisorreverse.get(userid).toString())
                            sp_Supervisor_name.setText(supervisorreverse.get(userid))
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

    private fun setspinneradaptors() {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, skillsList)
        sp_skill_set.setAdapter(adapter)

        val adapter2 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, projectslist)
        sp_project.setAdapter(adapter2)

        val adapter3 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, skillType)
        sp_skill_type.setAdapter(adapter3)

        val adapter4 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, generaloption)
        sp_report_is_ok.setAdapter(adapter4)
        sp_induction_status.setAdapter(adapter4)
        sp_medical_test_status.setAdapter(adapter4)

        val adapter5 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, contractorList)
        sp_sub_contractor.setAdapter(adapter5)

        val adapter6 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, supervisorList)
        sp_Supervisor_name.setAdapter(adapter6)

        val adapter7 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, status)
        sp_status.setAdapter(adapter7)

        val adapter8 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, experienceyearsList)
        sp_year_exp.setAdapter(adapter8)

        val adapter9 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, experiencemonthList)
        sp_months_exp.setAdapter(adapter9)

        val adapter10 = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, feedback)
        sp_feed_back.setAdapter(adapter10)
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
        if(dateview==0){
            edt_doj.setText(formattedDate)
        }else if(dateview == 2){
            induction_date.setText(formattedDate)
        }else if(dateview == 3){
            edt_licence_expire.setText(formattedDate)
        }
        else{
            edt_medical_date.setText(formattedDate)
        }
    }
    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                  val intentScan = Intent(this@WorkerInformationNext1Activity, MainActivity::class.java)
                  startActivity(intentScan)
                   true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@WorkerInformationNext1Activity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@WorkerInformationNext1Activity, ApprovalsActivity::class.java)
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
                        val intentScan = Intent(this@WorkerInformationNext1Activity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }


    private fun callArray() {
        status.add("Active")
        status.add("InActive")

        feedback.add("Good")
        feedback.add("Average")
        feedback.add("Below Average")

        generaloption.add("Yes")
        generaloption.add("No")
        skillType.add("Skilled")
        skillType.add("UnSkilled")
        experiencemonthList.add("1")
        experiencemonthList.add("2")
        experiencemonthList.add("3")
        experiencemonthList.add("4")
        experiencemonthList.add("5")
        experiencemonthList.add("6")
        experiencemonthList.add("7")
        experiencemonthList.add("8")
        experiencemonthList.add("9")
        experiencemonthList.add("10")
        experiencemonthList.add("11")
        experiencemonthList.add("12")
        experienceyearsList.add("1")
        experienceyearsList.add("2")
        experienceyearsList.add("3")
        experienceyearsList.add("4")
        experienceyearsList.add("5")
        experienceyearsList.add("6")
        experienceyearsList.add("7")
        experienceyearsList.add("8")
        experienceyearsList.add("9")
        experienceyearsList.add("10")
        experienceyearsList.add("11")
        experienceyearsList.add("12")
        experienceyearsList.add("13")
        experienceyearsList.add("14")
        experienceyearsList.add("15")
        experienceyearsList.add("16")
        experienceyearsList.add("17")
        experienceyearsList.add("18")
        experienceyearsList.add("19")
    }

}