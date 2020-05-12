package com.example.ratbvkotlin.ui.busline

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ratbvkotlin.data.BusWebService
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.databinding.FragmentBusLineListBinding

import com.example.ratbvkotlin.ui.busline.dummy.DummyContent
import com.example.ratbvkotlin.ui.busline.dummy.DummyContent.DummyItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [BusLinesFragment.OnListFragmentInteractionListener] interface.
 */
class BusLinesFragment : Fragment() {

    private lateinit var binding: FragmentBusLineListBinding

    private val busLinesViewModel: BusLinesViewModel by viewModel()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBusLineListBinding.inflate(layoutInflater)

        val busLinesAdapter = BusLinesAdapter(listener)
        binding.busLineList.adapter = busLinesAdapter

        lifecycleScope.launch {
            busLinesViewModel.busLines.observe(viewLifecycleOwner,  Observer { busLines ->
                busLinesAdapter.submitList(busLines)
            })
        }

        return binding.root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }
}
