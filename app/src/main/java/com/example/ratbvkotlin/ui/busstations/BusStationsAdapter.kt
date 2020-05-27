package com.example.ratbvkotlin.ui.busstations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ratbvkotlin.databinding.FragmentBusStationListItemBinding
import com.example.ratbvkotlin.ui.busstations.BusStationsViewModel.BusStationViewModel

class BusStationsAdapter
    : ListAdapter<BusStationViewModel, BusStationsAdapter.BusStationViewHolder>(DiffCallback()) {

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

        fun bind(busStationViewModel: BusStationViewModel) {
            binding.busStationViewModel = busStationViewModel
        }
    }

    /**
     * Class used to perform a diff between the old list of elements and the new list,
     * each time [submitList] is called on the [BusStationsAdapter].
     */
    private class DiffCallback : DiffUtil.ItemCallback<BusStationViewModel>() {

        override fun areItemsTheSame(oldItem: BusStationViewModel,
                                     newItem:BusStationViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusStationViewModel,
                                        newItem:BusStationViewModel): Boolean {
            return oldItem.busStation.id == newItem.busStation.id
        }
    }
}