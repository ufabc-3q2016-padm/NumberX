package br.edu.ufabc.padm.numberx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.edu.ufabc.padm.numberx.model.ModuloDAO;
import br.edu.ufabc.padm.numberx.model.Modulo;

public class ModuloAdapter extends BaseAdapter {
    private ModuloDAO dao;
    private Context context;


    public ModuloAdapter(Context c) {
        this.context = c;
        this.dao = ModuloDAO.newInstance();
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
            Modulo modulo = null;
            TextView titulo = null;
            TextView descricao = null;
            TextView progressStatus = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.modulo_listview_item, null);
            }
            modulo = dao.getItemAt(position);
            titulo = (TextView) convertView.findViewById(R.id.listview_item_title);
            descricao = (TextView) convertView.findViewById(R.id.listview_item_description);
            progressStatus = (TextView) convertView.findViewById(R.id.listview_item_progress_status);
            titulo.setText(modulo.getTitulo());
            descricao.setText(modulo.getDescricao());
            progressStatus.setText(modulo.getProgressStatus());

            return convertView;

        }


}


