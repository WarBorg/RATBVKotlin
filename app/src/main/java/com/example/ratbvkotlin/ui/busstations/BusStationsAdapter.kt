package com.example.ratbvkotlin.ui.busstations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ratbvkotlin.databinding.FragmentBusStationListItemBinding
import com.example.ratbvkotlin.ui.busstations.BusStationsViewModel.BusStationItemViewModel

class BusStationsAdapter
    : ListAdapter<BusStationItemViewModel, BusStationsAdapter.BusStationViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BusStationViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentBusStationListItemBinding.inflate(layoutInflater)

        return BusStationViewHolder(binding)
    }

    override fun onBindViewHolder(busStationViewHolder: BusStationViewHolder,
                                  position: Int) {

        val busStation = getItem(position)
        busStationViewHolder.bind(busStation)
    }

    inner class BusStationViewHolder(private val binding: FragmentBusStationListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(busStationItemViewModel: BusStationItemViewModel) {
            binding.busStationViewModel = busStationItemViewModel
        }
    }

    /**
     * Class used to perform a diff between the old list of elements and the new list,
     * each time [submitList] is called on the [BusStationsAdapter].
     */
    private class DiffCallback : DiffUtil.ItemCallback<BusStationItemViewModel>() {

        override fun areItemsTheSame(oldItem: BusStationItemViewModel,
                                     newItem:BusStationItemViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusStationItemViewModel,
                                        newItem:BusStationItemViewModel): Boolean {
            return oldItem.busStation.id == newItem.busStation.id
        }
    }
}