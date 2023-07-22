package com.proteam.renew.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.proteam.renew.Adapter.TrainingApproveListAdapter
import com.proteam.renew.R
import com.proteam.renew.requestModels.TrainingAllocationRequest
import com.proteam.renew.requestModels.TrainingListResponsce
import com.proteam.renew.requestModels.TrainingListResponsceItem
import com.proteam.renew.responseModel.generalGesponce
import com.proteam.renew.utilitys.OnResponseListener
import com.proteam.renew.utilitys.TrainingListner
import com.proteam.renew.utilitys.WebServices

class TrainingAllocationListActivity : AppCompatActivity(), OnResponseListener<Any>,TrainingListner {

    var userid: String = ""
    var rollid : String = ""
    var progressDialog: ProgressDialog? = null
    val rv_traiing: RecyclerView by lazy { findViewById(R.id.rv_traiing) }
    private lateinit var qrCodeScanner: IntentIntegrator
    var traingAlloc : TrainingListResponsceItem? = null

    //val tv_training_approve: TextView by lazy { findViewById(R.id.tv_training_approve) }
    //val checkbox = HashMap<String,Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_allocation_list)

        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences2.getString("rollid", "")!!
        userid = sharedPreferences2.getString("userid", "")!!
        qrCodeScanner = IntentIntegrator(this)
        rv_traiing.layoutManager = LinearLayoutManager(this)
        inilatize()
    }

    private fun inilatize() {
        callListapi()
    }

    private fun callListapi() {
        progressDialog = ProgressDialog(this@TrainingAllocationListActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                val webServices2 = WebServices<Any>(this@TrainingAllocationListActivity)
                webServices2.Traininglist(WebServices.ApiType.traininglist, userid)
            } else {
            }
        }
    }

    private fun callTraingallocate(scannedData: String) {
        progressDialog = ProgressDialog(this@TrainingAllocationListActivity)
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                var alloc = traingAlloc?.let {
                    TrainingAllocationRequest(
                        it.date_of_completion,
                        it.completion_status,it.date_allocation,scannedData,"",it.project_id,
                        it.project_type,"",it.training_master_id,userid)
                }
                val webServices2 = WebServices<Any>(this@TrainingAllocationListActivity)
                webServices2.trainingAllocation(WebServices.ApiType.trainingAllocate, alloc)
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
        when(URL){
            WebServices.ApiType.traininglist -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val traininglist = response as TrainingListResponsce
                    if (traininglist?.isEmpty() == false) {
                        for (x in traininglist) {

                        }
                        val adapter = TrainingApproveListAdapter(traininglist,getApplicationContext(),this)
                        rv_traiing.adapter = adapter
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            WebServices.ApiType.trainingAllocate -> {
                if (progressDialog != null) {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                }
                if (isSucces) {
                    val attendanceres = response as generalGesponce
                    if (attendanceres.status == "200" ) {
                        Toast.makeText(this, "Training added Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Something Went wrong. Please check Internet", Toast.LENGTH_SHORT)
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

    fun scanQRCode() {
        qrCodeScanner.setOrientationLocked(false)
        qrCodeScanner.createScanIntent()
        qrCodeScanner.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


            val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents != null) {
                    val scannedData = result.contents
                    callTraingallocate(scannedData)
                } else {

                }
            }

    }


    override fun traininglisten(trainginfo: TrainingListResponsceItem) {
        traingAlloc = trainginfo
        scanQRCode()
    }
}