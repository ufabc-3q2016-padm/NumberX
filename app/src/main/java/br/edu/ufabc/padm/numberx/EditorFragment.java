package br.edu.ufabc.padm.numberx;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ufabc.padm.numberx.model.DAO;

/**
 * Created by lusca on 07/12/16.
 */

public class EditorFragment extends Fragment {

    private static final String PROBLEM = "problem";
    WebView board;

    public static EditorFragment newInstance(String problem) {
        EditorFragment fragment = new EditorFragment();

        Bundle args = new Bundle();
        args.putString(PROBLEM, problem);
        fragment.setArguments(args);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_editor_fragment, container, false);
        BoardInterface boardInterface = new BoardInterface(getActivity());

        board = (WebView) rootView.findViewById(R.id.Board);
        board.getSettings().setJavaScriptEnabled(true);
        board.addJavascriptInterface(boardInterface, "android");
        board.loadUrl(getArguments().getString(PROBLEM));
        board.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showNumberPicker();
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

    private void showNumberPicker() {
        final BottomSheetDialog numberPicker = new BottomSheetDialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View pickerView = inflater.inflate(R.layout.number_picker, null);
        numberPicker.setContentView(pickerView);
        numberPicker.show();
        //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        final EditText number = (EditText) pickerView.findViewById(R.id.number_edit);
        number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String jsInsertCode = String.format(getString(R.string.new_constant_js), number.getText().toString());
                board.loadUrl(jsInsertCode);
                numberPicker.dismiss();
                return false;
            }
        });

    }



}
