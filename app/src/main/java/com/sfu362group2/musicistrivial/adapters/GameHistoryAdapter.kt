package com.sfu362group2.musicistrivial.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.database.GameHistory

class GameHistoryAdapter(private val context: Context, private var list: List<GameHistory>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.game_history_adapter, null)
        val textView = view.findViewById<TextView>(R.id.date)
        val entry = list[position]
        val text = "Date: ${entry.date} Score ${entry.score} Streak ${entry.streak}"
        textView.text = text
        return view
    }

    fun replaceList(newList: List<GameHistory>){
        list = newList
    }

}