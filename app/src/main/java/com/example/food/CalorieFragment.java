package com.example.food;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.Adapters.CalorieAdapter;
import com.example.food.Listener.CalorieClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class CalorieFragment extends Fragment {
    Button addButton,infoButton;
    RecyclerView recyclerView;
    TextView caloriesEaten;
    DocumentReference documentReference;
    String userID;
    ArrayList<String> name,calories,Image,amount,id;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CalorieAdapter calorieAdapter;
    Integer totalCalories;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_calorie,container,false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        addButton = viewRoot.findViewById(R.id.addButton);
        infoButton = viewRoot.findViewById(R.id.infoButton);
        recyclerView = viewRoot.findViewById(R.id.calorieRecyclerView);
        caloriesEaten = viewRoot.findViewById(R.id.caloriesEaten);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Allows user to add recipes to calorie tracker
               Intent intent = new Intent(getActivity(),addRecipeToCalorieCount.class);
               startActivity(intent);
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            //Displays infomation on calories
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Calorie Info")
                        .setMessage("Please note that the daily calorie intake is 2500 for men and 2000 for women told from the NHS")
                        .setPositiveButton("Learn more", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url = "https://www.nhs.uk/common-health-questions/food-and-diet/what-should-my-daily-intake-of-calories-be/#:~:text=An%20ideal%20daily%20intake%20of,women%20and%202%2C500%20for%20men.";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();
        documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = (ArrayList<String>) documentSnapshot.get("calName");
                calories = (ArrayList<String>) documentSnapshot.get("calories");
                Image = (ArrayList<String>) documentSnapshot.get("calImages");
                amount = (ArrayList<String>) documentSnapshot.get("amount");
                id = (ArrayList<String>) documentSnapshot.get("calID");

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                calorieAdapter = new CalorieAdapter(getActivity(),name,Image,calories,amount,id,calorieClickListener);
                recyclerView.setAdapter(calorieAdapter);

                totalCalories = 0;
                for(int x = 0; x < calories.size();x++ ){
                    totalCalories += Integer.parseInt(calories.get(x)) * (Integer.parseInt(amount.get(x)) - Integer.parseInt(id.get(x)));
                }
                if(totalCalories>0){
                    caloriesEaten.setText("Total Calories Eaten : "+Integer.toString(totalCalories));
                }


            }
        });




        return viewRoot;
    }
    private final CalorieClickListener calorieClickListener = new CalorieClickListener() {
        //changes the amount per recipe
        @Override
        public void onclicklistener(String name, String images, String calories, String a, String calID) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("change amount")
                    .setMessage("Would you want to add or remove this recipe")
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    progressDialog.show();
                                    ArrayList<String> aAmount = (ArrayList<String>) documentSnapshot.get("amount");
                                    ArrayList<String> aName = (ArrayList<String>) documentSnapshot.get("calName");
                                    ArrayList<String> aImages = (ArrayList<String>) documentSnapshot.get("calImages");
                                    ArrayList<String> aCalories = (ArrayList<String>) documentSnapshot.get("calories");
                                    ArrayList<String> aID = (ArrayList<String>) documentSnapshot.get("calID");

                                    Integer pos = 0;

                                    for(int x =0;x<aAmount.size();x++){
                                        if(aAmount.get(x).equals(a)){
                                            pos = x;
                                        }
                                    }
                                    aAmount.set(pos,Integer.toString(Integer.parseInt(a)+1));

                                    documentReference.update("amount", aAmount);
                                    calorieAdapter = new CalorieAdapter(getActivity(), aName, aImages, aCalories, aAmount, aID, calorieClickListener);
                                    recyclerView.setAdapter(calorieAdapter);
                                    totalCalories = 0;
                                    for(int x = 0 ; x<aAmount.size();x++){
                                        totalCalories += Integer.parseInt(aCalories.get(x)) * (Integer.parseInt(aAmount.get(x)) - Integer.parseInt(aID.get(x)));

                                    }
                                    caloriesEaten.setText("Total calories eaten: "+Integer.toString(totalCalories));
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    progressDialog.show();
                                    ArrayList<String> aAmount = (ArrayList<String>) documentSnapshot.get("amount");
                                    ArrayList<String> aName = (ArrayList<String>) documentSnapshot.get("calName");
                                    ArrayList<String> aImages = (ArrayList<String>) documentSnapshot.get("calImages");
                                    ArrayList<String> aCalories = (ArrayList<String>) documentSnapshot.get("calories");
                                    ArrayList<String> aID = (ArrayList<String>) documentSnapshot.get("calID");

                                    int pos = 0;

                                    for(int x =0;x<aAmount.size();x++){
                                        if(aAmount.get(x).equals(a)){
                                            pos = x;
                                        }
                                    }
                                    //Toast.makeText(getActivity(), Integer.toString(Integer.parseInt(aAmount.get(pos))-Integer.parseInt(aID.get(pos))-1), Toast.LENGTH_SHORT).show();
                                    if(Integer.parseInt(aAmount.get(pos))-Integer.parseInt(aID.get(pos))-1 <= 0){

                                        aAmount.remove(0);
                                        aName.remove(0);
                                        aImages.remove(0);
                                        aCalories.remove(0);
                                        aID.remove(0);
                                        documentReference.update("amount", aAmount);
                                        documentReference.update("calID",aID);
                                        documentReference.update("calImages",aImages);
                                        documentReference.update("calName",aName);
                                        documentReference.update("calories",aCalories);

                                    }
                                    else {
                                        aAmount.set(pos, Integer.toString(Integer.parseInt(a) - 1));

                                        documentReference.update("amount", aAmount);
                                    }
                                        calorieAdapter = new CalorieAdapter(getActivity(), aName, aImages, aCalories, aAmount, aID, calorieClickListener);
                                        recyclerView.setAdapter(calorieAdapter);
                                        totalCalories = 0;
                                        if(aAmount.size()>0) {
                                            for (int x = 0; x < aAmount.size(); x++) {
                                                totalCalories += Integer.parseInt(aCalories.get(x)) * (Integer.parseInt(aAmount.get(x)) - Integer.parseInt(aID.get(x)));

                                            }
                                        }
                                        caloriesEaten.setText("Total calories eaten: " + Integer.toString(totalCalories));
                                        progressDialog.dismiss();

                                }
                            });

                        }
                    })
                    .show();

        }
    };
}