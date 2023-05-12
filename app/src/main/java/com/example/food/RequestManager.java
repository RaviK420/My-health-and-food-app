package com.example.food;

import android.content.Context;

import com.example.food.Listener.InstructionsListener;
import com.example.food.Listener.RandomRecipeListener;
import com.example.food.Listener.RecipeDetailsListener;
import com.example.food.Listener.SimilarRecipesListener;
import com.example.food.apiModels.AnalyzedInstruction;
import com.example.food.apiModels.GetRandomRecipeResponse;
import com.example.food.apiModels.RecipeDetailsResponse;
import com.example.food.apiModels.SimilarRecipesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
//Class that has all the API requests
public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeListener listener,List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<GetRandomRecipeResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);
        call.enqueue(new Callback<GetRandomRecipeResponse>() {
            @Override
            public void onResponse(Call<GetRandomRecipeResponse> call, Response<GetRandomRecipeResponse> response) {
                if(!response.isSuccessful()){
                    listener.error(response.message());
                    return;
                }
                listener.fetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<GetRandomRecipeResponse> call, Throwable t) {
                listener.error(t.getMessage());

            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());

            }
        });
    }
    public void getSimilarRecipes(SimilarRecipesListener listener, int id){
        CallSimilarRecipes similarRecipes = retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipesResponse>> call = similarRecipes.callSimilarRecipes(id,"5",context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipesResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipesResponse>> call, Response<List<SimilarRecipesResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<SimilarRecipesResponse>> call, Throwable t) {
                listener.didError(t.getMessage());

            }
        });
    }
    public void getInstructions(InstructionsListener listener, int id){
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<AnalyzedInstruction>> call = callInstructions.callInstructions(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<List<AnalyzedInstruction>>() {
            @Override
            public void onResponse(Call<List<AnalyzedInstruction>> call, Response<List<AnalyzedInstruction>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());

            }

            @Override
            public void onFailure(Call<List<AnalyzedInstruction>> call, Throwable t) {
                listener.didError(t.getMessage());

            }
        });

    }

    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<GetRandomRecipeResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags")List<String> tags
                );
    }
    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
                );
    }
    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipesResponse>> callSimilarRecipes(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<AnalyzedInstruction>>  callInstructions(
          @Path("id") int id,
          @Query("apiKey") String apiKey
        );
    }
}
