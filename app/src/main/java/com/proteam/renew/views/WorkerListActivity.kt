package com.proteam.renew.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import com.proteam.renew.R

class WorkerListActivity : AppCompatActivity() {

    val iv_Add_NewWorker: ImageView by lazy { findViewById<ImageView>(R.id.iv_Add_NewWorker) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_list)

        iv_Add_NewWorker.setOnClickListener(View.OnClickListener
        {
            val intent = Intent(this@WorkerListActivity, WorkerInformationActivity::class.java)
            startActivity(intent)
        })
    }
}