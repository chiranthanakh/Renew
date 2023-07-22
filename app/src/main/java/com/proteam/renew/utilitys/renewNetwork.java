package com.proteam.renew.utilitys;

import com.proteam.renew.requestModels.ApprovalRequest;
import com.proteam.renew.requestModels.RejectRequest;
import com.proteam.renew.requestModels.AttendancApproveRequest;
import com.proteam.renew.requestModels.AttendanceRequest;
import com.proteam.renew.requestModels.Loginmodel;
import com.proteam.renew.requestModels.OnBoarding;
import com.proteam.renew.requestModels.TrainingAllocationRequest;
import com.proteam.renew.requestModels.TrainingListResponsce;
import com.proteam.renew.responseModel.AttendenceListResponsce;
import com.proteam.renew.responseModel.ContractorListResponsce;
import com.proteam.renew.responseModel.EmployeedetailResponsce;
import com.proteam.renew.responseModel.LocationResponse;
import com.proteam.renew.responseModel.LoginResponse;
import com.proteam.renew.responseModel.SupervisorListResponsce;
import com.proteam.renew.responseModel.ViewActivityMasterResponsce;
import com.proteam.renew.responseModel.ViewProjectMaster;
import com.proteam.renew.responseModel.ViewSkillsetMaster;
import com.proteam.renew.responseModel.ViewTrainingMaster;
import com.proteam.renew.responseModel.generalGesponce;
import com.proteam.renew.responseModel.statesResponse;
import com.proteam.renew.responseModel.workersListResponsce;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface renewNetwork {
    @POST("login")
    Call<LoginResponse> validatelogin(@Body Loginmodel loginmodel);

    @GET("viewStateMaster")
    Call<statesResponse> stateslist();

    @GET("viewLocationMaster")
    Call<LocationResponse> locationlist();

    @GET("workers")
    Call<workersListResponsce> workerslist();

    @GET("viewProjectMaster")
    Call<ViewProjectMaster> projectslist();

    @GET("viewSkillSetMaster")
    Call<ViewSkillsetMaster> skillslist();

    @GET("viewTrainingMaster")
    Call<ViewTrainingMaster> trininglist();

    @GET("contractor_list/")
    Call<ContractorListResponsce> contractors(@Query("user_id") String user_id);

    @GET("supervisor_list/")
    Call<SupervisorListResponsce> supervisor(@Query("user_id") String user_id);

    @POST("create_employee")
    Call<Boolean> onBoarding(@Body OnBoarding onBording);

    @PUT("updateWorker/{id}")
    Call<generalGesponce> update(@Body OnBoarding onBording,  @Path("id") String id);

    @POST("approve_employee")
    Call<generalGesponce> approve(@Body ApprovalRequest onapprove);

    @POST("reject_employee")
    Call<Boolean> reject(@Body RejectRequest onapprove);

    @GET("viewActivityMaster")
    Call<ViewActivityMasterResponsce> activityList();

    @POST("attendance_insert")
    Call<generalGesponce> attendencepass(@Body AttendanceRequest attendanceRequest);

    @GET("list_attendance/")
    Call<AttendenceListResponsce> attendance_list(@Query("user_id") String user_id);

    @GET("employeeDetails")
    Call<EmployeedetailResponsce> empdetails(@Query("employee") String user_id);

    @PUT("approve_attendance")
    Call<generalGesponce> attendanceapprove(@Body AttendancApproveRequest request);

    @GET("training_allocation_list/")
    Call<TrainingListResponsce> training_list(@Query("user_id") String user_id);

    @POST("allocate_training")
    Call<generalGesponce> trainingaloocate(@Body TrainingAllocationRequest tainingallocate);

}
