package com.logicsquare.parentsmeet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.ForumCommentItemBinding
import com.logicsquare.parentsmeet.model.CommentsItem
import com.logicsquare.parentsmeet.utils.capitalizeWords

class CommentsForumAdapter(val response: List<CommentsItem?>?) :
    RecyclerView.Adapter<CommentsForumAdapter.ViewHolder>() {
    class ViewHolder(val binding: ForumCommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForumCommentItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.userMsg.setText(response?.get(position)?.content)
        holder.binding.userName.setText(response?.get(position)?.user?.name?.full?.capitalizeWords())
    }

    override fun getItemCount() = response?.size ?: 0

}