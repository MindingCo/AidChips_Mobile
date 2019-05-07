package com.minding.aidchips

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.TextView

class ClientActivity : AppCompatActivity(), View.OnClickListener, OnLongClickListener
{
    private val timeOut: Long = 200
    lateinit var backBtn : ActionBar

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        if (savedInstanceState == null)
        {
//            Toast.makeText(this,"savedInstanceState == null", Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, HomeFragment.newInstance())
                .commit()
            setTitle(R.string.title_home)
        }
            //        Start config to FAB
        findViewById<FloatingActionButton>(R.id.fabMain).setOnClickListener(this)

        val fabPermissions: FloatingActionButton = findViewById(R.id.fabPermissions)
        val fabProfile: FloatingActionButton = findViewById(R.id.fabProfile)
        val fabAlerts: FloatingActionButton = findViewById(R.id.fabAlerts)

        fabPermissions.setOnClickListener(this)
        fabProfile.setOnClickListener(this)
        fabAlerts.setOnClickListener(this)

        fabPermissions.setOnLongClickListener(this)
        fabProfile.setOnLongClickListener(this)
        fabAlerts.setOnLongClickListener(this)
//        End config to FAB
        findViewById<ConstraintLayout>(R.id.curtain).setOnClickListener(this)

        setToolbar()
    }
    @SuppressLint("RestrictedApi")
    override fun onClick(v: View)
    {
        when (v.id)
        {
//            Start config to FAB
            R.id.fabMain ->
            {
                if (findViewById<FloatingActionButton>(R.id.fabPermissions).visibility == View.VISIBLE)
                {
                    setTitle(R.string.title_home)
                    if (supportFragmentManager.backStackEntryCount > 0)
                        onBackPressed()
                    hideMenu()
                }
                else
                    showMenu()
            }
            R.id.fabPermissions ->
            {
                openFragment(PermissionsFragment.newInstance())
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_permissions)
            }
            R.id.fabProfile ->
            {
                openFragment(ProfileFragment.newInstance())
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_profile)
            }
            R.id.fabAlerts ->
            {
                openFragment(AlertsFragment.newInstance())
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_alerts)
            }
//            End config to FAB
            R.id.curtain -> hideMenu()
        }
    }
    override fun onLongClick(v: View): Boolean
    {
        when (v.id)
        {
//            Start config to FAB
            R.id.fabPermissions -> showHelp(v)
            R.id.fabProfile -> showHelp(v)
            R.id.fabAlerts -> showHelp(v)
//            End config to FAB
        }
        return true
    }
    override fun onBackPressed()
    {
        setTitle(R.string.title_home)
        backBtn.setDisplayHomeAsUpEnabled(false)
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack(0,0)
        super.onBackPressed()
    }
    private fun openFragment(fragment: Fragment)
    {
        if (fragment !is HomeFragment)
            backBtn.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun getMenusOptions() :ArrayList<FloatingActionButton>
    {
        val arrayList: ArrayList<FloatingActionButton> = ArrayList()

        arrayList.add(findViewById(R.id.fabPermissions))
        arrayList.add(findViewById(R.id.fabProfile))
        arrayList.add(findViewById(R.id.fabAlerts))

        return arrayList
    }
    @SuppressLint("RestrictedApi")
    private fun showMenu()
    {
//        showing curtain_out
        findViewById<ConstraintLayout>(R.id.curtain).also {
            it.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(it, "alpha", 0.66f).start()
        }
//        showing options
        val options: ArrayList<FloatingActionButton> = getMenusOptions()
        getMenusOptions().forEach { it.visibility = View.VISIBLE }

        SpringAnimation(options[0], DynamicAnimation.TRANSLATION_X).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)).animateToFinalPosition(-300f)
        SpringAnimation(options[1], DynamicAnimation.TRANSLATION_X).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)).animateToFinalPosition(-225f)
        SpringAnimation(options[1], DynamicAnimation.TRANSLATION_Y).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)).animateToFinalPosition(-225f)
        SpringAnimation(options[2], DynamicAnimation.TRANSLATION_Y).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)).animateToFinalPosition(-300f)
    }
    @SuppressLint("RestrictedApi")
    private fun hideMenu()
    {
        hideHelp()
//        hiding curtain_out
        findViewById<ConstraintLayout>(R.id.curtain).also {
            ObjectAnimator.ofFloat(it, "alpha", 0.0f).apply {
                addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(anim: Animator) { it.visibility = View.GONE } })
                duration = timeOut
                start()
            }
            it.visibility = View.GONE
        }
//        hiding options
        val options: ArrayList<FloatingActionButton> = getMenusOptions()

        SpringAnimation(options[0], DynamicAnimation.TRANSLATION_X).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_MEDIUM).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)).animateToFinalPosition(0f)
        SpringAnimation(options[1], DynamicAnimation.TRANSLATION_X).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_MEDIUM).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)).animateToFinalPosition(0f)
        SpringAnimation(options[1], DynamicAnimation.TRANSLATION_Y).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_MEDIUM).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)).animateToFinalPosition(0f)
        val lastanim = SpringAnimation(options[2], DynamicAnimation.TRANSLATION_Y).setSpring(
            SpringForce() .setStiffness( SpringForce.STIFFNESS_MEDIUM).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY))

        lastanim.addEndListener { _, _, _, _ -> getMenusOptions().forEach { it.visibility = View.GONE } }
        lastanim.animateToFinalPosition(0f)
    }
    private fun showHelp(v: View)
    {
        val title:TextView = findViewById(R.id.TitleNextLayout)
        val desc :TextView = findViewById(R.id.DescriptionNextLayout)

        if (title.visibility != View.VISIBLE)
        {
            title.visibility = View.VISIBLE
            desc.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(title, "alpha", 1f).start()
            ObjectAnimator.ofFloat(desc, "alpha", 1f).start()
        }

        when (v.id)
        {
            R.id.fabPermissions ->
            {
                title.text = getText(R.string.title_permissions)
                desc.text = getText(R.string.description_permissions)
            }
            R.id.fabProfile ->
            {
                title.text = getText(R.string.title_profile)
                desc.text = getText(R.string.description_profile)
            }
            R.id.fabAlerts ->
            {
                title.text = getText(R.string.title_alerts)
                desc.text = getText(R.string.description_alerts)
            }
        }
    }
    private fun hideHelp()
    {
        findViewById<TextView>(R.id.TitleNextLayout).also {
            ObjectAnimator.ofFloat(it, "alpha", 0f).apply {
                addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(anim: Animator) {
                    it.visibility = View.GONE
                    it.text = null
                } })
                duration = timeOut/2
                start()
            }

        }
        findViewById<TextView>(R.id.DescriptionNextLayout).also {
            ObjectAnimator.ofFloat(it, "alpha", 0f).apply {
                addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(anim: Animator) {
                    it.visibility = View.GONE
                    it.text = null
                } })
                duration = timeOut/2
                start()
            }
        }
    }
    @SuppressLint("PrivateResource")
    private fun setToolbar()
    {
        val toolbar: android.support.v7.widget.Toolbar  = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        backBtn = supportActionBar!!
        backBtn.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
//        backBtn.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed() }
    }
}