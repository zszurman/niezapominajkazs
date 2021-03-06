package com.zszurman.niezapominajkazs


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*

class AddNoteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        var addId: Int = 0
        var addTyt: String = ""
        var addNot: String = ""
        var addAdr: String = ""
        var addR: Int = 2020
        var addM: Int = 1
        var addD: Int = 26
        var bar: String = "Aktualizacja danych"
        var bat: String = "Aktualizuj"
    }

    private var c = Calendar.getInstance()

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        nrEt.text = addId.toString()
        tytEt.setText(addTyt)
        notEt.setText(addNot)
        adrEt.setText(addAdr)
        rEt.text = addR.toString()
        mEt.text = addM.toString()
        dEt.text = addD.toString()
        dataEt.text = makeText()
        okBtn.text = bat
        nokBtn.text = "Powróć"
        val mActionBar = supportActionBar
        if (mActionBar != null) mActionBar.subtitle = bar

        dateBtn.setOnClickListener {
            val dataPiker = TimePiker()
            dataPiker.show(supportFragmentManager, "date picker")
        }
        nokBtn.setOnClickListener {
            bar = "Aktualizacja danych"
            bat = "Aktualizuj"
            finish()
        }
        okBtn.setOnClickListener {
            addTyt = tytEt.text.toString()
            addNot = notEt.text.toString()
            addAdr = adrEt.text.toString()
            addR = rEt.text.toString().toInt()
            addM = mEt.text.toString().toInt()
            addD = dEt.text.toString().toInt()
            if (tytEt.length() > 0 && notEt.length() > 0) addOrUpdate()
            else Toast.makeText(this, "Uzupełnij dane", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        addR = year
        addM = month
        addD = dayOfMonth
        rEt.text = addR.toString()
        mEt.text = addM.toString()
        dEt.text = addD.toString()
        dataEt.text = makeText()
    }

    private fun makeText(): String {
        var x1 = """$addD.0${addM + 1}.$addR"""
        if (addM > 8) x1 = """$addD.$addM.$addR"""
        return x1
    }

    private fun addOrUpdate() {
        val dbHelper = DbHelper(this)
        val values = ContentValues()
        values.put(TableInfo.COL_TYT, addTyt)
        values.put(TableInfo.COL_NOT, addNot)
        values.put(TableInfo.COL_ADR, addAdr)
        values.put(TableInfo.COL_R, addR)
        values.put(TableInfo.COL_M, addM)
        values.put(TableInfo.COL_D, addD)
        if (addId == 0) {
            val x = dbHelper.insert(values)
            if (x > 0) {
                Toast.makeText(this, "Wydarzenie dodano", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Błąd w dodaniu wydarzenia", Toast.LENGTH_SHORT).show()
            }
        } else
        {
            val selectionArgs = arrayOf(addId.toString())
            val y = dbHelper.update(values, TableInfo.COL_ID + "=?", selectionArgs)
            if (y > 0) {
                Toast.makeText(this, "Dane zaktualizowano", Toast.LENGTH_SHORT).show()
                finish()
            } else
            {
                Toast.makeText(this, "Błąd w aktualizacji danych", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

