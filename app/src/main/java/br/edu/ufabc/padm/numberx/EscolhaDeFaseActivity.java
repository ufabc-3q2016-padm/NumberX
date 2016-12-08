package br.edu.ufabc.padm.numberx;

import android.content.Intent;
<<<<<<< HEAD
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
=======
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
>>>>>>> bf4f0eb034a7877194550cb11bea81948d013fae
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class EscolhaDeFaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_de_fase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateFases();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
    }

            @Override
            public void onBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
            }

    private void populateFases() {
        final ListView listView = (ListView) findViewById(R.id.list_fases);


        listView.setAdapter(new FaseAdapter(this));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(parent.getContext(), Exercicio.class);
                intent.putExtra("fasePosition", position);

                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.escolhadefase_activity_menu, menu);
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
        } else if (id == R.id.action_return) {
            startActivity((new Intent(this, MainActivity.class)));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

            @SuppressWarnings("StatementWithEmptyBody")
            //@Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_progress) {
                    startActivity((new Intent(this, EscolhaDeFaseActivity.class)));

                    return true;

                } else if (id == R.id.nav_notebook) {
                    startActivity((new Intent(this, CadernoActivity.class)));

                    return true;

                } else if (id == R.id.nav_calculator) {
                    Intent intent = new Intent(this, Editor.class);
                    intent.putExtra("editMode", true);
                    startActivity(intent);

                } else if (id == R.id.nav_adjust) {

                }else if(id==R.id.nav_home){
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
}


