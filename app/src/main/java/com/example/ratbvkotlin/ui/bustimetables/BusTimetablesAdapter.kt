package com.example.ratbvkotlin.ui.bustimetables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ratbvkotlin.databinding.FragmentBusTimetableListItemBinding
import com.example.ratbvkotlin.ui.bustimetables.BusTimetablesViewModel.BusTimetableItemViewModel

class BusTimetablesAdapter
    : ListAdapter<BusTimetableItemViewModel, BusTimetablesAdapter.BusTimetableViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BusTimetableViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentBusTimetableListItemBinding.inflate(layoutInflater)

        return BusTimetableViewHolder(binding)
    }

    override fun onBindViewHolder(busTimetaleViewHolder: BusTimetableViewHolder,
                                  position: Int) {

        val busTimetable = getItem(position)
        busTimetaleViewHolder.bind(busTimetable)
    }

    inner class BusTimetableViewHolder(private val binding: FragmentBusTimetableListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(busTimetableItemViewModel: BusTimetableItemViewModel) {
            binding.busTimetableViewModel = busTimetableItemViewModel
        }
    }

    /**
     * Class used to perform a diff between the old list of elements and the new list,
     * each time [submitList] is called on the [BusTimetablesAdapter].
     */
    private class DiffCallback : DiffUtil.ItemCallback<BusTimetableItemViewModel>() {

        override fun areItemsTheSame(oldItem: BusTimetableItemViewModel,
                                     newItem: BusTimetableItemViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusTimetableItemViewModel,
                                        newItem: BusTimetableItemViewModel): Boolean {

            return oldItem.busTimetable.id == newItem.busTimetable.id
        }
    }
}
