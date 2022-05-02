package com.logicsquare.parentsmeet.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.model.KidsItem

class KidsAdapter(private val kidsList: ArrayList<KidsItem>,val context:Context) : RecyclerView.Adapter<KidsAdapter.KidsViewHolder>(){

    lateinit var onItemClickListener:OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidsViewHolder {
        return KidsViewHolder(inflateView(parent, R.layout.layout_kids))
    }

    override fun onBindViewHolder(holder: KidsViewHolder, position: Int) {
//        Picasso.with(context).load(kidsList.get(position).).into(holder.ivKid)
        holder.tvName.text = kidsList[position].name

        holder.ivKid.setOnClickListener{
            onItemClickListener.onClick(kidsList[position])
        }

        holder.tvName.setOnClickListener{
            onItemClickListener.onClick(kidsList[position])
        }
    }

    private fun inflateView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    override fun getItemCount(): Int {
        return kidsList.size
    }

    fun setListener(onItemClickListener:OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class KidsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivKid: ImageView = itemView.findViewById(R.id.iv_kid)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }

    interface OnItemClickListener{
        fun onClick(kidsItem:KidsItem)
    }
}