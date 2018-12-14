package com.youhappy.youhappy.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youhappy.youhappy.R;

import java.io.InputStream;


public class SelectActivity extends AppCompatActivity {

    private String serie, sinopse, title;
    private TextView selectserie, selectepisodio;
    private Button assistirButton;
    private String UrlVideo, Eid, Efigure;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference AdmobIDUnit = databaseReference.child("Admob");
    private String IDUnit;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        AdmobIDUnit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    IDUnit = dados.getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        MobileAds.initialize(SelectActivity.this, IDUnit);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        selectserie = (TextView) findViewById(R.id.selectserie);
        selectepisodio = (TextView) findViewById(R.id.selectepisodio);
        assistirButton = (Button) findViewById(R.id.assistirButton);

        if (getIntent().hasExtra("etitle")) {
            title = getIntent().getStringExtra("etitle");
        }
        if (getIntent().hasExtra("efigure")) {
            Efigure = getIntent().getStringExtra("efigure");
        }
        if (getIntent().hasExtra("eid")) {
            Eid = getIntent().getStringExtra("eid");
        }
        if (getIntent().hasExtra("serie")) {
            serie = getIntent().getStringExtra("serie");
        }
        if (getIntent().hasExtra("elink")) {
            UrlVideo = getIntent().getStringExtra("elink");
        }
        if (getIntent().hasExtra("esinopse")) {
            sinopse = getIntent().getStringExtra("esinopse");
        }

        selectserie.setText(title);
        selectepisodio.setText(serie + " - " + Eid);
        new DownloadImageTask((ImageView) findViewById(R.id.selectepisodioiv)).execute(Efigure);
        assistirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder mensagem = new AlertDialog.Builder(SelectActivity.this);
//                mensagem.setTitle("");
//                mensagem.setMessage("Clique na Publicidade.");
//                mensagem.setNeutralButton("OK", null);
//                mensagem.show();
                Intent i = new Intent(SelectActivity.this, VideoActivity.class);
                i.putExtra("urlvideo", UrlVideo);
                i.putExtra("esinopse", sinopse);
                i.putExtra("etitle", title);
                i.putExtra("serie", serie);
                i.putExtra("eid", Eid);
                startActivity(i);
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication () {
                Intent i = new Intent(SelectActivity.this, VideoActivity.class);
                i.putExtra("urlvideo", UrlVideo);
                i.putExtra("esinopse", sinopse);
                i.putExtra("etitle", title);
                i.putExtra("serie", serie);
                i.putExtra("eid", Eid);
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
