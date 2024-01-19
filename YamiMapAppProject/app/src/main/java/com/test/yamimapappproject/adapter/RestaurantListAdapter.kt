package com.test.yamimapappproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.geometry.LatLng
import com.test.yamimapappproject.databinding.ItemRestaurantBinding
import com.test.yamimapappproject.dataclass.SearchItem

class RestaurantListAdapter(val onClick: (LatLng) -> Unit) :
    RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>() {
    private var itemUesList = emptyList<SearchItem>()

    fun updateList(list:List<SearchItem>){
        this.itemUesList = list
        Log.d("testt","리스트 갯수: ${itemUesList.size}")
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(private var itemRestaurantBinding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(itemRestaurantBinding.root) {
        fun binding(item: SearchItem) {
            itemRestaurantBinding.nameTextView.text = item.title
            itemRestaurantBinding.categoryTextView.text = item.category
            itemRestaurantBinding.locationTextView.text = item.address

            itemRestaurantBinding.root.setOnClickListener {
                val latLng =
                    LatLng(item.mapy.toDouble() / 10000000, item.mapx.toDouble() / 10000000)
                onClick(latLng)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRestaurantBinding: ItemRestaurantBinding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(itemRestaurantBinding)
    }

    override fun getItemCount(): Int {
        return itemUesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(itemUesList[position])
    }
}