package com.example.MyNotes

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.MyNotes.activities.DetailsActivity
import com.example.MyNotes.databinding.CardViewBinding


class CardViewAdapter(val context: Context, val db: SQLiteDatabase, var notes: ArrayList<Note>): RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val cursor = db.query(TableInfo.TABLE_NAME, null, BaseColumns._ID+"=?", arrayOf(holder.adapterPosition.plus(1).toString()),
            null,null,null)



        var cardTitle = holder.binding.cardViewTitle
        var cardText = holder.binding.cardViewText
        var cardDate = holder.binding.cardViewDate

        cardTitle.setText(notes[holder.adapterPosition].title)
        cardText.setText(notes[holder.adapterPosition].message)
        cardDate.setText(notes[holder.adapterPosition].date)


        holder.binding.root.setOnClickListener {
            val intentEdit = Intent(holder.binding.root.context, DetailsActivity::class.java)
            val cardTitleEdit = notes[holder.adapterPosition].title
            val cardTextEdit = notes[holder.adapterPosition].message
            val idEdit = notes[holder.adapterPosition].id.toString()
            intentEdit.putExtra("title", cardTitleEdit)
            intentEdit.putExtra("message", cardTextEdit)
            intentEdit.putExtra("id", idEdit)

            holder.binding.root.context.startActivity(intentEdit)
        }

        holder.binding.root.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                val cm = context.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("CopyText", "Title: " + cardTitle.text + "\n" + "Text: " + cardText.text)
                cm.setPrimaryClip(clipData)

                Toast.makeText(holder.binding.root.context, "Copied!", Toast.LENGTH_SHORT).show()
                return true
            }

        })
    }

    override fun getItemCount(): Int {
        val cursor = db.query(TableInfo.TABLE_NAME, null, null, null,
            null,null,null)
        val size = cursor.count

        cursor.close()
        return size
    }

}

class MyViewHolder(val binding: CardViewBinding): RecyclerView.ViewHolder(binding.root)
