package com.example.chatbotopenai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.chatbotopenai.Constants;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiServiceInterface apiService = new Retrofit.Builder()
                .baseUrl(Constants.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServiceInterface.class);

        ApiRequest request = new ApiRequest(
                "gpt-3.5-turbo",
                Arrays.asList(
                        new ApiRequest.ChatMessage("system", "You are a helpful assistant."),
                        new ApiRequest.ChatMessage("user", "Hello!")
                )
        );

        try {
            retrofit2.Response<ApiResponse> response = apiService.getChatResponse("Bearer "+Constants.getAPI_KEY(), request).execute();

            if (response.isSuccessful()) {
                ApiResponse completionResponse = response.body();
                Log.d("TAG", "onCreate: "+ completionResponse.getChoices().get(0).getMessage().getContent());
                // Handle the completion response
            } else {
                // Handle errors
                System.out.println("Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}