package com.example.footballapi.controleur;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.footballapi.model.model_retrofit.supporter.Supporter;
import com.example.footballapi.model.model_retrofit.retrofit.always_data.RestAlwaysData;
import com.example.footballapi.view.activities.InscriptionActivity;
import com.example.footballapi.view.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionController {

    private InscriptionActivity activity;

    public InscriptionController(InscriptionActivity activity) {
        this.activity = activity;
    }

    /**
     * Inscrire un supporter
     * @param pseudo pseudo du supporter
     * @param password mot de passe du supporter
     */
    public void onCreate(String pseudo, String password, int favoriteTeamId, String favoriteTeamName) {
        Call<Supporter> call = RestAlwaysData.get().inscription(pseudo, password, favoriteTeamId, favoriteTeamName);
        call.enqueue(new Callback<Supporter>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Supporter> call, @NonNull Response<Supporter> response) {
                if (response.isSuccessful()) {
                    final Supporter supporter = response.body();
                    assert supporter != null;

                    if (supporter.getIdSupporter() == -1){
                        Snackbar.make(activity.contextView, "Ce pseudo est déjà pris", Snackbar.LENGTH_SHORT).show();
                    }else {
                        // Ajouter les données dans les SharedPreferences
                        SessionManagerPreferences.getSettings(activity.getApplicationContext()).sign_in(supporter.getIdSupporter(), supporter.getPseudo(), supporter.getPassword(), supporter.getFavoriteTeam(), supporter.getFavoriteTeamName(), supporter.getTab_bets());
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Supporter> call, @NonNull Throwable t) {
                Snackbar.make(activity.contextView, "Vérifiez votre connexion Internet", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
