package com.proteam.renew.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.R

class AttendenceApproveListAdapter: RecyclerView.Adapter<AttendenceApproveListAdapter.MyViewHolder>()  {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.layout_attendence_approval,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){

        var ch_approve : CheckBox
        var tv_worker : TextView
        var tv_contractor : TextView
        var tv_attendance : TextView
        var iv_action_Attendance_approve : ImageView
        init {

            ch_approve=view.findViewById(R.id.ch_approve)
            tv_worker = view.findViewById(R.id.tv_worker)
            tv_contractor = view.findViewById(R.id.tv_contractor)
            tv_attendance = view.findViewById(R.id.tv_attendance)
            iv_action_Attendance_approve = view.findViewById(R.id.iv_action_Attendance_approve)

        }


    }
}