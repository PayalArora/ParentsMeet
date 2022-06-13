package com.logicsquare.parentsmeet.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.model.JobsItem
import com.logicsquare.parentsmeet.utils.getProgressDrawable

class JobsAdapter(private var jobsList: ArrayList<JobsItem>, val context: Context) :
    RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder(inflateView(parent, R.layout.jobs_adapter))
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.tvTittle.text = jobsList[position].title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvDesc.text = Html.fromHtml(jobsList[position].description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.tvDesc.text = Html.fromHtml(jobsList[position].description)
        }
        holder.tvType.text ="${jobsList[position].companyName} | ${jobsList[position].jobType} | ${jobsList[position].experienceRequirement} | ${jobsList[position].locationPreference}"

        holder.mainLayout.setOnClickListener {
            onItemClickListener.onClick(jobsList[position])
        }
        if (jobsList[position].isJobApplied == 1){
            //holder.ivApply.visibility = View.GONE
            holder.ivSave.visibility = View.GONE
            holder.ivApply.setImageResource(R.drawable.check_filled)
            holder.ivApply.isEnabled = false
            holder.ivSave.isEnabled = false
        } else{
            holder.ivApply.isEnabled = true
            holder.ivSave.isEnabled = true
            holder.ivApply.setImageResource(R.drawable.ic_apply_job)
            holder.ivApply.visibility = View.VISIBLE
        }

        if (jobsList[position].isJobSaved == 1){
            holder.ivSave.isEnabled = false
            holder.ivSave.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else{
            holder.ivSave.isEnabled = true
            holder.ivSave.setImageResource(R.drawable.ic_save_job)
        }
        holder.ivApply.setOnClickListener {
            if (jobsList[position].isJobApplied == 0)
                jobsList[position] =  onItemClickListener.applyJob(jobsList[position])
            notifyDataSetChanged()
        }

        holder.ivSave.setOnClickListener {
            if (jobsList[position].isJobSaved == 0)
                onItemClickListener.saveJob(jobsList[position])
            notifyDataSetChanged()
        }
        val circularProgressDrawable = context?.getProgressDrawable()
        circularProgressDrawable?.start()
        if (!jobsList[position].logo.isNullOrEmpty()) {
            Glide.with(context).load(jobsList[position].logo)
                .placeholder(circularProgressDrawable).centerCrop().into(holder.view)
        }
//            Glide.with(context).load(circularProgressDrawable)
//                .into(holder.view)

    }

    private fun inflateView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class JobsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTittle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
        val ivApply: ImageView = itemView.findViewById(R.id.iv_apply)
        val ivSave: ImageView = itemView.findViewById(R.id.iv_save)
        val view: ImageView = itemView.findViewById(R.id.iv_logo)
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.main_layout)
    }

    interface OnItemClickListener {
        fun onClick(job: JobsItem)
        fun applyJob(job: JobsItem):JobsItem
        fun saveJob(job: JobsItem):JobsItem


    }

}