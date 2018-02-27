package hr.math.android.kuharica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

}