package com.minding.aidchips

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.inflate
import android.widget.*
import org.json.JSONObject


class ClientActivity : AppCompatActivity(), View.OnClickListener, OnLongClickListener
{
    private val timeOut: Long = 200
    lateinit var backBtn : ActionBar
    lateinit var toolbar: android.support.v7.widget.Toolbar

    private lateinit var dialog: Dialog
    private lateinit var dialogPropierty: EditText
    private lateinit var dialogTel: EditText
    private lateinit var dialogSerial: TextView

    private lateinit var nfcManager: NFCManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        supportFragmentManager.beginTransaction()
            .add(R.id.client_content, HomeFragment.newInstance())
            .commit()
        setTitle(R.string.title_home)

        setupToolbar()
        setupFabs()
        setupDialog()
        nfcManager = NFCManager(this)

        if (!nfcManager.nfcAdapter.isEnabled)
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()

        findViewById<ConstraintLayout>(R.id.curtain).setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        nfcManager.enableForegroundDispatchSystem(this, this, ClientActivity::class.java)
    }

    override fun onPause() {
        super.onPause()

        nfcManager.disableForegroundDispatchSystem(this)
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)
        if (nfcManager.isNfcIntent(intent) && dialog.isShowing)
        {
            if (dialogPropierty.text.toString().isNotBlank()  && dialogTel.text.toString().isNotBlank())
            {
                val basicStructure = JSONObject(
                    """{
    "propietary": {
        "name": "${dialogPropierty.text}",
        "tel": "${dialogTel.text}"
    },
    "emergency data": {
        "full name":"",
        "address":"",
        "health insurance":"",
        "blood": "",
        "allergies":"",
        "medicines":"",
        "donor":"",
        "medial notes":""
    },
    "contact data": {

    },
    "personal data": {

    },
    "files":{

    }
}"""
                )
                println(basicStructure.toString())

                nfcManager.write(intent, basicStructure.toString(), this)
                dialogSerial.text = nfcManager.getNSerial(intent)
            }
            else
                Toast.makeText(this, "Debes llenar ambos campos primero", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.menu_add_chip ->
            {
                dialog.show()
//                startActivity(Intent(this, ReadingActivity::class.java))
//                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            }
            R.id.menu_aidchips_web ->
            {
                startActivity(Intent(this, CameraActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            }
            R.id.menu_add_data ->
            {
                startActivity(Intent(this, WritingActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onClick(v: View)
    {
        when (v.id)
        {
//            Start config to FAB
            R.id.fab_main ->
            {
                if (findViewById<FloatingActionButton>(R.id.fab_permits).visibility == View.VISIBLE)
                {
                    setTitle(R.string.title_home)
                    if (supportFragmentManager.backStackEntryCount > 0)
                        onBackPressed()
                    hideMenu()
                }
                else
                    showMenu()
            }
            R.id.fab_permits ->
            {
                openFragment(PermissionsFragment.newInstance())
                toolbar.menu.clear()
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_permissions)
            }
            R.id.fab_profile ->
            {
                openFragment(ProfileFragment.newInstance())
                toolbar.menu.clear()
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_profile)
            }
            R.id.fab_alerts ->
            {
                openFragment(AlertsFragment.newInstance())
                toolbar.menu.clear()
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                hideMenu()
                setTitle(R.string.title_alerts)
            }
//            End config to FAB
            R.id.curtain -> hideMenu()
//            config dialog
            R.id.closeDialog -> dialog.dismiss()
            R.id.dialog_add_btn_add ->
            {
                if (dialogPropierty.text.toString().isNotBlank()  && dialogTel.text.toString().isNotBlank() && dialogSerial.text.isNotBlank())
                {
                    val params : MutableMap<String, String> = HashMap()
                    params["nserie"] = dialogSerial.text.toString()
                    params["ownName"] = dialogPropierty.text.toString()
                    params["tel"] = dialogTel.text.toString()
                    DataBase().requestOperation(this, DataBase.Action.Add.CHIP, DataBase.Method.POST, params)
                    { result ->
                        when (result)
                        {
                            true ->
                            {
                                val paramsa : MutableMap<String, String> = HashMap()
                                paramsa["id"] = SavedData().getIntSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()
                                paramsa["nserie"] = dialogSerial.text.toString()
                                paramsa["owner"] = "true"

                                DataBase().requestOperation(this, DataBase.Action.Add.PERMIT, DataBase.Method.POST, paramsa)
                                { result1 ->
                                    when (result1)
                                    {
                                        true ->
                                        {
                                            Toast.makeText(this, "Tarjeta Añadida", Toast.LENGTH_SHORT).show()
                                        }
                                        false -> {
                                            Toast.makeText(this, "El chip ya ha sido registradoa otro dueño", Toast.LENGTH_SHORT).show()
                                        }
                                        null -> Toast.makeText(this, "Problema en conexcion con el servidor", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            false ->
                            {
                                Toast.makeText(this, "El chip ya habia registrado previamente", Toast.LENGTH_SHORT).show()
                                val paramsa : MutableMap<String, String> = HashMap()
                                paramsa["id"] = SavedData().getIntSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()
                                paramsa["nserie"] = dialogSerial.text.toString()
                                paramsa["owner"] = "true"

                                DataBase().requestOperation(this, DataBase.Action.Add.PERMIT, DataBase.Method.POST, paramsa)
                                { result2 ->
                                    when (result2)
                                    {
                                        true ->
                                        {
                                            Toast.makeText(this, "Tarjeta Añadida", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                        }
                                        false -> {
                                            Toast.makeText(this, "El chip ya ha sido registrado con un dueño", Toast.LENGTH_SHORT).show()
                                        }
                                        null -> Toast.makeText(this, "Problema en conexcion con el servidor", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            null -> Toast.makeText(this, "Problema en conexcion con el servidor", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                }
                else
                    Toast.makeText(this, "Debes llenar ambos campos primero y luego acercar el chip a registrar", Toast.LENGTH_LONG).show()
            }
//            End config dialog
        }
    }
    override fun onLongClick(v: View): Boolean
    {
        when (v.id)
        {
//            Start config to FAB
            R.id.fab_permits -> showHelp(v)
            R.id.fab_profile -> showHelp(v)
            R.id.fab_alerts -> showHelp(v)
//            End config to FAB
        }
        return true
    }
    override fun onBackPressed()
    {
        setTitle(R.string.title_home)
        backBtn.setDisplayHomeAsUpEnabled(false)
        toolbar.inflateMenu(R.menu.menu_home)
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack(0,0)
        super.onBackPressed()
    }
    private fun openFragment(fragment: Fragment)
    {
        if (fragment !is HomeFragment)
            backBtn.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.client_content, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun getMenusOptions() :ArrayList<FloatingActionButton>
    {
        val arrayList: ArrayList<FloatingActionButton> = ArrayList()

        arrayList.add(findViewById(R.id.fab_permits))
        arrayList.add(findViewById(R.id.fab_profile))
        arrayList.add(findViewById(R.id.fab_alerts))

        return arrayList
    }
    @SuppressLint("RestrictedApi")
    private fun showMenu()
    {
//        showing curtain_out
        findViewById<ConstraintLayout>(R.id.curtain).apply {
            visibility = View.VISIBLE
            ObjectAnimator.ofFloat(this, "alpha", 0.66f).start()
        }
        findViewById<FloatingActionButton>(R.id.fab_main).setImageResource(R.drawable.ic_home)
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
        findViewById<FloatingActionButton>(R.id.fab_main).setImageResource(R.drawable.ic_suspensivedots)
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
        val title:TextView = findViewById(R.id.nextLayout_title)
        val desc :TextView = findViewById(R.id.nextLayout_description)

        if (title.visibility != View.VISIBLE)
        {
            title.visibility = View.VISIBLE
            desc.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(title, "alpha", 1f).start()
            ObjectAnimator.ofFloat(desc, "alpha", 1f).start()
        }

        when (v.id)
        {
            R.id.fab_permits ->
            {
                title.text = getText(R.string.title_permissions)
                desc.text = getText(R.string.description_permissions)
            }
            R.id.fab_profile ->
            {
                title.text = getText(R.string.title_profile)
                desc.text = getText(R.string.description_profile)
            }
            R.id.fab_alerts ->
            {
                title.text = getText(R.string.title_alerts)
                desc.text = getText(R.string.description_alerts)
            }
        }
    }
    private fun hideHelp()
    {
        findViewById<TextView>(R.id.nextLayout_title).also {
            ObjectAnimator.ofFloat(it, "alpha", 0f).apply {
                addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(anim: Animator)
                {
                    it.visibility = View.GONE
                    it.text = null
                } })
                duration = timeOut/2
                start()
            }
        }
        findViewById<TextView>(R.id.nextLayout_description).also {
            ObjectAnimator.ofFloat(it, "alpha", 0f).apply {
                addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(anim: Animator)
                {
                    it.visibility = View.GONE
                    it.text = null
                } })
                duration = timeOut/2
                start()
            }
        }
    }
    @SuppressLint("PrivateResource")
    private fun setupToolbar()
    {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        backBtn = supportActionBar!!
        backBtn.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupFabs()
    {
        findViewById<FloatingActionButton>(R.id.fab_main).setOnClickListener(this)

        val fabPermissions: FloatingActionButton = findViewById(R.id.fab_permits)
        val fabProfile: FloatingActionButton = findViewById(R.id.fab_profile)
        val fabAlerts: FloatingActionButton = findViewById(R.id.fab_alerts)

        fabPermissions.setOnClickListener(this)
        fabProfile.setOnClickListener(this)
        fabAlerts.setOnClickListener(this)

        fabPermissions.setOnLongClickListener(this)
        fabProfile.setOnLongClickListener(this)
        fabAlerts.setOnLongClickListener(this)
    }
    private fun setupDialog()
    {
        val dialogView: View = inflate(this, R.layout.dialog_add_chip, null)

        dialogView.findViewById<ImageButton>(R.id.closeDialog).setOnClickListener(this)
        dialogView.findViewById<Button>(R.id.dialog_add_btn_add).setOnClickListener(this)

        dialogPropierty = dialogView.findViewById(R.id.dialog_add_field_propierty)
        dialogTel = dialogView.findViewById(R.id.dialog_add_field_tel)
        dialogSerial = dialogView.findViewById(R.id.dialog_add_serial)

        val ctw = ContextThemeWrapper(this, R.style.Main)
        val builder = AlertDialog.Builder(ctw)
        builder.setView(dialogView)
        dialog = builder.create()
    }
}