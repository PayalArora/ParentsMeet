package com.logicsquare.parentsmeet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.SpinnerTextViewBinding


class SpinnerAdapter(var array:ArrayList<String> = arrayListOf(), val spinnerItemClick: onSpinnerItemClick) : RecyclerView.Adapter<SpinnerAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    var type1:Int = 0
    class ViewHolder(val binding: SpinnerTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = SpinnerTextViewBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.apply {
            text1.setText(array.get(position))
            text1.setOnClickListener {
                spinnerItemClick.clickItem(position, type1)
            }
        }

    }

    override fun getItemCount(): Int {
        return array.size
    }

    fun setType(type1:Int) {
        this.type1 = type1
    }

}

interface onSpinnerItemClick{
    fun clickItem(position: Int, type1: Int)
}