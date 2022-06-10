package com.logicsquare.parentsmeet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.CheckboxBinding


class CustomInterestAdapter(val type:String, val list: List<Any?>, val onItemClick: OnItemClickActivity) : RecyclerView.Adapter<CustomInterestAdapter.ViewHolder>() {
    var arr = arrayListOf<String>()
    var  sel:List<Any> = arrayListOf()
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
        if (sel.contains(list?.get(position).toString())){
            viewHolder.binding.checkbox.isChecked = true
        }

    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }

    fun setSelection(sel: List<Any>) {
     this.sel = sel
        notifyDataSetChanged()
    }

}

interface OnItemClickActivity{
    fun  onItemClick(position:ArrayList<String>, type: String)
}
