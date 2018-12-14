package com.youhappy.youhappy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youhappy.youhappy.Models.Episodios;
import com.youhappy.youhappy.R;

import java.util.List;


/**
 * Created by Moises on 17/04/17.
 */

public class EpisodeAdapter extends BaseAdapter {
    private List<Episodios> episodeList;
    private Context context;
    private LayoutInflater layoutInflater;

    public EpisodeAdapter(List<Episodios> episodeList, Context context) {
        this.episodeList = episodeList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return episodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return episodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View parent, ViewGroup viewGroup) {
        View item = layoutInflater.inflate(R.layout.item_episodio_list, viewGroup, false);

        Episodios episode = episodeList.get(position);

        TextView titulo = (TextView) item.findViewById(R.id.item_layout_titulo);
        titulo.setText(episode.getEtitle());
        TextView numepisodio = (TextView) item.findViewById(R.id.numepisodio);
        numepisodio.setText(episode.getEserie() + " - " + episode.getEid());
        return item;
    }
}
