package com.proteam.renew.utilitys;

import com.proteam.renew.requestModels.Loginmodel;
import com.proteam.renew.requestModels.OnBoarding;
import com.proteam.renew.responseModel.ContractorListResponsce;
import com.proteam.renew.responseModel.LocationResponse;
import com.proteam.renew.responseModel.LoginResponse;
import com.proteam.renew.responseModel.SupervisorListResponsce;
import com.proteam.renew.responseModel.ViewProjectMaster;
import com.proteam.renew.responseModel.ViewProjectMasterItem;
import com.proteam.renew.responseModel.ViewSkillsetMaster;
import com.proteam.renew.responseModel.ViewSkillsetMasterItem;
import com.proteam.renew.responseModel.ViewTrainingMaster;
import com.proteam.renew.responseModel.ViewTrainingMasterItem;
import com.proteam.renew.responseModel.WorkersModelItem;
import com.proteam.renew.responseModel.statesResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<WorkersModelItem> workerslist();

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
   }
