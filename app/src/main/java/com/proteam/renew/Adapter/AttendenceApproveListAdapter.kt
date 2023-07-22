package com.proteam.renew.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.R
import com.proteam.renew.responseModel.AttendenceListResponsce
import com.proteam.renew.utilitys.CheckboxListner

class AttendenceApproveListAdapter(private val mList: AttendenceListResponsce, private val itemClickListener: CheckboxListner, var applicationContext: Context): RecyclerView.Adapter<AttendenceApproveListAdapter.MyViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.layout_attendence_approval,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val attendanceItem = mList[position]
        holder.tv_worker.text = attendanceItem.father_name
        holder.tv_contractor.text = attendanceItem.employer_contractor_name
        holder.tv_attendance.text = attendanceItem.attendance

        if((position % 2) == 0){
            holder.ll_layout.setBackgroundColor(Color.parseColor("#BFF1D1"))
        }

        if(attendanceItem.approval_status == "1"){
            holder.iv_action_Attendance_approve.setBackgroundResource(R.drawable.ic_status2)
        } else {
            holder.iv_action_Attendance_approve.setBackgroundResource(R.drawable.ic_status)
        }

        holder.ch_approve.setOnCheckedChangeListener { _, isChecked ->
            itemClickListener.onCheckboxChanged(attendanceItem.id, isChecked)
        }
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){

        var ch_approve : CheckBox
        var tv_worker : TextView
        var tv_contractor : TextView
        var tv_attendance : TextView
        var iv_action_Attendance_approve : ImageView
        var ll_layout : LinearLayout
        init {
            ch_approve=view.findViewById(R.id.ch_approve)
            tv_worker = view.findViewById(R.id.tv_worker)
            tv_contractor = view.findViewById(R.id.tv_contractor)
            tv_attendance = view.findViewById(R.id.tv_attendance)
            iv_action_Attendance_approve = view.findViewById(R.id.iv_action_Attendance_approve)
            ll_layout = view.findViewById(R.id.ll_layout)

        }
    }
}