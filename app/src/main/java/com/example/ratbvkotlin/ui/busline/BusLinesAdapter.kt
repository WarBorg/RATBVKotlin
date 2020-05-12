package com.example.ratbvkotlin.ui.busline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ratbvkotlin.databinding.FragmentBusLineBinding

import com.example.ratbvkotlin.ui.busline.BusLinesViewModel.*

import com.example.ratbvkotlin.ui.busline.BusLinesFragment.OnListFragmentInteractionListener
import com.example.ratbvkotlin.ui.busline.dummy.DummyContent.DummyItem

class BusLinesAdapter(private val mListener: OnListFragmentInteractionListener?)
    : ListAdapter<BusLineViewModel, BusLinesAdapter.BusLineViewHolder>(DiffCallback()) {

    // TODO resolve the click event issues (MVVM says to not place it in the ViewModel)
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BusLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentBusLineBinding.inflate(layoutInflater)
        return BusLineViewHolder(binding)
    }

    override fun onBindViewHolder(busLineViewHolder: BusLineViewHolder,
                                  position: Int) {

        val busLine = getItem(position)
        busLineViewHolder.bind(busLine)
    }

    inner class BusLineViewHolder(private val binding: FragmentBusLineBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(busLineViewModel: BusLineViewModel) {
            binding.busLineViewModel = busLineViewModel
        }
    }

    /**
     * Class used to perform a diff between the old list of elements and the new list,
     * each time [submitList] is called on the [BusLinesAdapter].
     */
    private class DiffCallback : DiffUtil.ItemCallback<BusLineViewModel>() {
        override fun areItemsTheSame(oldItem: BusLineViewModel,
                                     newItem: BusLineViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusLineViewModel,
                                        newItem: BusLineViewModel): Boolean {
            //return oldItem.busLine.lastUpdateDate == newItem.busLine.lastUpdateDate
            return oldItem.busLine.id == newItem.busLine.id
        }
    }
}
