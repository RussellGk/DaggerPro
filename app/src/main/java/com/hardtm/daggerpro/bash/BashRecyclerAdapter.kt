package com.hardtm.daggerpro.bash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardtm.daggerpro.R
import com.hardtm.daggerpro.db.BashEntity
import kotlinx.android.synthetic.main.item_recycler.view.*

class BashRecyclerAdapter(
    private val bashDataList: List<BashEntity>,
    val bashClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<BashRecyclerAdapter.BashViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BashViewHolder {

        if (!this::context.isInitialized) {
            context = parent.context
        }

        return BashViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recycler,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BashViewHolder, position: Int) {
        holder.fillBashCard(bashDataList[position], bashClickListener)
    }

    override fun getItemCount(): Int = bashDataList.size

    class BashViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun fillBashCard(itemBash: BashEntity, linkClickListener: (String) -> Unit) {
            itemView.cardContent.text = itemBash.bashText

            itemView.cardItem.setOnClickListener {
                linkClickListener(itemBash.bashId.toString())
            }
        }
    }
}
