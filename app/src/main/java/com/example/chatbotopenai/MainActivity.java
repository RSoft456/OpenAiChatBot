package com.example.chatbotopenai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chatbotopenai.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText editText;
    ImageButton imageButton;
    ArrayList<ApiResponse.Message> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.question);
        imageButton = (ImageButton) findViewById(R.id.sendBtn);
        ChatAdapter adapter = new ChatAdapter(this,chatList);
        listView.setAdapter(adapter);
        ApiServiceInterface apiService = new Retrofit.Builder()
                .baseUrl(Constants.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServiceInterface.class);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = editText.getText().toString();
                if(question.trim() != null || !question.trim().equals("")){
                    ApiResponse.Message message = new ApiResponse.Message();
                    message.setRole("user");
                    message.setContent(question);
                    chatList.add(message);
                    adapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(chatList.size()-1);
                    ApiRequest request = new ApiRequest(
                            "gpt-3.5-turbo",
                            Arrays.asList(
                                    new ApiRequest.ChatMessage("user",question)
                            )
                    );
                    apiService.getChatResponse("Bearer " + Constants.getAPI_KEY(), request).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            try{
                            if (response.isSuccessful() && response.body() != null) {
                                ApiResponse completionResponse = response.body();
                                Log.d("TAG", "onResponse: " + completionResponse.getChoices().get(0).getMessage().getContent());
                                Toast.makeText(MainActivity.this, ""+completionResponse.getChoices().get(0).getMessage().getRole(), Toast.LENGTH_SHORT).show();
                                ApiResponse.Message assistantMessage = new ApiResponse.Message();
                                assistantMessage.setRole(completionResponse.getChoices().get(0).getMessage().getRole());
                                assistantMessage.setContent(completionResponse.getChoices().get(0).getMessage().getContent());
                                Log.d("TAG", "onResponse: " + assistantMessage.getContent() +"role "+assistantMessage.getRole());

                                chatList.add(assistantMessage);
                                adapter.notifyDataSetChanged();
                                listView.smoothScrollToPosition(chatList.size()-1);
                            } else {
                                // Handle errors
                                ApiResponse.Message errorMessage = new ApiResponse.Message();
                                errorMessage.setRole("assistant");
                                errorMessage.setContent("Something went wrong! Please try again!");
                                chatList.add(errorMessage);
                                adapter.notifyDataSetChanged();
                                listView.smoothScrollToPosition(chatList.size()-1);
                            }}catch (Exception e){
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            // Handle failures
                            Log.e("TAG", "onFailure: " + t.getMessage());
                        }
                    });

                }
            }
        });


    }
}