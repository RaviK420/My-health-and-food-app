package com.example.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.food.Adapters.FavouriteAdapter;
import com.example.food.Listener.RecipeClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    ProgressDialog dialog;
    FavouriteAdapter favouriteAdapter;
    RecyclerView recyclerView;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    DocumentReference documentReference;
    ArrayList<String> favouriteID,favouriteImage,favouriteName,favouriteLikes,favouriteServing,favouriteTime;
    Button update;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading...");

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();
        documentReference = fStore.collection("users").document(userID);
        update = rootView.findViewById(R.id.updateButton);
        dialog.show();


        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //Displays favourite recipes by taking a snapshot of the database
                favouriteID = (ArrayList<String>) value.get("Favourite_ID");
                favouriteImage = (ArrayList<String>) value.get("Image");
                favouriteLikes = (ArrayList<String>) value.get("Likes");
                favouriteName = (ArrayList<String>) value.get("Name");
                favouriteServing = (ArrayList<String>) value.get("Serving");
                favouriteTime = (ArrayList<String>) value.get("Time");

                dialog.dismiss();
                recyclerView = getActivity().findViewById(R.id.favourite_recyclerView);

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                favouriteAdapter = new FavouriteAdapter(getActivity(),favouriteID,favouriteImage,favouriteLikes,favouriteName,favouriteServing,favouriteTime,recipeClickListener);
                recyclerView.setAdapter(favouriteAdapter);
                dialog.dismiss();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            //updates favourite section
            @Override
            public void onClick(View view) {
                dialog.show();
                documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        favouriteID = (ArrayList<String>) value.get("Favourite_ID");
                        favouriteImage = (ArrayList<String>) value.get("Image");
                        favouriteLikes = (ArrayList<String>) value.get("Likes");
                        favouriteName = (ArrayList<String>) value.get("Name");
                        favouriteServing = (ArrayList<String>) value.get("Serving");
                        favouriteTime = (ArrayList<String>) value.get("Time");

                        dialog.dismiss();
                        recyclerView = getActivity().findViewById(R.id.favourite_recyclerView);

                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        favouriteAdapter = new FavouriteAdapter(getActivity(),favouriteID,favouriteImage,favouriteLikes,favouriteName,favouriteServing,favouriteTime,recipeClickListener);
                        recyclerView.setAdapter(favouriteAdapter);

                    }
                });
            }
        });



        return rootView;
    }
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void OnRecipeClicked(String id, String image, String likes, String name, String serving, String time) {
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