package com.logicsquare.parentsmeet.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.model.JobsItem

class JobsAdapter(private val jobsList: ArrayList<JobsItem>, val context: Context) :
    RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder(inflateView(parent, R.layout.jobs_adapter))
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.tvTittle.text = jobsList[position].title
        holder.tvDesc.text = Html.fromHtml(jobsList[position].description)
        holder.tvType.text = jobsList[position].jobType

        holder.mainLayout.setOnClickListener {
            onItemClickListener.onClick(jobsList[position])
        }

        holder.ivApply.setOnClickListener {
            if (jobsList[position].isJobApplied == 0)
                onItemClickListener.applyJob(jobsList[position])
        }

        holder.ivSave.setOnClickListener {
            if (jobsList[position].isJobSaved == 0)
                onItemClickListener.saveJob(jobsList[position])
        }
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
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.main_layout)
    }

    interface OnItemClickListener {
        fun onClick(job: JobsItem)
        fun applyJob(job: JobsItem)
        fun saveJob(job: JobsItem)
    }
}