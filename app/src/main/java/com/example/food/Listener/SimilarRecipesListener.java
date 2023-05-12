package com.example.food.Listener;

import com.example.food.apiModels.RecipeDetailsResponse;
import com.example.food.apiModels.SimilarRecipesResponse;

import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipesResponse> response, String message);
    void didError(String message);
}
