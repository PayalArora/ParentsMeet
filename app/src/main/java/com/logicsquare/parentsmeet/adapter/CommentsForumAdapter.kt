package com.logicsquare.parentsmeet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.ForumCommentItemBinding
import com.logicsquare.parentsmeet.interfaces.OnItemClickId
import com.logicsquare.parentsmeet.model.AddCommentResponse
import com.logicsquare.parentsmeet.model.CommentsItem
import com.logicsquare.parentsmeet.model.GetAllCommentsResponse

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
        holder.binding.userName.setText(response?.get(position)?.user?.name?.full)
    }

    override fun getItemCount() = response?.size ?: 0

}