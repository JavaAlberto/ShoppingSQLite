package berufsschulefreising.de.shoppingsqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Shopping_DB_Connection dbc;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText editTextQuantity = null;
    private EditText editTextProduct = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbc = new Shopping_DB_Connection(this);
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dbc.open();
        // -----------------------------------------------------------------
        activateAddButton();
        //------------------------------------------------------------------
        initializeContextualActionBar();
        //------------------------------------------------------------------
        Shopping shop = dbc.createShopping("Testprodukt",2);
        Log.d(LOG_TAG, "Es wurde folgender Eintrag in die Datenbank geschrieben: ");
        Log.d(LOG_TAG, "ID: " + shop.getId() + ", Inhalt: " + shop.toString());
        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        // die folgende Methode sorgt für eine Ausgabe der DB-Datensätze mit Hilfe
        // des ArrayAddapters
        showAllListEntries();
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dbc.close();

    }
    private void showAllListEntries () {
        List<Shopping> shoppingList = dbc.getAllShoppingMemos();

        /* dem Arrayadapter wird die Liste der Shopping-Objekte übergeben
        Der ArrayAdapter sorgt dann für die Darstellung
        // die Einträge werden mit Hilfe eines vordefinierten Standard-Layouts
        ausgegeben:
        */
        ArrayAdapter<Shopping> shoppingArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingList);

        ListView shoppingListView = (ListView) findViewById(R.id.listview_shopping);
        shoppingListView.setAdapter(shoppingArrayAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dbc.open();
        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dbc.close();
    }
    private void activateAddButton()
    {
        Button buttonAddProduct = (Button) findViewById(R.id.button_add_product);
         editTextQuantity = (EditText) findViewById(R.id.editText_quantity);
         editTextProduct = (EditText) findViewById(R.id.editText_product);

        buttonAddProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String quantityString = editTextQuantity.getText().toString();
        String product = editTextProduct.getText().toString();

        if(TextUtils.isEmpty(quantityString)) {
            editTextQuantity.setError(getString(R.string.editText_errorMessage));
            return;
        }
        if(TextUtils.isEmpty(product)) {
            editTextProduct.setError(getString(R.string.editText_errorMessage));
            return;
        }

        int quantity = Integer.parseInt(quantityString);
        editTextQuantity.setText("");
        editTextProduct.setText("");

        dbc.createShopping(product, quantity);

        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        showAllListEntries();
    }
    // -----------------------------------------------------------------------------------------------------
    //  ContextualActionBar:
    //  --------------------------------------------------------------------------------------------------
    private void initializeContextualActionBar() {

        final ListView shoppingListView = (ListView) findViewById(R.id.listview_shopping);
        shoppingListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        shoppingListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menue_cab, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.cab_delete:
                        SparseBooleanArray touchedShoppingMemosPositions = shoppingListView.getCheckedItemPositions();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                Shopping shopping = (Shopping) shoppingListView.getItemAtPosition(postitionInListView);
                                Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + shopping.toString());
                                dbc.deleteShopping(shopping);
                            }
                        }
                        showAllListEntries();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }



}
