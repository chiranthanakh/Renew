package com.proteam.renew.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.R

class WorkerListAdapter(): RecyclerView.Adapter<WorkerListAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.layout_worker_list,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){

         var tv_status : TextView
         var tv_approval : TextView
         var tv_doj : TextView
         var tv_name : TextView
         var tv_id : TextView
         var iv_action : ImageView
        init {

            tv_status=view.findViewById(R.id.tv_status)
            tv_approval = view.findViewById(R.id.tv_approval)
            tv_doj = view.findViewById(R.id.tv_doj)
            tv_name = view.findViewById(R.id.tv_name)
            tv_id = view.findViewById(R.id.tv_id)
            iv_action = view.findViewById(R.id.iv_action)
        }


    }
}