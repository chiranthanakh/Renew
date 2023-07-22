package com.proteam.renew.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proteam.renew.R

class ApprovalsActivity : AppCompatActivity(), View.OnClickListener {
    var ll_OnBoard_approval: LinearLayout? = null
    var ll_training_approval: LinearLayout? = null
    var ll_attendance_approval: LinearLayout? = null
    var userid: String = ""
    var rollid : String = ""
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approvals)
        ll_OnBoard_approval = findViewById(R.id.ll_OnBoard_approval)
        ll_OnBoard_approval?.setOnClickListener(this)
        ll_attendance_approval = findViewById(R.id.ll_attendance_approval)
        ll_attendance_approval?.setOnClickListener(this)
        ll_training_approval = findViewById(R.id.ll_training_approval)
        ll_training_approval?.setOnClickListener(this)

        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
        rollid = sharedPreferences2.getString("rollid", "")!!
        userid = sharedPreferences2.getString("userid", "")!!

        bottomnavigation()
    }

    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {

                        val intentScan = Intent(this@ApprovalsActivity, MainActivity::class.java)
                        startActivity(intentScan)
                        finish()

                    true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@ApprovalsActivity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@ApprovalsActivity, ApprovalsActivity::class.java)
                        val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("nav", true)
                        editor.commit()
                        startActivity(intentapprovals)
                        finish()
                    }
                    // Handle the Profile action
                    true
                }
                R.id.it_worker -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@ApprovalsActivity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_OnBoard_approval -> {
                val intent = Intent(this@ApprovalsActivity, WorkerListActivity::class.java)
                val bundle = Bundle()
                bundle.putBoolean("approval", true)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            R.id.ll_attendance_approval -> {
                val intentAttendence =
                    Intent(this@ApprovalsActivity, AttendenceApproveActivity::class.java)
                startActivity(intentAttendence)
            }

            R.id.ll_training_approval -> {
                val intentAttendence =
                    Intent(this@ApprovalsActivity, TrainingAllocationListActivity::class.java)
                startActivity(intentAttendence)
            }
        }
    }
}