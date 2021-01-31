package com.hardtm.daggerpro.jokes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardtm.daggerpro.R
import com.hardtm.daggerpro.db.JokeEntity
import kotlinx.android.synthetic.main.item_recycler.view.*

class JokesRecyclerAdapter(
    private val jokesDataList: List<JokeEntity>,
    val jokeClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<JokesRecyclerAdapter.JokesViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {

        if (!this::context.isInitialized) {
            context = parent.context
        }

        return JokesViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recycler,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        holder.fillJokeCard(jokesDataList[position], jokeClickListener)
    }

    override fun getItemCount(): Int = jokesDataList.size

    class JokesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun fillJokeCard(itemJoke: JokeEntity, linkClickListener: (String) -> Unit) {
            itemView.cardContent.text = itemJoke.jokeText

            itemView.cardItem.setOnClickListener {
                linkClickListener(itemJoke.jokeId.toString())
            }
        }
    }
}
