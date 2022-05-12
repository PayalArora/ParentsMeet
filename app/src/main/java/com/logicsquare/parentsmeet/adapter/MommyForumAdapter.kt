package com.logicsquare.parentsmeet.adapter

import android.os.Build
import android.text.Html
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ForumMainItemBinding
import com.logicsquare.parentsmeet.interfaces.OnItemClickId
import com.logicsquare.parentsmeet.interfaces.OnItemClickPosition
import com.logicsquare.parentsmeet.model.ForumResponse
import com.logicsquare.parentsmeet.utils.getProgressDrawable

class MommyForumAdapter(val onItemClick: OnItemClickId, val response: List<ForumResponse.ForumsItem?>?) :
    RecyclerView.Adapter<MommyForumAdapter.ViewHolder>() {


    class ViewHolder(val binding: ForumMainItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForumMainItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.binding.userMsg.setText(fromHtml(response?.get(position)?.description, Html.FROM_HTML_MODE_COMPACT))
//        } else {
//            holder.binding.userMsg.setText(fromHtml(response?.get(position)?.title))
//        }
        holder.binding.userMsg.setText(fromHtml(response?.get(position)?.title))
        holder.binding.txtComments.setText(response?.get(position)?.comments.toString()+" Comments")
        holder.binding.txtLikes.setText(response?.get(position)?.likes.toString()+" Likes")
        if(response?.get(position)?.isLiked ==1){
            holder.binding.imgLike.setImageResource(R.drawable.ic_like_selected)
        } else{
            holder.binding.imgLike.setImageResource(R.drawable.ic_like)
        }
        if (response?.get(position)?.isCreatedByAdmin?:true){
            holder.binding.userName.setText(holder.binding.root.context.getString(R.string. appname))
            holder.binding.imgUser.setImageResource(R.mipmap.ic_launcher_round)
        } else {
            holder.binding.userName.setText(response?.get(position)?.createdBy?.name?.full)
        }
//        val circularProgressDrawable = holder.binding.root.context.getProgressDrawable()
//        circularProgressDrawable.start()

//        if (response?.get(position)?. != null) {
//            Glide.with(this.root).load(item?.Poster)
//                .placeholder(circularProgressDrawable).into(imageView)
//        } else
//            Glide.with(this.root).load(circularProgressDrawable)
//                .into(imageView)
        holder.itemView.setOnClickListener {
            response?.get(position)?.id?.let { it1 -> onItemClick.OnClick(it1) }
        }
    }

    override fun getItemCount() = response?.size ?: 0

}