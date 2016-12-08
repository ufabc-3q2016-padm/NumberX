package br.edu.ufabc.padm.numberx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.edu.ufabc.padm.numberx.model.Rascunho;
import br.edu.ufabc.padm.numberx.model.DAO;

public class RascunhoAdapter extends BaseAdapter {
    private DAO.Rascunho dao;
    private Context context;


    public RascunhoAdapter(Context c) {
        this.context = c;
        this.dao = DAO.Rascunho.newInstance();
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
            Rascunho rascunho = null;
            TextView titulo = null;
            TextView descricao = null;
            TextView postDate = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.caderno_listview_item, null);
            }
            rascunho = dao.getItemAt(position);
            titulo = (TextView) convertView.findViewById(R.id.listview_item_title);
            descricao = (TextView) convertView.findViewById(R.id.listview_item_description);
            postDate = (TextView) convertView.findViewById(R.id.listview_item_post_date);
            titulo.setText(rascunho.getTitulo());
            descricao.setText(rascunho.getDescricao());
            postDate.setText(rascunho.getPostDate());

            return convertView;

        }


}


