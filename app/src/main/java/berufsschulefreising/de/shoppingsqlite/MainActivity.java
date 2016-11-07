package berufsschulefreising.de.shoppingsqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Shopping_DB_Connection dbc;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Shopping shop1 = new Shopping("Bananen", 100, 10);
        Log.d(LOG_TAG, "Inhalt: " + shop1.toString());

        dbc = new Shopping_DB_Connection(this);
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dbc.open();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dbc.close();

    }


}
