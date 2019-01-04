package com.amandeep.recordandsendfile.retrofit;


import com.amandeep.recordandsendfile.ModelUpload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterface {
    // you need to put your file upload url here
    @POST("")
    Call<ModelUpload> uploadTextAndAudio(
            @Body MultipartBody multipartBody
    );

}
