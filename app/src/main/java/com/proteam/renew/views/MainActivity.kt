package com.proteam.renew.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proteam.renew.R

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var ll_add_newWorker: LinearLayout? = null
    var ll_id_scan: LinearLayout? = null
    var ll_approvals: LinearLayout? = null
    var iv_logout: ImageView? = null
    var userid: String = ""
    var rollid : String = ""
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreferences2: SharedPreferences =getSharedPreferences("myPref", Context.MODE_PRIVATE)!!
           rollid = sharedPreferences2.getString("rollid", "")!!
            userid = sharedPreferences2.getString("userid", "")!!

        if(userid == ""){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ll_add_newWorker = findViewById(R.id.ll_add_newWorker)
        ll_add_newWorker?.setOnClickListener(this)
        ll_id_scan = findViewById(R.id.ll_id_scan)
        ll_id_scan?.setOnClickListener(this)
        ll_approvals = findViewById(R.id.ll_approvals)
        ll_approvals?.setOnClickListener(this)
        iv_logout = findViewById(R.id.iv_logout)

        if(rollid == "1"){
            ll_approvals?.visibility = View.GONE
            ll_id_scan?.visibility = View.GONE
        }
        iv_logout?.setOnClickListener {
            val sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomnavigation()

    }

    private fun bottomnavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavShift)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {

                    if(rollid != "1"){

                    }
                    true
                }
                R.id.it_scan -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@MainActivity, ScanIdActivity::class.java)
                        startActivity(intentScan)
                    }
                    true
                }
                R.id.it_approve -> {
                    if(rollid != "1") {
                        val intentapprovals =
                            Intent(this@MainActivity, ApprovalsActivity::class.java)
                        val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("nav", true)
                        editor.commit()
                        startActivity(intentapprovals)
                    }
                    // Handle the Profile action
                    true
                }
                R.id.it_worker -> {
                    if(rollid != "1") {
                        val intentScan = Intent(this@MainActivity, WorkerListActivity::class.java)
                        startActivity(intentScan)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_add_newWorker -> {
                val intent = Intent(this@MainActivity, WorkerListActivity::class.java)
                val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("nav",false)
                editor.commit()
                startActivity(intent)
            }

            R.id.ll_id_scan -> {
                val intentScan = Intent(this@MainActivity, ScanIdActivity::class.java)
                startActivity(intentScan)
            }

            R.id.ll_approvals -> {
                val intentapprovals = Intent(this@MainActivity, ApprovalsActivity::class.java)
                val prefs = getSharedPreferences("onboard", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("nav",true)
                editor.commit()
                startActivity(intentapprovals)
            }
        }
    }
}