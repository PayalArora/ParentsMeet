package com.logicsquare.parentsmeet.network


import com.logicsquare.parentsmeet.model.*
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse?>

    @POST("/api/v1/register")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<LoginResponse?>

    @POST("/api/v1/get/otp")
    fun getOtp(@Body otpRequest: OtpRequest): Call<LoginResponse?>

    @POST("/api/v1/verify/otp")
    fun submitOtp(@Body otpRequest: SubmitOtpRequest): Call<LoginResponse?>

    @GET("api/v1/user/details")
    fun getProfile(@Header("Authorization") Authorization: String): Call<ProfileResponse?>

    @PUT("api/v1/user/details")
    fun updateProfile(
        @Header("Authorization") Authorization: String,
        @Body profileRequest: ProfileRequest
    ): Call<ProfileResponse?>

    @POST("api/v1/kid/details")
    fun addKid(
        @Header("Authorization") Authorization: String,
        @Body addKidRequest: AddKidRequest
    ): Call<AddKidsResponse?>

    @PUT("/api/v1/kid/details/{kidId}")
    fun updateKid(
        @Path("kidId") id: String?,
        @Header("Authorization") token: String, @Body addKidRequest: UpdateKidRequest
    ): Call<AddKidsResponse?>

    @POST("/api/v1/forums")
    fun getForums(
        @Header("Authorization") token: String,
        @Body getForums: GetAllForum
    ): Call<ForumResponse?>
    @GET("/api/v1/forum/{id}")
    fun getForumDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String?
    ): Call<ForumDetailResponse?>

    @POST("/api/v1/forum/comment/{id}")
    fun addForumComment(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
         @Body addCommentRequest: AddComentRequest
    ): Call<AddCommentResponse?>

    @PUT("/api/v1/forum/like/{id}")
    fun toggleLike(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
    ): Call<LikeForumResponse?>


    @POST("/api/v1/forum/comments/{id}")
    fun getAllComments(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
        @Body getComment: GetComentRequest
    ): Call<GetAllCommentsResponse?>
    @POST("api/v1/blogs")
    fun getAllBlogs(@Header("Authorization") Authorization: String): Call<BlogsResponse?>

    @GET("/api/v1/blog/{id}")
    fun getBlogDetails(
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Call<BlogDetailsResponse?>


    @POST("/api/v1/jobs")
    fun getJobs(
        @Header("Authorization") Authorization: String,
        @Body addJobRequest: AddJobRequest
    ): Call<JobsResponse?>

    @GET("/api/v1/job/{id}")
    fun getJobDetails(
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Call<JobsdetailResponse?>

    @PUT("/api/v1/job/applied/{id}")
    fun applyJob(
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Call<JobAppliedSavedResponse?>

    @PUT("/api/v1/job/saved/{id}")
    fun saveJob(
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Call<JobAppliedSavedResponse?>

}

