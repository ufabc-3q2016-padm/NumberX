package br.edu.ufabc.padm.numberx;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by lusca on 07/12/16.
 */

public class EditorFragment extends Fragment {

    public static EditorFragment newInstance() {
        EditorFragment fragment = new EditorFragment();
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_editor_fragment, container, false);

        WebView board = (WebView) rootView.findViewById(R.id.Board);
        board.getSettings().setJavaScriptEnabled(true);
        board.loadUrl(getString(R.string.board_url));
        board.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //showNumberPicker();
                return true;
            }
        });
        board.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                if (dragEvent.getAction() == DragEvent.ACTION_DROP)
                    //Toast.makeText(Editor.this, "TODO: Drag and drop symbols",Toast.LENGTH_LONG).show();

                    return true;
                return  true;
            }
        });
        return rootView;
    }



}
