package berufsschulefreising.de.shoppingsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albrecht on 22.10.2016.
 */

// DAO = Data Access Object

public class Shopping_DB_Connection
{
    private static final String LOG_TAG = Shopping_DB_Connection.class.getSimpleName();

    private SQLiteDatabase database;
    private ShoppingSQLiteHelper dbHelper;
   // Spaltenbezeichnungen
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

    // Daten zum Eintrag in DB werden übergeben
    public Shopping createShopping(String product, int quantity)
    {
        // Content values > zu Einfügen in SQLiteDB
        // put:  key - value
        ContentValues values = new ContentValues();
        values.put(ShoppingSQLiteHelper.COLUMN_PRODUCT, product);
        values.put(ShoppingSQLiteHelper.COLUMN_QUANTITY, quantity);

        // Eintrag in DB:
        // zurückgegeben wird eine eindeutige ID des Datensatzes
        long insertId = database.insert(ShoppingSQLiteHelper.TABLE_SHOPPING_LIST, null, values);

        // Suche in der DB ("query")
        // Der Rückgabewert ist eine Art Zeiger auf die Zeile der DB-Tabelle
        /*
        Paramter 1: Tabellenname
        Patameter 2: Namen der TAbellenspalten die von der Suchanfrage zurückgegeben wwrden soll
        Parameter 3: Such-String > enthält den Namen der zu vergleichenden Spalte und testet
        mit dem = - Operator, ob die Werte den vorgegebenen Daten entsprechen
        Beispiel:  _id =6
        Ist der Suchstring null, werden alle Datensätze der Tabelle zurückgeliefert

        Zurückgegeben wird ein spiezielles Data Access Objekt (DAO) vom Typ Cursor)
        Mit diesem Cursor können die Datensätze durchlaufen werden und es kann auf ihre
        inneren Werte zugegriffen werden
         */
        // http://www.willemer.de/informatik/android/prgsql.htm
        Cursor cursor = database.query(ShoppingSQLiteHelper.TABLE_SHOPPING_LIST,
                columns, ShoppingSQLiteHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);
        // Wechsel zum ersten datensatz
        cursor.moveToFirst();
        Shopping shopping = cursorToShopping(cursor);
        cursor.close();

        return shopping;
    }
    // DB auslesen und ausgelesene Daten in Objekten ablegen
    private Shopping cursorToShopping(Cursor cursor)
    {
        //  liefert den Index der Spalte zurück:
        int idIndex = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_PRODUCT);
        int idQuantity = cursor.getColumnIndex(ShoppingSQLiteHelper.COLUMN_QUANTITY);
        String product = cursor.getString(idProduct);
        int quantity = cursor.getInt(idQuantity);
        long id = cursor.getLong(idIndex);
        Shopping shopping = new Shopping(product, quantity, id);

        return shopping;

    }

    public List<Shopping> getAllShoppingMemos() {
        List<Shopping> shoppingList = new ArrayList<>();
        // Ist der Suchstring 3 null, werden alle Datensätze der Tabelle zurückgeliefert
        Cursor cursor = database.query(ShoppingSQLiteHelper.TABLE_SHOPPING_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Shopping shopping;

        while(!cursor.isAfterLast())
        {
            // Objekt aus Datensatz bilden über Methodenaufruf
            shopping = cursorToShopping(cursor);
            // Objekt der Liste hinzufügen
            shoppingList.add(shopping);
            Log.d(LOG_TAG, "ID: " + shopping.getId() + ", Inhalt: " + shopping.toString());
            cursor.moveToNext();
        }
        // Wichtig : Schließen des Cursors !!
        cursor.close();
        // Liste mit allen Shopping-Objekten wird zurückgegeben
        return shoppingList;
    }

}
