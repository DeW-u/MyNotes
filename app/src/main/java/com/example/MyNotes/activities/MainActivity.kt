package com.example.MyNotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.MyNotes.*
import com.example.MyNotes.databaseHelper.DataBaseHelper
import com.example.MyNotes.databaseHelper.TableInfo
import com.example.MyNotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.takeNotesBT.setOnClickListener {
            val intent = Intent(applicationContext, DetailsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        val dbHelper = DataBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase

        val cursor = db.query(TableInfo.TABLE_NAME, null, null, null, null, null, null )

        val notes = ArrayList<Note>()

        if(cursor.count > 0){
            cursor.moveToFirst()
            while(!cursor.isAfterLast){
                if(!cursor.isNull(1) || !cursor.isNull(2)) {
                    val note = Note()
                    note.id = cursor.getInt(0)
                    note.title = cursor.getString(2)
                    note.message = cursor.getString(1)
                    note.date = cursor.getString(3)
                    notes.add(note)
                    cursor.moveToNext()
                }
            }
        }
        cursor.close()

        binding.recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        binding.recyclerView.adapter = CardViewAdapter(applicationContext, db, notes)

    }

}
