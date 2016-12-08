package br.edu.ufabc.padm.numberx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.edu.ufabc.padm.numberx.model.Fase;
import br.edu.ufabc.padm.numberx.model.FaseDAO;

/**
 * Created by lusca on 11/11/16.
 */

public class FaseAdapter extends BaseAdapter {
    //private DAO.Fase dao;
    private FaseDAO dao;
    private Context context;


    public FaseAdapter(Context c) {
        this.context = c;
        //this.dao = DAO.Fase.newInstance();
        this.dao = FaseDAO.newInstance();
    }

    @Override
    public int getCount() {
        return dao.size();
    }

    @Override
    public Object getItem(int position) {
        return dao.getItemAt(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fase fase = null;
        TextView titulo = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.escolhadefase_listview_item, null);
        }
        fase = dao.getItemAt(position);
        titulo = (TextView) convertView.findViewById(R.id.listview_item_title);

        titulo.setText(fase.getTitulo());



        return convertView;

    }


}
