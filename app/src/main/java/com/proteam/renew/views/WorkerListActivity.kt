package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proteam.renew.Adapter.WorkerListAdapter
import com.proteam.renew.R
import com.proteam.renew.responseModel.workersListResponsce
import com.proteam.renew.responseModel.workersListResponsceItem
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.WebServices

class WorkerListActivity : AppCompatActivity(),OnResponseListener<Any> {

    val iv_Add_NewWorker: ImageView by lazy { findViewById<ImageView>(R.id.iv_Add_NewWorker) }
    var contractorList = ArrayList<workersListResponsceItem>()
    var progressDialog: ProgressDialog? = null
    val rv_worker_list: RecyclerView by lazy { findViewById(R.id.rv_worker_list) }
    var value: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_list)
        val bundle = intent.extras
        if (bundle != null) {
             value = bundle.getBoolean("approval")
        }
        callLoginapi()

        rv_worker_list.layoutManager = LinearLayoutManager(this)
        iv_Add_NewWorker.setOnClickListener(View.OnClickListener
        {
            val intent = Intent(this@WorkerListActivity, WorkerInformationActivity::class.java)
            val sharedPreferences1: SharedPreferences =applicationContext.getSharedPreferences("workerPref", Context.MODE_PRIVATE)!!
            val editor1: SharedPreferences.Editor = sharedPreferences1.edit()
            editor1.putBoolean("edit",false)
            editor1.commit()
            startActivity(intent)
        })
    }

    private fun callLoginapi() {
        progressDialog = ProgressDialog(this@WorkerListActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val webServices2 = WebServices<Any>(this@WorkerListActivity)
                webServices2.workers(WebServices.ApiType.workers)
            } else {
            }
        }
    }
    override fun onResponse(
        response: Any?,
        URL: WebServices.ApiType?,
        isSucces: Boolean,
        code: Int
    ) {
        when (URL) {
            WebServices.ApiType.workers -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val workerslist = response as workersListResponsce
                    if (workerslist?.isEmpty() == false) {
                        for (x in workerslist) {
                            if(value){
                                if(x.approval_status == "0"){
                                    contractorList.add(x)
                                }
                            }else{
                                contractorList.add(x)
                            }
                        }
                        val adapter = WorkerListAdapter(contractorList, getApplicationContext(),value)
                        rv_worker_list.adapter = adapter
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