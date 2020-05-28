package com.example.ratbvkotlin.ui.buslines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ratbvkotlin.databinding.FragmentBusLineListItemBinding
import com.example.ratbvkotlin.ui.buslines.BusLinesViewModel.BusLineItemViewModel

class BusLinesAdapter
    : ListAdapter<BusLineItemViewModel, BusLinesAdapter.BusLineViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BusLineViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentBusLineListItemBinding.inflate(layoutInflater)
        return BusLineViewHolder(binding)
    }

    override fun onBindViewHolder(busLineViewHolder: BusLineViewHolder,
                                  position: Int) {

        val busLine = getItem(position)
        busLineViewHolder.bind(busLine)
    }

    inner class BusLineViewHolder(private val binding: FragmentBusLineListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(busLineItemViewModel: BusLineItemViewModel) {
            binding.busLineViewModel = busLineItemViewModel
        }
    }

    /**
     * Class used to perform a diff between the old list of elements and the new list,
     * each time [submitList] is called on the [BusLinesAdapter].
     */
    private class DiffCallback : DiffUtil.ItemCallback<BusLineItemViewModel>() {

        override fun areItemsTheSame(oldItem: BusLineItemViewModel,
                                     newItem: BusLineItemViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusLineItemViewModel,
                                        newItem: BusLineItemViewModel): Boolean {

            return oldItem.busLine.id == newItem.busLine.id
        }
    }
}
