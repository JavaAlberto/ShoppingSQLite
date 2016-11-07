package berufsschulefreising.de.shoppingsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Albrecht on 22.10.2016.
 */

// DAO = Data Access Object

public class Shopping_DB_Connection
{
    private static final String LOG_TAG = Shopping_DB_Connection.class.getSimpleName();

    private SQLiteDatabase database;
    private ShoppingSQLiteHelper dbHelper;
    private String[] columns = {
            ShoppingSQLiteHelper.COLUMN_ID,
            ShoppingSQLiteHelper.COLUMN_PRODUCT,
            ShoppingSQLiteHelper.COLUMN_QUANTITY
                            };


    public Shopping_DB_Connection(Context context)
    {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ShoppingSQLiteHelper(context);
    }

    public void open()
    {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close()
    {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Shopping createShopping(String product, int quantity)
    {
        ContentValues values = new ContentValues();
        values.put(ShoppingSQLiteHelper.COLUMN_PRODUCT, product);
        values.put(ShoppingSQLiteHelper.COLUMN_QUANTITY, quantity);

        long insertId = database.insert(ShoppingSQLiteHelper.TABLE_SHOPPING_LIST, null, values);

        Cursor cursor = database.query(ShoppingSQLiteHelper.TABLE_SHOPPING_LIST,
                columns, ShoppingSQLiteHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Shopping shoppingMemo = cursorToShopping(cursor);
        cursor.close();

        return shoppingMemo;
    }
    private Shopping cursorToShopping(Cursor cursor)
    {
        int idIndex = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_PRODUCT);
        int idQuantity = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_QUANTITY);
        String product = cursor.getString(idProduct);
        int quantity = cursor.getInt(idQuantity);
        long id = cursor.getLong(idIndex);
        Shopping shoppingMemo = new Shopping(product, quantity, id);

        return shoppingMemo;

    }

}
