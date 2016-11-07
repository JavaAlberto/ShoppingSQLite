package berufsschulefreising.de.shoppingsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Albrecht on 22.10.2016.
 */



public class ShoppingSQLiteHelper extends SQLiteOpenHelper
{


    private static final String LOG_TAG = ShoppingSQLiteHelper.class.getSimpleName();
    private static final String DATENBANKNAME = "shoppingDB.db";
    private static int DATENBANK_VERSION = 1;


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_QUANTITY = "quantity";

    public static final String TABLE_SHOPPING_LIST = "shopping_list";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_SHOPPING_LIST +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              COLUMN_PRODUCT + " TEXT NOT NULL, " +
                              COLUMN_QUANTITY + " INTEGER NOT NULL);";


    public ShoppingSQLiteHelper(Context context)
    {

        super(context, "DATENBANKNAME", null, DATENBANK_VERSION);
        Log.d(LOG_TAG, "SQLiteOpenHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

        @Override
        public void onCreate(SQLiteDatabase db)
        // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
        {
            try {
                Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
                db.execSQL(SQL_CREATE);
            }
            catch (Exception ex) {
                Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
            }
        }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
