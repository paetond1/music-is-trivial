package com.sfu362group2.musicistrivial.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.activities.GamePlayActivity

class SongListAdapter(
    private val myContext: Context,
    private var songList: ArrayList<GamePlayActivity.Song>,
): BaseAdapter() {
    override fun getCount(): Int {
        return songList.size
    }

    override fun getItem(position: Int): Any {
        return songList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(myContext, R.layout.song_list_adapter, null)
        val song_title = view.findViewById<TextView>(R.id.song_title)
        val rank_num = view.findViewById<TextView>(R.id.bubble_text)

        val thisSong = songList[position]
        song_title.text = thisSong.song_title
        if (thisSong.input_rank != 0) {
            rank_num.text = thisSong.input_rank.toString()
        } else {
            rank_num.text = ""
        }

        return view
    }

    fun replaceList(newSongList: ArrayList<GamePlayActivity.Song>) {
        songList = newSongList
    }
}