
package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ProgressBar
import com.minding.aidchips.HomeAdapter.Chip
import com.minding.aidchips.ui.home.ClientViewModel

class HomeFragment : Fragment(), OnClickListener
{
    companion object { fun newInstance() = HomeFragment() }

    private lateinit var viewModel: ClientViewModel
//    private lateinit var dialog: Dialog

    override fun onCreateView
    (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
    : View { return inflater.inflate(R.layout.fragment_home, container, false) }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()

        DataBase().requestArrayJSON(view!!.context, DataBase.Action.Get.CHIPS, DataBase.Method.POST, params)
        { chipsJSONArray ->
            if (chipsJSONArray != null)
            {
                val chips: ArrayList<Chip> = ArrayList()

                var i = 0
                while (i < chipsJSONArray.length())
                {
                    val chip = chipsJSONArray.getJSONObject(i)
                    chips.add(Chip(chip.getString("nse_chp"), chip.getString("por_chp"), chip.getString("tel_chp")))
                    i++
                }
//                chips.add(Chip("AAA003", "Patricia", "12312312"))
                view!!.findViewById<RecyclerView>(R.id.home).adapter = HomeAdapter(chips)
                view!!.findViewById<RecyclerView>(R.id.home).layoutManager = GridLayoutManager(view!!.context, 1)
            }
            else adviseNoChips()
            view!!.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
        }
//        val chips: ArrayList<Chip> = ArrayList()
//        chips.add(Chip("AAA003", "Patricia", "12312312"))
//        view!!.findViewById<RecyclerView>(R.id.home).adapter = HomeAdapter(chips)
//        view!!.findViewById<RecyclerView>(R.id.home).layoutManager = GridLayoutManager(view!!.context, 1)
    }

    private fun adviseNoChips() {
        view!!.findViewById<RecyclerView>(R.id.home).background = view!!.resources.getDrawable(R.drawable.background_no_chips,null)
    }

    override fun onClick(v: View) =
        when (v.id)
        {
//            R.id.btnRChipLikeProperty -> {}
//            R.id.btnRChip -> showDialog()
//            R.id.closeDialog -> dialog.dismiss()
//            R.id.btn_request_permission -> { Toast.makeText(context, "Me clicleaste UwU", Toast.LENGTH_SHORT).show() }
//            R.id.btn_emergency -> { Toast.makeText(context, "Me clicleaste UnU", Toast.LENGTH_SHORT).show() }
            else -> {}
        }
}
