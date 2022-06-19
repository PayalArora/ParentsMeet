package com.logicsquare.parentsmeet.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.model.KidsItem
import com.logicsquare.parentsmeet.utils.SharedPref
import com.logicsquare.parentsmeet.utils.capitalizeWords

class KidsAdapter(private val kidsList: ArrayList<KidsItem>,val context:Context) : RecyclerView.Adapter<KidsAdapter.KidsViewHolder>(){

    lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidsViewHolder {
        return KidsViewHolder(inflateView(parent, R.layout.layout_kids))
    }

    override fun onBindViewHolder(holder: KidsViewHolder, position: Int) {
//        Picasso.with(context).load(kidsList.get(position).).into(holder.ivKid)

        if (SharedPref(context).getSelectedKid()!! == kidsList[position].id){
            holder.mainLayout.setBackgroundResource(R.drawable.background_blue_curve_filled)
        }else{
            holder.mainLayout.setBackgroundColor(context.getColor(android.R.color.transparent))
        }
        holder.tvName.text = kidsList[position].name?.capitalizeWords()

        holder.ivKid.setOnClickListener{
            notifyDataSetChanged()
            holder.mainLayout.setBackgroundResource(R.drawable.background_blue_curve_filled)
            onItemClickListener.onClick(kidsList[position])
        }

        holder.tvName.setOnClickListener{
            notifyDataSetChanged()
            holder.mainLayout.setBackgroundResource(R.drawable.background_blue_curve_filled)
            onItemClickListener.onClick(kidsList[position])
        }
    }

    private fun inflateView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    override fun getItemCount(): Int {
        return kidsList.size
    }

    fun setListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class KidsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivKid: ImageView = itemView.findViewById(R.id.iv_kid)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.main_layout)
    }

    interface OnItemClickListener{
        fun onClick(kidsItem:KidsItem)
    }
}