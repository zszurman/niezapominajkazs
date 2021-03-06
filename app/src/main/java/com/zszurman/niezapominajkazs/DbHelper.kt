package com.zszurman.niezapominajkazs


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns
import android.widget.Toast
import com.zszurman.niezapominajkazs.MainActivity.Companion.list
import com.zszurman.niezapominajkazs.MainActivity.Companion.total

object TableInfo : BaseColumns {
    const val DATABASE_NAME = "BazaZs.db"
    const val TABLE_NAME = "Niezapominajka"
    const val COL_ID = "ID"
    const val COL_TYT = "Tytul"
    const val COL_NOT = "Notatka"
    const val COL_ADR = "Adres"
    const val COL_R = "Rok"
    const val COL_M = "Miesiac"
    const val COL_D = "Dzien"
    const val DATABASE_VERSION = 1
    val projections = arrayOf(COL_ID, COL_TYT, COL_NOT, COL_ADR, COL_R, COL_M, COL_D)
}

object BasicCommand {
    const val createTable: String =
        "CREATE TABLE IF NOT EXISTS " + TableInfo.TABLE_NAME + " (" +
                TableInfo.COL_ID + " INTEGER PRIMARY KEY, " +
                TableInfo.COL_TYT + " TEXT, " +
                TableInfo.COL_NOT + " TEXT, " +
                TableInfo.COL_ADR + " TEXT, " +
                TableInfo.COL_R + " INTEGER, " +
                TableInfo.COL_M + " INTEGER, " +
                TableInfo.COL_D + " INTEGER);"
    const val deleteTable = "DROP TABLE IF EXISTS " + TableInfo.TABLE_NAME
}

class DbHelper(private val context: Context) :
    SQLiteOpenHelper(context, TableInfo.DATABASE_NAME, null, TableInfo.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(BasicCommand.createTable)
        Toast.makeText(this.context, "Baza Gotowa", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(BasicCommand.deleteTable)
    }

    fun insert(values: ContentValues): Long {

        val db = this.writableDatabase
        return db!!.insert(TableInfo.TABLE_NAME, "", values)
    }

    private fun xxxxxxxxx(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ): Cursor {
        val db = this.writableDatabase
        val qb = SQLiteQueryBuilder()
        qb.tables = TableInfo.TABLE_NAME
        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)

    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val db = this.writableDatabase
        return db!!.delete(TableInfo.TABLE_NAME, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {

        val db = this.writableDatabase

        return db!!.update(TableInfo.TABLE_NAME, values, selection, selectionArgs)
    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TableInfo.TABLE_NAME, null, null)
        db.close()
    }

    fun initListAlfabet(title: String): ArrayList<Nota> {
        val dbHelper = DbHelper(context)

        fun initRecord(cursor: Cursor) {
            val nr = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_ID))
            val tyt = cursor.getString(cursor.getColumnIndex(TableInfo.COL_TYT))
            val not = cursor.getString(cursor.getColumnIndex(TableInfo.COL_NOT))
            val adr = cursor.getString(cursor.getColumnIndex(TableInfo.COL_ADR))
            val r = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_R))
            val m = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_M))
            val d = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_D))
            val x = Nota(nr, tyt, not, adr, r, m, d)
            list.add(x)
        }

        val selectionArgs = arrayOf(title)
        val cursor = dbHelper.xxxxxxxxx(
            TableInfo.projections,
            TableInfo.COL_TYT + " like ?",
            selectionArgs,
            TableInfo.COL_TYT
        )
        list.clear()

        if (cursor.moveToFirst()) {
            do {
                initRecord(cursor)
            } while (cursor.moveToNext())
        }
        total = list.size
        return list
    }
}