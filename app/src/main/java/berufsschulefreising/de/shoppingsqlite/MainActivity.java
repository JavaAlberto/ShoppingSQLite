package berufsschulefreising.de.shoppingsqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Shopping_DB_Connection dbc;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Zum Testen der Shopping-Klasse
        Shopping shop1 = new Shopping("Bananen", 100, 10);
        Log.d(LOG_TAG, "Inhalt: " + shop1.toString());

        dbc = new Shopping_DB_Connection(this);
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dbc.open();
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


}
