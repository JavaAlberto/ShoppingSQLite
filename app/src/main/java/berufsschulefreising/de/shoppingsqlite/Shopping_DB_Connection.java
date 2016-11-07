package berufsschulefreising.de.shoppingsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Albrecht on 22.10.2016.
 */

public class Shopping_DB_Connection
{
    private static final String LOG_TAG = Shopping_DB_Connection.class.getSimpleName();

    private SQLiteDatabase database;
    private ShoppingSQLiteHelper dbHelper;


    public Shopping_DB_Connection(Context context)
    {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ShoppingSQLiteHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


}
