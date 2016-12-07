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

    /**
        @param mod  Recebe como argumento "Fase", "Modulo" ou "Rascunho",
                    direcionando a query no DB.
     */
    public Cursor carregaDados(String mod){
        Cursor cursor = null;
        String qry = null;
        switch (mod) {
            case "Fase":
                qry = Resources.getSystem().getString(R.string.carrega_dados_fase);
                break;
            case "Modulo":
                qry = Resources.getSystem().getString(R.string.carrega_dados_modulo);
                break;
            case "Rascunho":
                qry = Resources.getSystem().getString(R.string.carrega_dados_rascunho);
                break;
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
