package hr.math.android.kuharica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    ArrayList<String> ingredientList;
    ArrayAdapter<String> adapterIngredients;
    ArrayList<String> stepList;
    ArrayAdapter<String> adapterSteps;
    ListView ingredientView;
    ListView stepView;
    String recipeName;
    String tempIngredient;
    String tempStep;
    String recipeNotesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        ingredientList = new ArrayList<String>();
        adapterIngredients = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientList);
        ingredientView = (ListView)findViewById(R.id.ingredientsListView);
        ingredientView.setAdapter(adapterIngredients);

        stepList = new ArrayList<String>();
        adapterSteps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stepList);
        stepView = (ListView)findViewById(R.id.stepsListView);
        stepView.setAdapter(adapterSteps);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        EditText editTextName = (EditText)findViewById(R.id.EditText_recipeName);
        recipeName = editTextName.getText().toString();
        savedInstanceState.putString("recipeName", recipeName);

        EditText editTextIngredient = (EditText)findViewById(R.id.EditText_ingredient);
        tempIngredient = editTextIngredient.getText().toString();
        savedInstanceState.putString("tempIngredient", tempIngredient);

        EditText editTextDetails = (EditText)findViewById(R.id.EditText_step);
        tempStep = editTextDetails.getText().toString();
        savedInstanceState.putString("tempStep", tempStep);

        EditText editTextNotes = (EditText)findViewById(R.id.recipeNotes);
        recipeNotesText = editTextNotes.getText().toString();
        savedInstanceState.putString("recipeNotesText", recipeNotesText);

        savedInstanceState.putStringArrayList("ingredientList", ingredientList);
        savedInstanceState.putStringArrayList("stepList", stepList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        recipeName = savedInstanceState.getString("recipeName");
        tempIngredient = savedInstanceState.getString("tempIngredient");
        ingredientList = savedInstanceState.getStringArrayList("ingredientList");
        tempStep = savedInstanceState.getString("tempStep");
        recipeNotesText = savedInstanceState.getString("recipeNotesText");

        EditText editTextName = (EditText)findViewById(R.id.EditText_recipeName);
        if(!recipeName.isEmpty()) editTextName.setText(recipeName);
        else {
            editTextName.setText("");
            editTextName.setHint(R.string.addRecipe_recipeNameHint);
        }
        EditText editTextIngredient = (EditText)findViewById(R.id.EditText_ingredient);
        if(!tempIngredient.isEmpty()) editTextIngredient.setText(tempIngredient);
        else {
            editTextIngredient.setText("");
            editTextIngredient.setHint(R.string.addRecipe_ingredientHint);
        }
        EditText editTextDetails = (EditText)findViewById(R.id.EditText_step);
        if(!tempStep.isEmpty()) editTextDetails.setText(tempStep);
        else {
            editTextDetails.setText("");
            editTextDetails.setHint(R.string.addRecipe_recipeDetailsHint);
        }
        EditText editTextNotes = (EditText)findViewById(R.id.recipeNotes);
        if(!recipeNotesText.isEmpty()) editTextNotes.setText(recipeNotesText);
        else {
            editTextNotes.setText("");
            editTextNotes.setHint(R.string.addRecipe_recipeNotesHint);
        }

        adapterIngredients = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientList);
        ingredientView = (ListView)findViewById(R.id.ingredientsListView);
        ingredientView.setAdapter(adapterIngredients);
        setListViewHeightBasedOnChildren(ingredientView);

        adapterSteps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stepList);
        stepView = (ListView)findViewById(R.id.stepsListView);
        stepView.setAdapter(adapterSteps);
        setListViewHeightBasedOnChildren(stepView);
    }

    public void addBtnClick(View view){
        EditText editText = (EditText)findViewById(R.id.EditText_ingredient);
        String ingredient = editText.getText().toString();

        if(ingredient.isEmpty()) return;

        ingredientList.add(ingredient);
        editText.setText("");
        editText.setHint(R.string.addRecipe_ingredientHint);
        adapterIngredients.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(ingredientView);
    }

    public void stepBtnClick(View view){
        EditText editText = (EditText)findViewById(R.id.EditText_step);
        String step = editText.getText().toString();

        if(step.isEmpty()) return;

        stepList.add(step);
        editText.setText("");
        editText.setHint(R.string.addRecipe_recipeStepHint);
        adapterSteps.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(stepView);
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

    public void saveBtnClick(View view){
        EditText editTextName = (EditText)findViewById(R.id.EditText_recipeName);
        recipeName = editTextName.getText().toString();

        EditText editTextNotes = (EditText)findViewById(R.id.recipeNotes);
        recipeNotesText = editTextNotes.getText().toString();

        if(recipeName.isEmpty() || stepList.isEmpty() || recipeNotesText.isEmpty() || ingredientList.isEmpty()){
            Toast.makeText(this, "Form is not filled properply.", Toast.LENGTH_LONG).show();
            return;
        }

        Recept recept = new Recept();
        recept.setImeRecepta(recipeName);
        recept.setNotes(recipeNotesText);
        recept.setUpute(stepList);
        recept.setSastojci(ingredientList);
        recept.setPhotoRecept(null);

        DBRAdapter db = new DBRAdapter(this);
        db.open();
        recept.setId(db.insertRecept(recept));
        Log.w("AddRecipeActivity", db.getReceptZaId(recept.getId()).toString());
        db.close();
    }
}
