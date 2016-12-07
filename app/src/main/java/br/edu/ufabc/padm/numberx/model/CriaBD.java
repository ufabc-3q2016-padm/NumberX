package br.edu.ufabc.padm.numberx.model;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.edu.ufabc.padm.numberx.R;


public class CriaBD extends SQLiteOpenHelper {

    private static final String NOME_DB = "app.db";
    private static final int VERSAO = 1;

    public CriaBD(Context context) {
        super(context, NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTableStr = Resources.getSystem().getString(R.string.create_user_table_str);
        String createProgressTableStr = Resources.getSystem().getString(R.string.create_progress_table_str);
        String createNotebookDraftsTableStr = Resources.getSystem().getString(R.string.create_notebook_table_str);

        sqLiteDatabase.execSQL(createUserTableStr);
        sqLiteDatabase.execSQL(createProgressTableStr);
        sqLiteDatabase.execSQL(createNotebookDraftsTableStr);
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
