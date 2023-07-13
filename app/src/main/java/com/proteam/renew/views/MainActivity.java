package com.proteam.renew.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.proteam.renew.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_add_newWorker,ll_id_scan,ll_approvals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_add_newWorker = findViewById(R.id.ll_add_newWorker);
        ll_add_newWorker.setOnClickListener(this);
        ll_id_scan = findViewById(R.id.ll_id_scan);
        ll_id_scan.setOnClickListener(this);
        ll_approvals = findViewById(R.id.ll_approvals);
        ll_approvals.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_add_newWorker:
                Intent intent= new Intent(MainActivity.this, WorkerInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_id_scan:
               // Intent intentScan= new Intent(MainActivity.this, ScanIdActivity.class);
               // startActivity(intentScan);
                break;
            case R.id.ll_approvals:
                Intent intentapprovals= new Intent(MainActivity.this, ApprovalsActivity.class);
                startActivity(intentapprovals);
                break;

        }

    }
}