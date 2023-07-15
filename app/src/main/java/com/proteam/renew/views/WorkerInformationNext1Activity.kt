package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.proteam.renew.R
import com.proteam.renew.responseModel.ContractorListResponsce
import com.proteam.renew.responseModel.SupervisorListResponsce
import com.proteam.renew.responseModel.ViewProjectMaster
import com.proteam.renew.responseModel.ViewSkillsetMaster
import com.proteam.renew.responseModel.ViewTrainingMaster
import com.proteam.renew.responseModel.statesResponse
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices

class WorkerInformationNext1Activity : AppCompatActivity(),OnResponseListener<Any> {

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
    val tv_previous: TextView by lazy { findViewById<TextView>(R.id.tv_previous) }
    val tv_next_two: TextView by lazy { findViewById<TextView>(R.id.tv_next_two) }
    val dri_const: ConstraintLayout by lazy { findViewById(R.id.dri_const) }
    val con_induction_date: ConstraintLayout by lazy { findViewById(R.id.con_induction_date) }
    val con_remarks: ConstraintLayout by lazy { findViewById(R.id.con_remarks) }
    val ti_report_ok: TextInputLayout by lazy { findViewById(R.id.ti_report_ok) }
    val edt_worker_id: EditText by lazy { findViewById(R.id.edt_worker_id) }
    val ti_state: TextInputLayout by lazy { findViewById(R.id.ti_state) }
    val ti_performance_feedback: TextInputLayout by lazy { findViewById(R.id.ti_performance_feedback) }
    val edt_remarks: EditText by lazy { findViewById<EditText>(R.id.edt_remarks) }
    val ti_induction_state: TextInputLayout by lazy { findViewById(R.id.ti_induction_state) }
    val induction_date: EditText by lazy { findViewById<EditText>(R.id.edt_induction_date) }
    val con_worker_id: EditText by lazy { findViewById(R.id.con_worker_id) }
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


    val projectMap = HashMap<String, String>()
    val skillsetMap = HashMap<String, String>()
    val trainingMap = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information_next1)

        callArray()
        val sharedPreferences: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences.getString("rollid", "")!!
        userid = sharedPreferences.getString("userid", "")!!

        if(rollid == "1"){
            con_induction_date.visibility = View.GONE
            ti_report_ok.visibility = View.GONE
            ti_induction_state.visibility = View.GONE
            con_remarks.visibility = View.GONE
            ti_state.visibility = View.GONE
            ti_performance_feedback.visibility = View.GONE
        }
        tv_next_two.setOnClickListener {
            validateform()
            val intent = Intent(applicationContext, WorkerInformationNext2Activity::class.java)
            startActivity(intent)
        }

        tv_previous.setOnClickListener {
            val intent = Intent(applicationContext, WorkerInformationActivity::class.java)
            startActivity(intent)
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
        val sharedPreferences1: SharedPreferences =getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
        type = sharedPreferences1.getBoolean("edit", false)!!

        if(type == true) {
            val sharedPreferences2: SharedPreferences =getSharedPreferences("updateworker", Context.MODE_PRIVATE)!!
            prijectid = sharedPreferences2.getString("Project", "")!!
            sp_skill_type.setText(sharedPreferences2.getString("skill_type", ""))!!
            skillid = sharedPreferences2.getString("skill_set", "")!!
            edt_worker_designation.setText( sharedPreferences2.getString("worker_designation", ""))!!
            edt_doj.setText( sharedPreferences2.getString("doj", ""))!!
            sp_Supervisor_name.setText( sharedPreferences2.getString("Supervisor_name", ""))!!
            sp_sub_contractor.setText( sharedPreferences2.getString("sub_contractor", ""))!!
            edt_contractor_contact_number.setText( sharedPreferences2.getString("contractor_contact_number", ""))!!
            induction_date.setText(sharedPreferences2.getString("inductiondate", ""))
            edt_worker_id.setText(sharedPreferences2.getString("workerid", ""))

            if( sharedPreferences2.getString("induction_status", "") == "0"){
                sp_induction_status.setText("Yes")
            }else{
                sp_induction_status.setText("no")
            }
            edt_aadhaar_card.setText( sharedPreferences2.getString("aadhaar_card", ""))!!
            if( sharedPreferences2.getString("medical_test_status", "") == "0"){
                sp_medical_test_status.setText("Yes")
            }else{
                sp_medical_test_status.setText("no")
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
            }else{
                dri_const.visibility = View.VISIBLE
            }
        }

        sp_induction_status.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if(selectedItem.lowercase() == "yes"){
                ti_report_ok.visibility = View.VISIBLE
            }else{
                ti_report_ok.visibility = View.GONE
            }
        }
        callmasterAps()
        setspinneradaptors()
    }

    private fun validateform() {
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
            editor.putString("induction_date",induction_date.text.toString())
            editor.putString("workerid",edt_worker_id.text.toString())
            editor.commit()
            val intent = Intent(applicationContext, WorkerInformationNext2Activity::class.java)
            startActivity(intent)

    }

    private fun callmasterAps() {
        val webServices = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices.projects(WebServices.ApiType.projects)

        val webServices2 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices2.skills(WebServices.ApiType.skills)

        val webServices3 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices3.training(WebServices.ApiType.training)

        val webServices4 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices4.Conctractors(WebServices.ApiType.contractors, userid)

        val webServices5 = WebServices<Any>(this@WorkerInformationNext1Activity)
        webServices5.Supervisor(WebServices.ApiType.supervisors, userid)
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
                        for (x in contractorListResponsce) {
                            contractorList.add(x.username)
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