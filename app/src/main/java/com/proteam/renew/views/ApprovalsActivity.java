package com.proteam.renew.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.proteam.renew.R;

public class ApprovalsActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_OnBoard_approval,ll_training_approval,ll_attendance_approval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals);

        ll_OnBoard_approval = findViewById(R.id.ll_OnBoard_approval);
        ll_OnBoard_approval.setOnClickListener(this);
        ll_attendance_approval = findViewById(R.id.ll_attendance_approval);
        ll_attendance_approval.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_OnBoard_approval:

                Intent intent= new Intent(ApprovalsActivity.this, WorkerDetailsActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_attendance_approval:

                Intent intentAttendence= new Intent(ApprovalsActivity.this, AttendenceApproveActivity.class);
                startActivity(intentAttendence);
                break;


        }
    }
}