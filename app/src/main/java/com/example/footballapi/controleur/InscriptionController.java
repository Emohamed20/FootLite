package com.example.footballapi.controleur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.footballapi.model.model_session_manager.SessionManagerPreferences;
import com.example.footballapi.model.model_retrofit.always_data.Supporter;
import com.example.footballapi.model.model_retrofit.restService.always_data.RestAlwaysData;
import com.example.footballapi.view.activities.InscriptionActivity;
import com.example.footballapi.view.activities.MainActivity;

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
     * @param pseudo
     * @param password
     */
    public void onCreate(String pseudo, String password) {
        Call<Supporter> call = RestAlwaysData.get().inscription(pseudo, password, 4); /** ATTENTION A CHANGER !! **/
        call.enqueue(new Callback<Supporter>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Supporter> call, @NonNull Response<Supporter> response) {
                if (response.isSuccessful()) {
                    final Supporter supporter = response.body();
                    assert supporter != null;

                    // Ajouter les données dans les SharedPreferences
                    SessionManagerPreferences.getSettings(activity.getApplicationContext()).sign_in(supporter.getIdSupporter(), supporter.getPseudo(), supporter.getPassword(), supporter.getFavoriteTeam(), supporter.getTab_bets());
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Toast.makeText(activity, "Le nombre d'appels a été dépassé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Supporter> call, @NonNull Throwable t) {
                Toast.makeText(activity, "Vérifiez votre connexion_activity Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
