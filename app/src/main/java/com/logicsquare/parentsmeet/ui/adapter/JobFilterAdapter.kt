package com.logicsquare.parentsmeet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.CheckboxBinding


class JobFilterAdapter(val type:Int, val list: List<Any?>, val onItemClick: OnItemClickEvent) : RecyclerView.Adapter<JobFilterAdapter.ViewHolder>() {
    var arr = arrayListOf<String>()
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(val binding: CheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = CheckboxBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.checkbox.setText(list?.get(position).toString())
        viewHolder.binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked){
                arr.add(compoundButton.text.toString())
            } else {
                arr.remove(compoundButton.text.toString())
            }
            onItemClick.onItemClick(arr,type)
        }

    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }

}

interface OnItemClickEvent{
    fun  onItemClick(position:ArrayList<String>, type: Int)
}