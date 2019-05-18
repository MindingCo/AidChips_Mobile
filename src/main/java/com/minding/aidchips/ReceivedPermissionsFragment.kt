package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import com.minding.aidchips.ui.home.ClientViewModel

class ReceivedPermissionsFragment : Fragment()
{
    companion object { fun newInstance() = ReceivedPermissionsFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_received_permissions, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()

        DataBase().requestArrayJSON(view!!.context, DataBase.Action.Get.RECEIVEDP, DataBase.Method.POST, params)
        { chipsJSONArray ->
            if (chipsJSONArray != null)
            {
                val chipsJSONArray2 =  chipsJSONArray.getJSONArray(0)
                val permits: ArrayList<Permit> = ArrayList()

                var i = 0
                while (i < chipsJSONArray2.length())
                {
                    val chip = chipsJSONArray2.getJSONObject(i)
                    permits.add( Permit( chip.getString("nse_chp"), if (chip.has("id_usu")) chip.get("id_usu") as Int else 0, chip.getString("npr_chp"), chip.getString("cel_chp"),  null) )
                    i++
                }
                view!!.findViewById<ListView>(R.id.list_received_permissions).adapter = ReceivedPermissionsAdapter(permits)
            }
            else adviseNoReceivedPermits()
            view!!.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
        }
    }

    private fun adviseNoReceivedPermits() {
        view!!.findViewById<ListView>(R.id.list_received_permissions).background  = view!!.resources.getDrawable(R.drawable.background_no_received_permits,null)
    }
}