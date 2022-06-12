package com.logicsquare.parentsmeet.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.AdapterMeetBinding
import com.logicsquare.parentsmeet.model.UsersItem
import com.logicsquare.parentsmeet.utils.capitalizeWords

class MeetAdapter(val response: List<UsersItem?>?, var onClickListeners: OnClickListeners) :
    RecyclerView.Adapter<MeetAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterMeetBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterMeetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text =
            "${response?.get(position)?.name?.first} ${response?.get(position)?.name?.last}".capitalizeWords()
        holder.binding.tvDescription.text =
            "${response?.get(position)?.relation?.capitalize()} of ${response?.get(position)?.kidsCount}, ${response?.get(position)?.profession?.capitalize()}"
        holder.binding.btnMeet.setOnClickListener {
            onClickListeners.onMeetClick(response?.get(position)!!)
        }
        holder.binding.btnMessage.setOnClickListener {
            onClickListeners.onMessageClick(response?.get(position)!!)
        }
        holder.binding.mainLayout.setOnClickListener {
            onClickListeners.onClick(response?.get(position)!!)
        }

    }

    override fun getItemCount() = response?.size ?: 0

    interface OnClickListeners {
        fun onClick(usersItem: UsersItem)
        fun onMeetClick(usersItem: UsersItem)
        fun onMessageClick(usersItem: UsersItem)
    }

}