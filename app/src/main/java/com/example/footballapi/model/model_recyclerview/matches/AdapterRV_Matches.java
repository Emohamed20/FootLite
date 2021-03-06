package com.example.footballapi.model.model_recyclerview.matches;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.footballapi.R;
import com.example.footballapi.controleur.CrestGenerator;
import com.example.footballapi.model.model_dao.DataBase;
import com.example.footballapi.view.fragments.MatchFragment;
import com.example.footballapi.view.fragments.MatchesFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRV_Matches extends RecyclerView.Adapter<AdapterRV_Matches.ViewHolder> {

    private static final String CLE_DONNEES_ID_MATCH= "idMatch";
    private static final String CLE_DONNEES_ID_HOME= "idHome";
    private static final String CLE_DONNEES_ID_AWAY= "idAway";
    private static final String CLE_DONNEES_STATUS= "status";

    private List<MatchesModel> values;
    private MatchesFragment fragment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatchday;
        TextView tvHomeTeam;
        TextView tvScore;
        TextView tvAwayTeam;
        private ImageView ivLogoClubHome;
        private ImageView ivLogoClubAway;

        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvMatchday = v.findViewById(R.id.tvMatchday);
            tvHomeTeam = v.findViewById(R.id.tvHomeTeam);
            tvScore = v.findViewById(R.id.tvScore);
            tvAwayTeam = v.findViewById(R.id.tvAwayTeam);
            ivLogoClubHome = v.findViewById(R.id.ivLogoHome);
            ivLogoClubAway = v.findViewById(R.id.ivLogoAway);
        }
    }

    public AdapterRV_Matches(List<MatchesModel> myDataset, MatchesFragment fragment) {
        this.values = myDataset;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdapterRV_Matches.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_matches, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvMatchday.setText("J" + values.get(position).getMatchDay());
        holder.tvAwayTeam.setText(values.get(position).getAwayTeam());
        holder.tvScore.setText(values.get(position).getScore());
        holder.tvHomeTeam.setText(values.get(position).getHomeTeam());

        String crestHomeBD = (new DataBase(fragment.getActivity()).findTeamCrest(Integer.valueOf(values.get(position).getIdTeamHome())) != null) ? new DataBase(fragment.getActivity()).findTeamCrest(Integer.valueOf(values.get(position).getIdTeamHome())) : "" ;
        String crestHome = (new CrestGenerator().crestGenerator(values.get(position).getHomeTeam()).equals("")) ? crestHomeBD : new CrestGenerator().crestGenerator(values.get(position).getHomeTeam());

        String crestAwayBD = (new DataBase(fragment.getActivity()).findTeamCrest(Integer.valueOf(values.get(position).getIdTeamAway())) != null) ? new DataBase(fragment.getActivity()).findTeamCrest(Integer.valueOf(values.get(position).getIdTeamAway())) : "" ;
        String crestAway = (new CrestGenerator().crestGenerator(values.get(position).getAwayTeam()).equals("")) ? crestAwayBD : new CrestGenerator().crestGenerator(values.get(position).getAwayTeam());

        if (crestHome.length() >= 4) {
            switch (crestHome.substring(crestHome.length() - 3)) {
                case "svg":
                    SvgLoader.pluck()
                            .with(this.fragment.getActivity())
                            .setPlaceHolder(R.drawable.ic_logo_foreground, R.drawable.ic_logo_foreground)
                            .load(crestHome, holder.ivLogoClubHome)
                            .close();
                    break;
                case "gif":
                case "png":
                    Picasso.get()
                            .load(crestHome)
                            .error(R.drawable.ic_logo_foreground)
                            .resize(50, 50)
                            .centerCrop()
                            .into(holder.ivLogoClubHome);
                    break;
            }
        } else {
            holder.ivLogoClubHome.setImageResource(R.drawable.ic_logo_foreground);
        }

        if (crestAway.length() >= 4) {
            switch (crestAway.substring(crestAway.length() - 3)) {
                case "svg":
                    SvgLoader.pluck()
                            .with(this.fragment.getActivity())
                            .load(crestAway, holder.ivLogoClubAway)
                            .close();
                    break;
                case "gif":

                    break;
                case "png":
                    Picasso.get()
                            .load(crestAway)
                            .resize(50, 50)
                            .centerCrop()
                            .into(holder.ivLogoClubAway);
                    break;
            }
        } else {
            holder.ivLogoClubHome.setImageResource(R.drawable.ic_logo_foreground);
        }

        if (values.get(position).getWinner() != null) {
            switch (values.get(position).getWinner()) {
                case "HOME_TEAM":
                    holder.tvHomeTeam.setTypeface(null, Typeface.BOLD);
                    holder.tvAwayTeam.setTypeface(null, Typeface.NORMAL);
                    holder.tvHomeTeam.setTextColor(Color.BLACK);
                    holder.tvAwayTeam.setTextColor(Color.BLACK);
                    break;
                case "AWAY_TEAM":
                    holder.tvHomeTeam.setTypeface(null, Typeface.NORMAL);
                    holder.tvAwayTeam.setTypeface(null, Typeface.BOLD);
                    holder.tvHomeTeam.setTextColor(Color.BLACK);
                    holder.tvAwayTeam.setTextColor(Color.BLACK);
                    break;
                case "DRAW":  // Match nul
                    holder.tvHomeTeam.setTypeface(null, Typeface.NORMAL);
                    holder.tvAwayTeam.setTypeface(null, Typeface.NORMAL);
                    holder.tvHomeTeam.setTextColor(Color.GRAY);
                    holder.tvAwayTeam.setTextColor(Color.GRAY);
                    break;
            }
        }
        else{
            holder.tvHomeTeam.setTypeface(null, Typeface.NORMAL);
            holder.tvAwayTeam.setTypeface(null, Typeface.NORMAL);
            holder.tvHomeTeam.setTextColor(Color.BLACK);
            holder.tvAwayTeam.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment matchesFragment = new MatchFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(CLE_DONNEES_ID_MATCH, Integer.parseInt(values.get(position).getIdMatch()));
                bundle.putInt(CLE_DONNEES_ID_HOME, Integer.parseInt(values.get(position).getIdTeamHome()));
                bundle.putInt(CLE_DONNEES_ID_AWAY, Integer.parseInt(values.get(position).getIdTeamAway()));
                bundle.putString(CLE_DONNEES_STATUS, values.get(position).getStatus());
                matchesFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) fragment.getContext();
                if (activity != null) activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left).replace(R.id.fragment_hoster, matchesFragment).addToBackStack(null).commit();

            }
        });

        switch (values.get(position).getStatus()) {
            case "LIVE":
            case "IN_PLAY":
                holder.itemView.setBackgroundResource(R.drawable.live);
                break;
            case "PAUSED":
            case "SUSPENDED":
                holder.itemView.setBackgroundResource(R.drawable.paused);
                break;
            case "CANCELED":
                holder.itemView.setBackgroundResource(R.drawable.canceled);
                break;
            case "FINISHED":
                holder.itemView.setBackgroundResource(R.drawable.terminated);
                break;
            case "SCHEDULED":
                holder.itemView.setBackgroundResource(R.drawable.scheduled);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}