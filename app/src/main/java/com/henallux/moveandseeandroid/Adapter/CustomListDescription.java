package com.henallux.moveandseeandroid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Alexandre on 18-11-17.
 */

public class CustomListDescription extends ArrayAdapter<DescriptionWithVote> {

    //View viewDescription;

    public CustomListDescription(@NonNull Context context, ArrayList<DescriptionWithVote> listDescription) {
        super(context, R.layout.list_view_descriptions, listDescription);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View view, @NonNull ViewGroup viewGroup){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View viewDescription = layoutInflater.inflate(R.layout.list_view_descriptions, viewGroup, false);

            DescriptionWithVote descriptionWithVote = getItem(position);

            TextView textPseudo = (TextView) viewDescription.findViewById(R.id.title_pseudo_interest_point);
            TextView textExplication = (TextView) viewDescription.findViewById(R.id.description);
            TextView textAverage = (TextView) viewDescription.findViewById(R.id.rate);



            textPseudo.setText(descriptionWithVote.description.idUserNavigation.pseudo);
            textExplication.setText(descriptionWithVote.description.explication);
            textAverage.setText(Integer.toString(descriptionWithVote.moyenne) +"%");

            //Evènement pouce vert
            /*ImageView btn_thumb_up=(ImageView) viewDescription.findViewById(R.id.thumb_up);

            btn_thumb_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewDescription.getContext(), "ok !", Toast.LENGTH_SHORT).show();
                }
            });

            //Evènement pouce rouge
            ImageView btn_thumb_down=(ImageView) viewDescription.findViewById(R.id.thumb_down);

            btn_thumb_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewDescription.getContext(), "ok !", Toast.LENGTH_SHORT).show();
                }
            });*/

            return viewDescription;

    }
}
