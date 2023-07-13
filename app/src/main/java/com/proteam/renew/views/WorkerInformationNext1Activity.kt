package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    val edt_doj: EditText by lazy { findViewById<EditText>(R.id.edt_doj) }
    val sp_Supervisor_name: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_Supervisor_name) }
    val sp_sub_contractor: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_sub_contractor) }
    val edt_contractor_contact_number: EditText by lazy { findViewById<EditText>(R.id.edt_contractor_contact_number) }
    val sp_induction_status: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_induction_status) }
    val edt_aadhaar_card: EditText by lazy { findViewById<EditText>(R.id.edt_aadhaar_card) }
    val sp_medical_test_status: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_medical_test_status) }
    val sp_report_is_ok: AutoCompleteTextView by lazy { findViewById<AutoCompleteTextView>(R.id.sp_report_is_ok) }

    val tv_previous: TextView by lazy { findViewById<TextView>(R.id.tv_previous) }
    val tv_next_two: TextView by lazy { findViewById<TextView>(R.id.tv_next_two) }
    var progressDialog: ProgressDialog? = null
    var skillsList = ArrayList<String>()
    var projectslist = ArrayList<String>()
    var traininglist = ArrayList<String>()
    var skillType = ArrayList<String>()
    var generaloption = ArrayList<String>()
    var contractorList = ArrayList<String>()
    var supervisorList = ArrayList<String>()



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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_information_next1)

        generaloption.add("Yes")
        generaloption.add("No")

        skillType.add("Skilled")
        skillType.add("UnSkilled")

        val sharedPreferences: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences.getString("rollid", "")!!
        userid = sharedPreferences.getString("userid", "")!!

        tv_next_two.setOnClickListener {

            validateform()
            val intent = Intent(applicationContext, WorkerInformationNext2Activity::class.java)
            startActivity(intent)

        }

        tv_previous.setOnClickListener {

            val intent = Intent(applicationContext, WorkerInformationActivity::class.java)
            startActivity(intent)

        }
        callmasterAps()

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

    }

    private fun validateform() {
        val sharedPreferences: SharedPreferences =getSharedPreferences("WorkerInfoOnePref2", Context.MODE_PRIVATE)!!
            // Shared Pref saving and getting text in fields
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
            editor.putString(Project, sp_project)
            editor.putString(skill_type, sp_skill_type)
            editor.putString(skill_set, sp_skill_set)
            editor.putString(worker_designation, edt_worker_designation)
            editor.putString(doj, edt_doj)
            editor.putString(Supervisor_name, sp_Supervisor_name)
            editor.putString(sub_contractor, sp_sub_contractor)
            editor.putString(contractor_contact_number, edt_contractor_contact_number)
            editor.putString(induction_status, sp_induction_status)
            editor.putString(aadhaar_card, edt_aadhaar_card)
            editor.putString(medical_test_status, sp_medical_test_status)
            editor.putString(report_is_ok, sp_report_is_ok)

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
}