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
import com.google.zxing.integration.android.IntentIntegrator
import com.proteam.renew.R
import com.proteam.renew.requestModels.TrainingListResponsce
import com.proteam.renew.responseModel.AttendenceListResponsce
import com.proteam.renew.utilitys.CheckboxListner
import com.proteam.renew.utilitys.TrainingListner

class TrainingApproveListAdapter(private val mList: TrainingListResponsce, var applicationContext: Context,private val itemClickListener: TrainingListner): RecyclerView.Adapter<TrainingApproveListAdapter.MyViewHolder>()  {

    private lateinit var qrCodeScanner: IntentIntegrator

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.layout_training_allocation_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trainings = mList[position]
        holder.tv_training.text = trainings.training_name
        holder.tv_trainer.text = trainings.trainer_name
        holder.tv_date.text = trainings.date_allocation

        if((position % 2) != 0){
            holder.ll_layout.setBackgroundColor(Color.parseColor("#BFF1D1"))
        }

        holder.tv_scan.setOnClickListener {
            itemClickListener.traininglisten(trainings)
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){

        var tv_date : TextView
        var tv_training : TextView
        var tv_trainer : TextView
        var tv_scan : TextView
        var ll_layout : LinearLayout
        init {
            tv_date = view.findViewById(R.id.tv_date)
            tv_training = view.findViewById(R.id.tv_training)
            tv_trainer = view.findViewById(R.id.tv_trainer)
            tv_scan = view.findViewById(R.id.tv_scan)
            ll_layout = view.findViewById(R.id.ll_layout)

        }
    }
}