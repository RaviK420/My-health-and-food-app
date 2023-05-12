package com.example.food.Listener;

import com.example.food.apiModels.AnalyzedInstruction;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<AnalyzedInstruction> response, String message);
    void didError(String Message);
}
