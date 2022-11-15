package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.Utils
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.ItemMainBinding


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // asteroid clicked this block or lambda will be called by MainAdapter
        val adapter= Adapter(Adapter.OnClickListener{
                asteroid->view?.findNavController()?.
        navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter=adapter
        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroid()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val selectedDateItem= when (item.itemId) {
            R.id.show_all_menu-> Utils.AsteroidFilter.SHOW_WEEK
            R.id.show_buy_menu-> Utils.AsteroidFilter.SHOW_SAVED
            else-> Utils.AsteroidFilter.SHOW_TODAY
        }
        viewModel.applyFilter(selectedDateItem)
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}


class Adapter ( val onClickListener: OnClickListener) :
    ListAdapter< Asteroid, Adapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val viewDataBinding: ItemMainBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bindData(asteroid: Asteroid) {
            viewDataBinding.asteroids = asteroid
            viewDataBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bindData(asteroid)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}