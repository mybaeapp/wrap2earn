package com.wraptwoearn.locationtracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wraptwoearn.model.CityItem
import com.wraptwoearn.util.Constatns
import kotlinx.android.synthetic.main.item_context_bottom.view.*


class BottomSheetMenuAdapter(
    private val items: List<CityItem>, val callbackInterface: CallbackInterface
) : RecyclerView.Adapter<BottomSheetMenuAdapter.BottomSheetMenuViewHolder>() {


    private var callbackInterfaces: CallbackInterface

    //    lateinit var clipboard: BottomSheetDialog
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetMenuViewHolder {
        return BottomSheetMenuViewHolder(
            LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_context_bottom, parent, false)
        )

    }

    init {
        callbackInterfaces = callbackInterface
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BottomSheetMenuViewHolder, position: Int) {
        holder.bind(items[position], callbackInterfaces)
    }

    class BottomSheetMenuViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(
            item: CityItem,
            mBottomSheetBehaviors: CallbackInterface
        ) {
            with(view) {
                city_name.text = item.city_name

                setOnClickListener {
                    Constatns.CITY_NAME = item.city_name
                    mBottomSheetBehaviors.passResultCallback(item.city_name);

                }
            }
        }
    }

    interface CallbackInterface {
        fun passResultCallback(message: String)
    }


}