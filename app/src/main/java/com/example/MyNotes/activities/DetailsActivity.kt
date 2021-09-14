package com.example.MyNotes.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.MyNotes.R
import com.example.MyNotes.databaseHelper.DataBaseHelper
import com.example.MyNotes.databaseHelper.TableInfo
import com.example.MyNotes.databinding.ActivityDetailsBinding
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    val c = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(intent.hasExtra("title")) binding.activityDetailsTitle.setText(intent.getStringExtra("title"))
        if(intent.hasExtra("message")) binding.activityDetailsText.setText(intent.getStringExtra("message"))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.save_button){
            val saveInfoToast = Toast.makeText(applicationContext, "Note Saved!", Toast.LENGTH_SHORT)
            val dbHelper = DataBaseHelper(applicationContext)
            val db = dbHelper.writableDatabase


            val title = binding.activityDetailsTitle.text.toString()
            val message = binding.activityDetailsText.text.toString()
            val value = ContentValues()
            value.put("title", title)
            value.put("message", message)
            value.put("date", "${if(c.get(Calendar.DAY_OF_MONTH)<10) "0${c.get(Calendar.DAY_OF_MONTH)}" else c.get(
                Calendar.DAY_OF_MONTH)} . ${if(c.get(Calendar.MONTH)<10) "0${c.get(Calendar.MONTH)}" else c.get(
                Calendar.MONTH)} . ${c.get(java.util.Calendar.YEAR)}")
            if(intent.hasExtra("id")){
                if (!title.isNullOrEmpty() || !message.isNullOrEmpty()) {
                    db.update(TableInfo.TABLE_NAME, value, BaseColumns._ID + "=?", arrayOf(intent.getStringExtra("id")))
                    saveInfoToast.show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "I don't have anything to save :(",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else {
                if (!title.isNullOrEmpty() || !message.isNullOrEmpty()) {
                    db.insertOrThrow(TableInfo.TABLE_NAME, null, value)
                    saveInfoToast.show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "I don't have anything to save :(",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
            db.close()
            onBackPressed()
        }

        if(item.itemId == R.id.delete_button){
            val deleteInfoToast = Toast.makeText(applicationContext, "Note Deleted!", Toast.LENGTH_SHORT)
            val dbHelper = DataBaseHelper(applicationContext)
            val db = dbHelper.writableDatabase

            deleteInfoToast.show()

            onBackPressed()
            db.delete(TableInfo.TABLE_NAME, BaseColumns._ID+"=?",
                arrayOf(intent.getStringExtra("id")))
        }
        return super.onOptionsItemSelected(item)
    }
}