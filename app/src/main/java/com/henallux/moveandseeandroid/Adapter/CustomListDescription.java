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

import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
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

        TextView textPseudo = viewDescription.findViewById(R.id.title_pseudo_interest_point);
        TextView textExplication = viewDescription.findViewById(R.id.description);
        TextView textAverage = viewDescription.findViewById(R.id.rate);



        textPseudo.setText(descriptionWithVote.description.idUserNavigation.userName);
        textExplication.setText(descriptionWithVote.description.explication);

        if(descriptionWithVote.average != -1){
            textAverage.setText(Integer.toString(descriptionWithVote.average) +"%");
        }


        //Gestion du pouce positive
        ImageButton addVotePositive = viewDescription.findViewById(R.id.thumb_up);
        addVotePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DescriptionWithVote descriptionWithVoteCurrent = null;
                VoteDescription voteDescriptionPositive = new VoteDescription(true,homeConnectedActivity.getUserCurrent().id,descriptionWithVote.description.idDescription);
                new AddVoteDescriptionAsync().execute(voteDescriptionPositive);

                try{
                    descriptionWithVoteCurrent = new GetDescriptionByIdAsync().execute(descriptionWithVote.description.idDescription).get();
                }catch(Exception e){
                    e.printStackTrace();
                }

                TextView textAverage = viewDescription.findViewById(R.id.rate);


                if(descriptionWithVoteCurrent != null && descriptionWithVoteCurrent.average != -1){
                    textAverage.setText(Integer.toString(descriptionWithVoteCurrent.average) +"%");
                }
            }
        });

        //Gestion du pouce négatif
        ImageButton addVoteNegative = viewDescription.findViewById(R.id.thumb_down);
        addVoteNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DescriptionWithVote descriptionWithVoteCurrent = null;
                VoteDescription voteDescriptionNegative = new VoteDescription(false,homeConnectedActivity.getUserCurrent().id,descriptionWithVote.description.idDescription);
                new AddVoteDescriptionAsync().execute(voteDescriptionNegative);

                try{
                    descriptionWithVoteCurrent = new GetDescriptionByIdAsync().execute(descriptionWithVote.description.idDescription).get();
                }catch(Exception e){
                    e.printStackTrace();
                }

                TextView textAverage = viewDescription.findViewById(R.id.rate);

                if(descriptionWithVoteCurrent != null && descriptionWithVoteCurrent.average != -1){
                    textAverage.setText(Integer.toString(descriptionWithVoteCurrent.average) +"%");
                }
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
            if(resultCode != HttpURLConnection.HTTP_OK){
                Toast.makeText(getContext(), R.string.message_vote_not_recorded, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Classe GetDescriptionByIdAsync
    private class GetDescriptionByIdAsync extends AsyncTask<Long,Void,DescriptionWithVote>
    {
        @Override
        protected DescriptionWithVote doInBackground(Long... params) {
            DescriptionWithVote descriptionWithVote;
            DescriptionDAO descriptionDAO = new DescriptionDAO();

            try{
                descriptionWithVote = descriptionDAO.getDescriptionById(homeConnectedActivity.getToken(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                descriptionWithVote = null;
            }
            return descriptionWithVote;
        }

        @Override
        protected void onPostExecute(DescriptionWithVote descriptionWithVote)
        {
            if(descriptionWithVote == null){
                Toast.makeText(getContext(), R.string.description_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }
}