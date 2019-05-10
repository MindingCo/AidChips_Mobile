package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.minding.aidchips.ui.home.ClientViewModel

class GivenPermissionsFragment : Fragment()
{
    companion object { fun newInstance() = GivenPermissionsFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_given_permissions, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()

        DataBase().requestArrayJSON(view!!.context, DataBase.Action.Get.GIVENP, DataBase.Method.POST, params)
        { chipsJSONArray ->
            if (chipsJSONArray != null)
            {
                val permits: ArrayList<Permit> = ArrayList()

                var i = 0
                while (i < chipsJSONArray.length())
                {
                    val chip = chipsJSONArray.getJSONObject(i)
                    permits.add( Permit( chip.getString("nse_chp"), chip.getInt("id_usu"), chip.getString("npr_chp"), chip.getString("cel_chp"),  null) )
                    i++
                }
                view!!.findViewById<ListView>(R.id.list_given_permissions).adapter = GivenPermissionsAdapter(permits)
            }
            else adviseNoGivenPermits()
        }
    }

    private fun adviseNoGivenPermits() {
        view!!.findViewById<RecyclerView>(R.id.home).background = view!!.resources.getDrawable(R.drawable.background_no_given_permits,null)
    }
}