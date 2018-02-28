package hr.math.android.kuharica;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DBRAdapter db;
    private KategorijaAdapter kategorijaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initializeDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new FAMFragment()).commit();
        }

        navigationView.setCheckedItem(R.id.home);
    }

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new FAMFragment();
            ft.replace(R.id.fragment, fragment).commit();
            return true;
        }
    };

    private void initializeDatabase() {
         db = new DBRAdapter(this);
         db.open();
         if(db.getAllKategorije().size() == 0) {

             Kategorija kategorija = new Kategorija("Slatko", String.valueOf(R.drawable.cake));
             Recept r = new Recept();
             r.setImeRecepta("palačinke");
             r.setPhotoRecept(String.valueOf(R.drawable.pancakes));
             r.setNotes(null);
             r.setSastojci(Arrays.asList("300g brašna", "3 kašike šećera", "3 jaja"));
             r.setUpute(Arrays.asList("umiješati brašno sa šećerom", "zagrijati tavu 3 minute", "peći palačinke"));

             kategorija.setRecepti(Arrays.asList(r));
             r.setId(db.insertRecept(r));
             kategorija.setId(db.insertKategorija(kategorija));
             db.insertReceptUKategoriju(kategorija, r);

             Kategorija kat = new Kategorija("slano", null);
             Recept r1 = new Recept();
             r1.setImeRecepta("kifla");
             r1.setPhotoRecept(null);
             r1.setNotes(null);
             r1.setSastojci(Arrays.asList("300g brašna", "3 jaja"));
             r1.setUpute(Arrays.asList("peći u pećnici 15min"));
             kat.setRecepti(Arrays.asList(r1));
             r1.setId(db.insertRecept(r1));
             kat.setId(db.insertKategorija(kat));
             db.insertReceptUKategoriju(kat, r1);
        }

        kategorijaAdapter = new KategorijaAdapter(db.getAllKategorije(), this, recyclerView);
        recyclerView.setAdapter(kategorijaAdapter);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.search:
                openSearchActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void testBtnClick(View view){
        Intent intent = new Intent(this,AddRecipeActivity.class);
        startActivity(intent);
    }

}
