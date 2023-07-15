package com.proteam.renew.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.R
import com.proteam.renew.responseModel.workersListResponsceItem
import com.proteam.renew.views.WorkerDetailsActivity
import com.proteam.renew.views.WorkerInformationActivity
import com.proteam.renew.views.WorkerInformationNext1Activity

class WorkerListAdapter(
    private val mList: List<workersListResponsceItem>,
    var applicationContext: Context,
    var value : Boolean
): RecyclerView.Adapter<WorkerListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.layout_worker_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.tv_name.text = ItemsViewModel.full_name
        holder.tv_id.text = ItemsViewModel.id
        holder.tv_doj.text = ItemsViewModel.doj
        if((position % 2) == 0){
            holder.layout.setBackgroundColor(Color.parseColor("#BFF1D1"))
        }

        if(ItemsViewModel.state == "1"){
            holder.active.text = "Active"
        }else if(ItemsViewModel.state == "2"){
            holder.active.text = "Inactive"
        }

        if(ItemsViewModel.approval_status == "1"){
            holder.iv_status.text = "Approved"
        } else if(ItemsViewModel.approval_status == "0"){
            holder.iv_status.text = "Pending"
            holder.iv_status.setBackgroundResource(R.drawable.btn_bg_yellow)
            holder.active.setVisibility(View.INVISIBLE)
        }else{
            holder.iv_status.text = "Rejected"
            holder.iv_status.setBackgroundResource(R.drawable.btn_bg_orange)
        }
        if(value){
            holder.iv_action.setImageResource(R.drawable.ic_view_icon)
        }

        holder.iv_action.setOnClickListener {
            val sharedPreferences1: SharedPreferences =applicationContext.getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
            val editor1: SharedPreferences.Editor = sharedPreferences1.edit()
            editor1.putBoolean("edit",true)
            editor1.putString("workerid",ItemsViewModel.id)
            editor1.putBoolean("approval",value)
            editor1.commit()

            val sharedPreferences: SharedPreferences =applicationContext.getSharedPreferences("updateworker", Context.MODE_PRIVATE)!!
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("edit",true)
            editor.putString("Emp_Name", ItemsViewModel.full_name)
            editor.putString("Guardian_Name", ItemsViewModel.father_name)
            editor.putString("Dob", ItemsViewModel.dob)
            editor.putString("Gender", ItemsViewModel.gender)
            editor.putString("PhoneNumber", ItemsViewModel.mobile_no)
            editor.putString("emergency_contact_name", ItemsViewModel.emergency_contact_name)
            editor.putString("Nationality", ItemsViewModel.nationality)
            editor.putString("Blood_group", ItemsViewModel.blood_group)
            editor.putString("Address", ItemsViewModel.address_line_1)
            editor.putString("State", ItemsViewModel.state)
            editor.putString("Location", ItemsViewModel.city)
            editor.putString("pincode", ItemsViewModel.pin_code)
            editor.putString("emergency_contactNumber", ItemsViewModel.emergency_contact_no)
            editor.putString("Project", ItemsViewModel.project)
            editor.putString("skill_type", ItemsViewModel.skill_type)
            editor.putString("skill_set", ItemsViewModel.skill_set)
            editor.putString("worker_designation", ItemsViewModel.work_employee_designation)
            editor.putString("doj", ItemsViewModel.doj)
            editor.putString("Supervisor_name", "")
            editor.putString("sub_contractor", ItemsViewModel.subcontractor_name)
            editor.putString("contractor_contact_number", ItemsViewModel.subcontract_contract_no)
            editor.putString("induction_status", ItemsViewModel.induction_status)
            editor.putString("aadhaar_card", ItemsViewModel.aadhaar_card)
            editor.putString("medical_test_status", ItemsViewModel.medical_test_status)
            editor.putString("report_is_ok", ItemsViewModel.report_is_ok)
            editor.putString("profile",ItemsViewModel.profilepic)
            editor.putString("workerid",ItemsViewModel.worker_id)
            editor.commit()

            if(value){
                val intent = Intent(applicationContext, WorkerInformationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)
            }else {
                val intent = Intent(applicationContext, WorkerInformationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)
            }
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){

         var tv_doj : TextView
         var tv_name : TextView
         var tv_id : TextView
         var iv_action : ImageView
         var iv_status : TextView
         var layout : LinearLayout
         var active : TextView
        init {
            tv_doj = view.findViewById(R.id.tv_doj)
            tv_name = view.findViewById(R.id.tv_name)
            tv_id = view.findViewById(R.id.tv_id)
            iv_action = view.findViewById(R.id.iv_action)
            iv_status = view.findViewById(R.id.tv_approval)
            layout = view.findViewById(R.id.ll_layout)
            active = view.findViewById(R.id.tv_active)
        }


    }
}