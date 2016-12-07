package br.edu.ufabc.padm.numberx;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {
    private String title;
    private Toolbar bottomToolbar;
    private FloatingActionButton fab;
    private AlertDialog.Builder editBuilder;
    private View dialogView;
    private AlertDialog dialogEdit;
    private Activity activity;
    private WebView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        title = getString(R.string.default_title);
        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode(true);
            }
        });

        boolean edit = getIntent().getExtras().getBoolean("editMode");

        editMode(edit);

        setupTitleDialog();
        
        setupBoard();
    }

    private void showNumberPicker() {
        final BottomSheetDialog numberPicker = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
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

    private void showSymbolPicker(int page) {
        BottomSheetDialog symbolPicker = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View pickerView = null;
        switch (page) {
            case 0: {
                pickerView = inflater.inflate(R.layout.alphabet_symbol_picker, null);
                break;
            }case 1: {
                pickerView = inflater.inflate(R.layout.greek_alphabet_symbol_picker, null);
                break;
            }case 2: {
                pickerView = inflater.inflate(R.layout.non_alpabetic_symbol_picker, null);
                break;
            }
        }
        symbolPicker.setContentView(pickerView);
        symbolPicker.show();
    }

    /**
     * Create and configure the changing title dialog
     */
    private  void setupTitleDialog() {
        editBuilder = new AlertDialog.Builder(this);
        editBuilder.setTitle(R.string.edit_title)
                .setPositiveButton(R.string.save_option_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        title = ((EditText) dialogEdit.findViewById(R.id.edit_text_title)).getText().toString();
                        editMode(false);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_option_dialog,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(EditorActivity.this, R.string.cancel_toast_dialog,Toast.LENGTH_LONG).show();
                        dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ViewGroup vGroup = (ViewGroup) dialogView.getParent();
                        vGroup.removeView(dialogView);
                    }
                });

        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.edit_dialog, null);
        editBuilder.setView(dialogView);
    }

    /**
     * Handle the AppBar and BottomBar behavior if viewing or editing a math problem.
     * @param edit given by intent, and must be passed as a extra when the activity is started, true for the creating a new problem,
     * false for opening notebook files.
     */
    private void editMode(boolean edit) {
        bottomToolbar  = (Toolbar) findViewById(R.id.bottomToolbar);
        if (edit) {
            //Calculator mode
            showEditActionBar(true);
            //Setup listeners for the Bottom Bar:
            ((Button) findViewById(R.id.numeric_keyboard)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { showNumberPicker(); }
            });
            ((Button) findViewById(R.id.x_latin_picker_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { showSymbolPicker(0); }
            });
            ((Button) findViewById(R.id.alpha_greek_picker_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { showSymbolPicker(1); }
            });
            ((Button) findViewById(R.id.equality_non_alphabetic_picker_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { showSymbolPicker(2); }
            });
            ((Button) findViewById(R.id.minus_one_non_alphabetic_picker_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { showSymbolPicker(2); }
            });

            setupDrag();

            bottomToolbar.setVisibility(View.VISIBLE);
            fab.hide();
        } else {
            //Viewer mode
            showEditActionBar(false);
            bottomToolbar.setVisibility(View.GONE);
            fab.show();
        }
    }

    private  void setupDrag() {
        ((Button) findViewById(R.id.x_latin_picker_button)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        ((Button) findViewById(R.id.alpha_greek_picker_button)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        ((Button) findViewById(R.id.equality_non_alphabetic_picker_button)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        ((Button) findViewById(R.id.minus_one_non_alphabetic_picker_button)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });

    }

    /**
     * @param show specifies if the ActionBar for editing mode must be shown, if false showing the view AppBar.
     * Must be passed as a extra for the intent when the activity is started, true for the calculator,
     * false for notebook files.
     */
    private void showEditActionBar(boolean show) {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if (show) {
            View v = inflator.inflate(R.layout.app_bar_calculator, null);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(v);

            Button save = (Button) v.findViewById(R.id.action_save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editMode(false);
                    //save
                }
            });
        } else {
            View v = inflator.inflate(R.layout.app_bar_calculator_viewer, null);
            TextView problemTitle = (TextView) v.findViewById(R.id.problem_title);
            problemTitle.setText(title);
            problemTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = ((EditText) dialogView.findViewById(R.id.edit_text_title));
                    editText.setText(title);
                    editText.selectAll();
                    //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

                    dialogEdit = editBuilder.create();
                    dialogEdit.show();
                }
            });
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(v);
        }
    }

    /**
     * Setup the webview to display the math Board, using html/css/js
     */
    private void setupBoard() {
        board = (WebView) findViewById(R.id.Board);
        board.getSettings().setJavaScriptEnabled(true);
        board.loadUrl(getString(R.string.board_url));
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
                if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
                    //board.loadUrl("javascript:constant.setup()");
                    Toast.makeText(EditorActivity.this, "TODO: Drag and drop symbols", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
