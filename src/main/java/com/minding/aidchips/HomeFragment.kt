
package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
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

        val chips: ArrayList<Chip> = ArrayList()
        chips.add(Chip(123, "Gustavo A", "1234567890"))
        chips.add(Chip(234, "Gustavo Peduzzi", "12367890"))
        chips.add(Chip(4564, "Patricia Acevedo", "12345671230"))
        chips.add(Chip(125756, "luis", "123456789123"))
//        chips.add(Chip(1213, "Gustavo", "123456789asd"))

        view!!.findViewById<RecyclerView>(R.id.home).adapter = HomeAdapter(chips)
        view!!.findViewById<RecyclerView>(R.id.home).layoutManager = GridLayoutManager(view!!.context, 1)

//        view!!.findViewById<Button>(R.id.btnRChipLikeProperty).setOnClickListener(this)
//        view!!.findViewById<Button>(R.id.btnRChip).setOnClickListener(this)
    }

//    @SuppressLint("InflateParams", "ResourceAsColor")
//    private fun showDialog()
//    {
//        val dialogView: View = inflate(context, R.layout.dialog_rchip_nopermission, null)
//
//        dialogView.findViewById<ImageButton>(R.id.closeDialog).setOnClickListener(this)
//        dialogView.findViewById<Button>(R.id.btn_request_permission).setOnClickListener(this)
//        dialogView.findViewById<Button>(R.id.btn_emergency).setOnClickListener(this)
//
//        val ctw = ContextThemeWrapper(context, R.style.Main)
//        val builder = AlertDialog.Builder(ctw)
//        builder.setView(dialogView)
//        dialog = builder.create()
//
//        dialog.show()
//    }
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
