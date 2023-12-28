package com.example.chatbotopenai;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServiceInterface {

    @POST("chat/completions")
    Call<ApiResponse> getChatResponse(
            @Header("Authorization") String ApiKey,
            @Body ApiRequest requestBody
    );

}
