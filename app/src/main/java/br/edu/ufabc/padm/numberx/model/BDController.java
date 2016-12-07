package br.edu.ufabc.padm.numberx.model;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.ufabc.padm.numberx.R;

public class BDController {

    private SQLiteDatabase bd;
    private CriaBD criaBD;

    public BDController(Context c) {
        criaBD = new CriaBD(c);
    }

    public Cursor carregaDados(String mod){
        Cursor cursor = null;
        String qry = null;
        if (mod == "Fase"){
            qry = Resources.getSystem().getString(R.string.carrega_dados_fase);
        } else if (mod == "Modulo"){
            qry = Resources.getSystem().getString(R.string.carrega_dados_modulo);
        } else if (mod == "Rascunho") {
            qry = Resources.getSystem().getString(R.string.carrega_dados_rascunho);
        }

        if (qry != null) {
            cursor = bd.rawQuery(qry, null);
            if (cursor != null)
                cursor.moveToFirst();
            bd.close();
        }
        return cursor;
    }
}
