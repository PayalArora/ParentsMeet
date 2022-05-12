package com.logicsquare.parentsmeet.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.model.BlogsItem
import com.logicsquare.parentsmeet.utils.RoundedCornersTransformation
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class BlogsAdapter (private val blogsList: ArrayList<BlogsItem>, val context: Context) : RecyclerView.Adapter<BlogsAdapter.BlogsViewHolder>(){

    lateinit var onItemClickListener:OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogsViewHolder {
        return BlogsViewHolder(inflateView(parent, R.layout.layout_blogs))
    }

    override fun onBindViewHolder(holder: BlogsViewHolder, position: Int) {
        val transformation: Transformation = RoundedCornersTransformation(30, 0)
        if (!blogsList[position]?.coverImage.isNullOrEmpty()) {
            Picasso.with(context).load(blogsList?.get(position)?.coverImage).fit().transform(transformation).into(holder.ivBlog);
        }
        holder.tvTittle.text = blogsList[position].title

        holder.ivBlog.setOnClickListener{
            onItemClickListener.onClick(blogsList[position])
        }

        holder.tvTittle.setOnClickListener{
            onItemClickListener.onClick(blogsList[position])
        }
    }

    private fun inflateView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    override fun getItemCount(): Int {
        return blogsList.size
    }

    fun setListener(onItemClickListener:OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class BlogsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivBlog: ImageView = itemView.findViewById(R.id.iv_blog)
        val tvTittle: TextView = itemView.findViewById(R.id.tv_title)
    }

    interface OnItemClickListener{
        fun onClick(blog: BlogsItem)
    }
}