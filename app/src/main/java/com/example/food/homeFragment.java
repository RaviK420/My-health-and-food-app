package com.example.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.food.Adapters.RandomRecipeAdapter;
import com.example.food.Listener.RandomRecipeListener;
import com.example.food.Listener.RecipeClickListener;
import com.example.food.apiModels.GetRandomRecipeResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    String diet;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(getActivity(),new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                diet = value.getString("Diet");
            }
        });


        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("loading...");

        searchView = rootView.findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Displays recipes depending on diet
                tags.clear();

                if (!diet.equals("none")){
                    tags.add(query+','+diet);
                }
                else{
                    tags.add(query);
                }
                manager.getRandomRecipes(randomRecipeListener,tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = rootView.findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(getActivity());
//        manager.getRandomRecipes(randomRecipeListener);
//        dialog.show();
        return rootView;
    }
    private final RandomRecipeListener randomRecipeListener = new RandomRecipeListener() {
        @Override
        public void fetch(GetRandomRecipeResponse response, String message) {
            dialog.dismiss();
            recyclerView = getActivity().findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
            randomRecipeAdapter = new RandomRecipeAdapter(getActivity(),response.recipes,recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void error(String message) {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        }
    };
    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();


            if (!diet.equals("none")){
                tags.add(adapterView.getSelectedItem().toString()+','+diet);
            }
            else{
                tags.add(adapterView.getSelectedItem().toString());
            }

            manager.getRandomRecipes(randomRecipeListener,tags);
            dialog.show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        //What happens if a user selects  a recipe
        public void OnRecipeClicked(String id,String image, String likes,String name, String serving,String time) {
            startActivity(new Intent(getActivity(),RecipeDetailActivity.class)
                    .putExtra("id",id)
                    .putExtra("image",image)
                    .putExtra("likes",likes)
                    .putExtra("name",name)
                    .putExtra("serving",serving)
                    .putExtra("time",time));


        }
    };

}
