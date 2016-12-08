package br.edu.ufabc.padm.numberx;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by lusca on 06/12/16.
 */

import br.edu.ufabc.padm.numberx.model.Enunciado;
import br.edu.ufabc.padm.numberx.model.EnunciadoDAO;

public class EnunciadoFragment extends Fragment {

    private static final String TITULO = "titulo";
    private static final String DESCRICAO = "descricao";


    public static EnunciadoFragment newInstance(int pos) {
        EnunciadoFragment fragment = new EnunciadoFragment();
        EnunciadoDAO dao = EnunciadoDAO.newInstance();
        Enunciado enunciado = dao.getItemAt(pos);

        Bundle args = new Bundle();
        args.putString(TITULO, enunciado.getTitulo());
        args.putString(DESCRICAO, enunciado.getDescricao());
        fragment.setArguments(args);

        return fragment;
    }

    //public EnunciadoFragment() {

    //}

    private TextView titulo;
    private TextView descricao;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.enunciado_fragment, container, false);
        TextView textView1 = (TextView) rootView.findViewById(R.id.enunciado_titulo);
        textView1.setText(getString(R.string.format,getArguments().getString(TITULO)));

        TextView textView2 = (TextView) rootView.findViewById(R.id.enunciado_descricao);
        textView2.setText(getString(R.string.format,getArguments().getString(DESCRICAO)));

        return rootView;
    }


}

