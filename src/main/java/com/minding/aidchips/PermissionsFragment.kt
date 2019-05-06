package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minding.aidchips.ui.home.ClientViewModel

class PermissionsFragment : Fragment()
{
    companion object { fun newInstance() = PermissionsFragment() }

    private lateinit var viewModel: ClientViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View {
        return inflater.inflate(R.layout.fragment_permissions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        viewPager = view!!.findViewById(R.id.viewPager)
        setupViewPager(viewPager)

        tabs = view!!.findViewById(R.id.tabs)

        tabs.apply {
            this.addTab(this.newTab().setText(R.string.given))
            this.addTab(this.newTab().setText(R.string.received))
            this.tabMode = TabLayout.MODE_FIXED
            this.setupWithViewPager(viewPager)
        }

    }
    private fun setupViewPager(viewPager: ViewPager)
    {
        val adapter = SectionPagerAdapter(childFragmentManager)
        adapter.addFragment(GivenPermissionsFragment.newInstance(), getString(R.string.given))
        adapter.addFragment(ReceivedPermissionsFragment.newInstance(), getString(R.string.received))
        viewPager.adapter = adapter
    }
    class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
    {
        private var fragments: ArrayList<Fragment> = ArrayList()
        private var fragmentsTitles: ArrayList<String> = ArrayList()

        override fun getItem(idx: Int): Fragment =
            fragments[idx]
        override fun getCount(): Int =
            fragments.size
        override fun getPageTitle(position: Int): CharSequence? =
            fragmentsTitles[position]
        fun addFragment(fragment: Fragment, title: String)
        {
            fragments.add(fragment)
            fragmentsTitles.add(title)
        }
    }
}