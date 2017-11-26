package com.henallux.moveandseeandroid.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.DAO.VoteDescriptionDAO;
import com.henallux.moveandseeandroid.DAO.VoteInterestPointDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.VoteDescription;
import com.henallux.moveandseeandroid.Model.VoteInterestPoint;
import com.henallux.moveandseeandroid.R;
import com.henallux.moveandseeandroid.View.HomeConnectedActivity;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Alexandre on 18-11-17.
 */

public class CustomListDescription extends ArrayAdapter<DescriptionWithVote> {
    //Variables d'instance
    HomeConnectedActivity homeConnectedActivity;

    //View viewDescription;

    public CustomListDescription(@NonNull Context context, ArrayList<DescriptionWithVote> listDescription) {
        super(context, R.layout.list_view_descriptions, listDescription);
        homeConnectedActivity = (HomeConnectedActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View view, @NonNull ViewGroup viewGroup){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View viewDescription = layoutInflater.inflate(R.layout.list_view_descriptions, viewGroup, false);

        final DescriptionWithVote descriptionWithVote = getItem(position);

        TextView textPseudo = (TextView) viewDescription.findViewById(R.id.title_pseudo_interest_point);
        TextView textExplication = (TextView) viewDescription.findViewById(R.id.description);
        TextView textAverage = (TextView) viewDescription.findViewById(R.id.rate);



            textPseudo.setText(descriptionWithVote.description.idUserNavigation.userName);
            textExplication.setText(descriptionWithVote.description.explication);

            if(descriptionWithVote.average != -1){
                textAverage.setText(Integer.toString(descriptionWithVote.average) +"%");
            }


        //Gestion du pouce positive
        ImageButton addVotePositive = (ImageButton) viewDescription.findViewById(R.id.thumb_up);
        addVotePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            VoteDescription voteDescriptionPositive = new VoteDescription(true,"898c5147-3b5c-44ad-a3c8-bd8393175c6a",descriptionWithVote.description.idDescription);
            new AddVoteDescriptionAsync().execute(voteDescriptionPositive);
            }
        });

        //Gestion du pouce négatif
        ImageButton addVoteNegative = (ImageButton) viewDescription.findViewById(R.id.thumb_down);
        addVoteNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VoteDescription voteDescriptionNegative = new VoteDescription(false,"898c5147-3b5c-44ad-a3c8-bd8393175c6a",descriptionWithVote.description.idDescription);
                new AddVoteDescriptionAsync().execute(voteDescriptionNegative);
            }
        });



        return viewDescription;

    }

    //Classe Async (VoteInterestPoint)
    private class AddVoteDescriptionAsync extends AsyncTask<VoteDescription,Void,Integer>
    {
        @Override
        protected Integer doInBackground(VoteDescription... params) {
            Integer resultCode =0;
            VoteDescriptionDAO voteInterestPointDAO=new VoteDescriptionDAO();

            try{
                resultCode = voteInterestPointDAO.addVoteDescription(homeConnectedActivity.getToken(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode)
        {
            if(resultCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(getContext(), "Vote enregistré", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Vote non enregistré", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
