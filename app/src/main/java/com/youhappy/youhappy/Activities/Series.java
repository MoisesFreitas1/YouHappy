package com.youhappy.youhappy.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youhappy.youhappy.Models.Episodios;
import com.youhappy.youhappy.R;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Moises on 17/04/17.
 */

public class Series extends AppCompatActivity {

    private String episode;
    private Episodios episodio;
    private ImageView dragonball, dragonballz, dragonballsuper, dragonballkai, dragonballgt, onepunchman;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference ultimoEpisodio = databaseReference.child("Ultimo");
    private DatabaseReference ShareLink = databaseReference.child("Share");
    private String Link;
    private ArrayList<Episodios> episodios;
    private TextView ultimoepisodiotv, ultimaserie;
    private CardView ultimoepisodiocv;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_series);

        ultimoepisodiotv = (TextView) findViewById(R.id.ultimoepisodiotv);
        ultimaserie = (TextView) findViewById(R.id.ultimaserie);

        YoYo.with(Techniques.FadeIn).duration(1800).playOn(findViewById(R.id.ultimoepisodiocv));
        YoYo.with(Techniques.FlipInX).duration(2000).playOn(findViewById(R.id.dragonball));
        YoYo.with(Techniques.FlipInX).duration(2200).playOn(findViewById(R.id.dragonballz));
        YoYo.with(Techniques.FlipInX).duration(2400).playOn(findViewById(R.id.dragonballsuper));
        YoYo.with(Techniques.FlipInX).duration(2600).playOn(findViewById(R.id.dragonballkai));
        YoYo.with(Techniques.FlipInX).duration(2800).playOn(findViewById(R.id.dragonballgt));

        episodios = new ArrayList<>();
        progressDialog = ProgressDialog.show(this, "", "Carregando...", true);

        ultimoEpisodio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    episodio = dados.getValue(Episodios.class);
                    episodios.add(episodio);
                    new DownloadImageTask((ImageView) findViewById(R.id.ultimoepisodioiv)).execute(episodios.get(0).getEfigure());
                    ultimoepisodiotv.setText(episodios.get(0).getEserie() + " - " + episodios.get(0).getEid());
                    ultimaserie.setText(episodios.get(0).getEtitle());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ShareLink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Link = dados.getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ultimoepisodiocv = (CardView) findViewById(R.id.ultimoepisodiocv);
        ultimoepisodiocv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Compartilhar app
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, "Gostaria de compartilhar esse app?");
                share.putExtra(Intent.EXTRA_TEXT, Link);
                share.setType("text/plain");
                startActivity(share);

                //Assistir Episódio
                Intent intent = new Intent(view.getContext(), VideoActivity.class);
                intent.putExtra("urlvideo", episodios.get(0).getElink());
                intent.putExtra("esinopse", episodios.get(0).getEsinopse());
                intent.putExtra("etitle", episodios.get(0).getEtitle());
                intent.putExtra("serie", episodios.get(0).getEserie());
                intent.putExtra("eid", episodios.get(0).getEid());
                startActivity(intent);
            }
        });


        dragonball = (ImageView) findViewById(R.id.dragonball);
        dragonball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("dragonball", episode);
                startActivity(i);
            }
        });

        dragonballz = (ImageView) findViewById(R.id.dragonballz);
        dragonballz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("dragonballz", episode);
                startActivity(i);
            }
        });

        dragonballsuper = (ImageView) findViewById(R.id.dragonballsuper);
        dragonballsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("dragonballsuper", episode);
                startActivity(i);
            }
        });

        dragonballkai = (ImageView) findViewById(R.id.dragonballkai);
        dragonballkai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("dragonballkai", episode);
                startActivity(i);
            }
        });

        dragonballgt = (ImageView) findViewById(R.id.dragonballgt);
        dragonballgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("dragonballgt", episode);
                startActivity(i);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}