package com.wraptwoearn.locationtracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wraptwoearn.model.CityItem
import kotlinx.android.synthetic.main.bottom_sheet_menu.*

class CustomDialogFragment : BottomSheetDialogFragment(), BottomSheetMenuAdapter.CallbackInterface {

    companion object {

        const val TAG = "CustomBottomSheetDialogFragment"
        var name = ""

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val original = listOf(
            CityItem(
                "Mumbai / Thane"
            ),
            CityItem("Delhi / NCR"),
            CityItem("Bangalore"),
            CityItem("Pune"),
            CityItem("Chennai"),
            CityItem("Kolkata"),
            CityItem("Hyderabad"),
            CityItem("Ahmedabad"),
            CityItem("Surat"),
            CityItem("Jaipur"),
            CityItem("Lucknow"),
            CityItem("Indore"),
            CityItem("Kanpur"),
            CityItem("Nagpur"),
            CityItem("Guwahati"),
            CityItem("Chandigarh"),
            CityItem("Bhopal"),
            CityItem("Patna"),
            CityItem("Bhubaneswar"),
            CityItem("Dehradun"),
            CityItem("Kochi"),
            CityItem("Jodhpur"),
            CityItem("Raipur"),
            CityItem("Bilaspur"),
            CityItem("Vizag"),
            CityItem("Vijaywada"),
            CityItem("Nellore"),
            CityItem("Dhaka"),
            CityItem("Agra"),
            CityItem("Varanasi")
        )
        document_sign_type_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        document_sign_type_recycler.adapter = BottomSheetMenuAdapter(original, this)

    }

    override fun passResultCallback(message: String) {
        name = message
        RegistrationActivity.edit_city.setText(message)
//        Toast.makeText(
//            AppController.instance.applicationContext,
//            Constatns.CITY_NAME,
//            Toast.LENGTH_LONG
//        ).show()
        dismiss()
    }

}