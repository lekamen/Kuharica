package hr.math.android.kuharica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept);

        //String name = savedInstanceState.getString("recipeName");
        String name = "test_ime";
        //long ID = savedInstanceState.getLong("recipeID");
        long ID = 12345;
        TextView name_text = (TextView) findViewById(R.id.recipeName);
        name_text.setText(name);
        DBRAdapter db = new DBRAdapter(this);
        db.open();
        Recept test_recept = new Recept();
        test_recept.setId(ID);
        test_recept.setImeRecepta(name);
        List<String> temp_list = new ArrayList<String>();
        temp_list.add("prvi");
        temp_list.add("drugi");
        temp_list.add("treci");
        test_recept.setSastojci(temp_list);
        test_recept.setNotes("blabla blabla bla");
        test_recept.setUpute(Arrays.asList("korak 1","korak 2", "korak 3"));
        test_recept.setPhotoRecept("");

        db.insertRecept(test_recept);

        Recept current = db.getReceptZaId(ID);
        List<String> sastojci = current.getSastojci();
        List<String> upute = current.getUpute();
        ListView lista = (ListView) findViewById(R.id.sastojci_list);
        /*
        ArrayList<Sastojak> temp = new ArrayList<>();
        for (String s: temp_list) {
            Sastojak sas = new Sastojak();
            sas.setSastojak(s);
            sas.setSelected(false);
            temp.add(sas);
        }
        SastojciAdapter adapter_sastojci = new SastojciAdapter(this, temp);
        lista.setAdapter(adapter_sastojci);
        setListViewHeightBasedOnChildren(lista);
        */

    private int stari = 1;
    private long ID=0;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));


        ID = getIntent().getLongExtra("ID", 0);




        //long ID = 12345;
        DBRAdapter db = new DBRAdapter(this);
        db.open();
        Recept test_recept = new Recept();
        //test_recept.setId(ID);
        //test_recept.setImeRecepta(name);
        /*
        List<String> temp_list = new ArrayList<String>();
        temp_list.add("3 jaja");
        temp_list.add("100g šećera");
        temp_list.add("otprilike 4 grama soli");
        test_recept.setSastojci(temp_list);
        test_recept.setNotes("blabla blabla bla");
        test_recept.setUpute(Arrays.asList("korak 1 min","peci 3 minute", "korak 3: mijesi 2min"));
        test_recept.setPhotoRecept("");

        ID = db.insertRecept(test_recept);
        */


        Recept current = db.getReceptZaId(ID);

        final List<String> sastojci = current.getSastojci();
        List<String> upute = current.getUpute();
        String napomena = current.getNotes();
        String name = current.getImeRecepta();
        String photo = current.getPhotoRecept();

        if(photo!=null)
            try {
                File f=new File(photo);

                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                Drawable d = new BitmapDrawable(getResources(), b);
                toolbar.setBackground(d);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        else
        {
            toolbar.setBackground(getResources().getDrawable(R.drawable.cake));
        }

        collapsingToolbar.setTitle(name);

        final ListView lv_sastojci = (ListView) findViewById(R.id.sastojci_list);
        ListView lv_upute = (ListView) findViewById(R.id.priprema_list);
        TextView tv_napomene = (TextView) findViewById(R.id.napomene);

        final ArrayList<Sastojak> lista_sastojaka = new ArrayList<>();
        for (String s: sastojci) {
            Sastojak sas = new Sastojak();
            sas.setSastojak(preracunaj(s,1,2));
            sas.setSelected(false);
            lista_sastojaka.add(sas);
        }
        ArrayList<Uputa> lista_uputa = new ArrayList<>();
        for(String u: upute) {
            Uputa upu = new Uputa();
            upu.setUputa(u);
            upu.setSelected(false);
            lista_uputa.add(upu);
        }
        final SastojciAdapter adapter_sastojci = new SastojciAdapter(this, lista_sastojaka);
        lv_sastojci.setAdapter(adapter_sastojci);
        setListViewHeightBasedOnChildren(lv_sastojci);
        UputaAdapter adapter_uputa = new UputaAdapter(this, lista_uputa);
        lv_upute.setAdapter(adapter_uputa);
        setListViewHeightBasedOnChildren(lv_upute);

        tv_napomene.setText(napomena);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.broj_osoba, R.layout.support_simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Sastojak> temp = new ArrayList<>();
                for (Sastojak s:lista_sastojaka)
                {
                    Sastojak sas = new Sastojak();
                    sas.setSastojak(preracunaj(s.getSastojak(),stari,i+1));
                    sas.setSelected(false);
                    temp.add(sas);
                }

                lista_sastojaka.clear();
                for(Sastojak s:temp)
                    lista_sastojaka.add(s);
                stari = i+1;
                adapter_sastojci.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recept_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.uredi_recept:

                Intent intent = new Intent(ctx, AddRecipeActivity.class);
                intent.putExtra("ID", ID);
                ctx.startActivity(intent);


                return true;
            case R.id.obrisi_recept:
                DBRAdapter db = new DBRAdapter(this);
                db.open();
                db.deleteRecept(ID);
                db.close();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private String preracunaj(String ulaz, int stari, int novi)
    {
        String b = ulaz.replaceAll("[^0-9]","");
        String izlaz = "";
        int pocetak=0, kraj=0;
        Double temp =0.0;
        try {
            temp = Double.parseDouble(b.replace(",","."));
        }
        catch (NumberFormatException e)
        {

        }
        if(temp!=0.0) {
            kraj = ulaz.indexOf(b);
            izlaz += ulaz.substring(pocetak, kraj);
            temp= temp / stari * novi;
            if(temp%1==0)
                izlaz+=temp.intValue();
            else
                izlaz += temp;
            pocetak = kraj + b.length();
            }
        izlaz+=ulaz.substring(pocetak,ulaz.length());
        return izlaz;
    }
}
