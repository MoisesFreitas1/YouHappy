package com.youhappy.youhappy.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.youhappy.youhappy.Adapter.EpisodeAdapter;
import com.youhappy.youhappy.Configuration.ConfiguracaoFirebase;
import com.youhappy.youhappy.Models.Episodios;
import com.youhappy.youhappy.R;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    private ListView episodeList;
    private ArrayList<Episodios> episodios;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ImageView nomedaserie;
    private String serie;
    ProgressDialog progressDialog;
    EditText editText;
    private ArrayList<Episodios> episodiosSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        nomedaserie = (ImageView) findViewById(R.id.nomedaserie);

        YoYo.with(Techniques.ZoomInUp).duration(2000).playOn(findViewById(R.id.episodeList));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.nomedaserie));

        episodios = new ArrayList<>();
        episodeList = (ListView) findViewById(R.id.episodeList);
        final EpisodeAdapter adapter = new EpisodeAdapter(episodios, ListActivity.this);
        episodeList.setAdapter(adapter);

        if (getIntent().hasExtra("dragonball")) {
            firebase = ConfiguracaoFirebase.getFirebase().child("Dragon Ball");
            nomedaserie.setImageResource(R.drawable.dragonball);
            serie = "Dragon Ball";
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Carregando...", true);
        }
        if (getIntent().hasExtra("dragonballz")) {
            firebase = ConfiguracaoFirebase.getFirebase().child("Dragon Ball Z");
            nomedaserie.setImageResource(R.drawable.dragonballz);
            serie = "Dragon Ball Z";
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Carregando...", true);
        }
        if (getIntent().hasExtra("dragonballsuper")) {
            firebase = ConfiguracaoFirebase.getFirebase().child("Dragon Ball Super");
            nomedaserie.setImageResource(R.drawable.dragonballsuper);
            serie = "Dragon Ball Super";
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Carregando...", true);
        }
        if (getIntent().hasExtra("dragonballkai")) {
            firebase = ConfiguracaoFirebase.getFirebase().child("Dragon Ball Kai");
            nomedaserie.setImageResource(R.drawable.dragonballkai);
            serie = "Dragon Ball Kai";
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Carregando...", true);
        }
        if (getIntent().hasExtra("dragonballgt")) {
            firebase = ConfiguracaoFirebase.getFirebase().child("Dragon Ball GT");
            nomedaserie.setImageResource(R.drawable.dragonballgt);
            serie = "Dragon Ball GT";
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Carregando...", true);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Episodios episodio = dados.getValue(Episodios.class);
                    episodios.add(episodio);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListener);


//        episodiosSearch = new ArrayList<>();
//        final EpisodeAdapter adapterSearch = new EpisodeAdapter(episodiosSearch, this);
//        editText = findViewById(R.id.txt123);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() != 0){
//                    episodiosSearch.clear();
//                    for (int i = 0; i < episodios.size(); i++){
//                        if((episodios.get(i).getEsinopse().contains(s.toString())) || (episodios.get(i).getEtitle().contains(s.toString())) || (episodios.get(i).getEid().contains(s.toString()))){
//                            episodiosSearch.add(episodios.get(i));
//                            adapterSearch.notifyDataSetChanged();
//                        }
//                    }
//                    episodeList.setAdapter(adapterSearch);
//                } else{
//                    episodeList.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        episodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, SelectActivity.class);
                intent.putExtra("efigure",episodios.get(position).getEfigure());
                intent.putExtra("eid",episodios.get(position).getEid());
                intent.putExtra("elink",episodios.get(position).getElink());
                intent.putExtra("esinopse",episodios.get(position).getEsinopse());
                intent.putExtra("etitle",episodios.get(position).getEtitle());
                intent.putExtra("serie", serie);
                startActivity(intent);
            }
        });
    }
}