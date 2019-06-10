package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import com.minding.aidchips.AlertsAdapter.Notif
import com.minding.aidchips.ui.home.ClientViewModel

class AlertsFragment : Fragment()
{
    companion object { fun newInstance() = AlertsFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_given_permissions, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)


        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()

        DataBase().requestArrayJSON(view!!.context, DataBase.Action.Get.NOTIF, DataBase.Method.POST, params)
        { notifsJSONArray ->
            if (notifsJSONArray != null)
            {
                val notifs: ArrayList<Notif> = ArrayList()

                var i = 0
                while (i < notifsJSONArray.length())
                {
                    val notif = notifsJSONArray.getJSONObject(i)
                    notifs.add( Notif( User(notif.getInt("id_usu"), notif.getString("nom_usu"), notif.getString("ema_usu"), notif.getString("tel_usu"), null, null), if (notif.getString("tip_not") == "null") null else notif.getInt("tip_not"), notif.getString("fec_not"), notif.getString("nse_chp")) )
                    i++
                }
                view!!.findViewById<ListView>(R.id.list_given_permissions).adapter = AlertsAdapter(notifs)
            }
            else adviseNoNotifs()
            view!!.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
        }

//        val permissions: ArrayList<Notif> = ArrayList()
//
//        permissions.add(Notif(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
//        permissions.add(Notif(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
//        permissions.add(Notif(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
//        permissions.add(Notif(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),null ,"19 septiempre 2019", 123124, true))
    }

    private fun adviseNoNotifs() {
        view!!.findViewById<ListView>(R.id.list_given_permissions).background = view!!.resources.getDrawable(R.drawable.background_no_alerts,null)
    }
}