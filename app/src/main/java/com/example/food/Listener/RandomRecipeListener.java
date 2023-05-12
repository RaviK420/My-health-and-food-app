package com.example.food.Listener;


import com.example.food.apiModels.GetRandomRecipeResponse;

// basic interface for random recipes api listener
public interface RandomRecipeListener {
    void fetch(GetRandomRecipeResponse response, String message);
    void error(String message);
}
