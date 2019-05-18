package com.minding.aidchips

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.ContextThemeWrapper
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import org.json.JSONObject

class ReadingActivity : AppCompatActivity(), OnClickListener
{
    private lateinit var nfcManager: NFCManager
    lateinit var backBtn : ActionBar
    lateinit var toolbar: android.support.v7.widget.Toolbar
    private lateinit var dialog: Dialog
    private lateinit var currenttIntent: Intent

    interface TypeOfPermit {
        companion object {
            const val EMERGENCY = 2
            const val INVITED = 1
            const val OWNER = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        setTitle(R.string.read_data)

        setupDialog()
        setupToolbar()

        // Check for available NFC Adapter
        nfcManager = NFCManager(this)
        if (!nfcManager.nfcAdapter.isEnabled)
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()
        if (nfcManager.isNfcIntent(intent))
            showData(intent)

    }
    override fun onResume() {
        super.onResume()
        nfcManager.enableForegroundDispatchSystem(this, this, ReadingActivity::class.java)
    }

    override fun onPause() {
        super.onPause()

        nfcManager.disableForegroundDispatchSystem(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (nfcManager.isNfcIntent(intent))
            showData(intent)
    }

    @SuppressLint("PrivateResource")
    private fun setupToolbar()
    {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        backBtn = supportActionBar!!
        backBtn.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        backBtn.setDisplayHomeAsUpEnabled(true)
    }

    private fun showData(intent: Intent)
    {
        typeOfPermit(nfcManager.getNSerial(intent))
        { type ->
            Toast.makeText(this, type.toString(), Toast.LENGTH_SHORT).show()

            val chip = JSONObject(nfcManager.read(intent))

            when (type)
            {
                TypeOfPermit.EMERGENCY ->
                {
                    currenttIntent = intent
                    dialog.show()
                }
                TypeOfPermit.INVITED ->
                {
                    createAlert(type)
                    showPropertyData(chip.getJSONObject("propietary"))
                    showContactData(chip.getJSONObject("contact data"))
                    showEmergencyData(chip.getJSONObject("emergency data"))
                    showPersonalData(chip.getJSONObject("personal data"))
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_propiertyData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_contactData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_emergencyData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_personalData).visibility = View.VISIBLE
                }
                TypeOfPermit.OWNER ->
                {
                    showPropertyData(chip.getJSONObject("propietary"))
                    showContactData(chip.getJSONObject("contact data"))
                    showEmergencyData(chip.getJSONObject("emergency data"))
                    showPersonalData(chip.getJSONObject("personal data"))
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_propiertyData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_contactData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_emergencyData).visibility = View.VISIBLE
                    findViewById<android.support.design.card.MaterialCardView>(R.id.reading_personalData).visibility = View.VISIBLE

                }
            }
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
        }
    }

    private fun typeOfPermit(nserial: String, callback: (result: Int) -> Unit )
    {
        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()
        params["nserie"] = nserial

        DataBase().requestOperation(this, DataBase.Action.TYPEPERMIT, DataBase.Method.POST, params)
        { result ->
            callback( when (result)
            {
                true -> TypeOfPermit.OWNER
                false -> TypeOfPermit.INVITED
                else -> TypeOfPermit.EMERGENCY
            })
        }
    }

    private fun showPropertyData(property: JSONObject)
    {
        findViewById<TextView>(R.id.reading_field_propierty).text = property.getString("name")
        findViewById<TextView>(R.id.reading_field_tel).text = property.getString("tel")
    }
    private fun showEmergencyData(emergencyData: JSONObject)
    {
        findViewById<TextView>(R.id.reading_field_name).text = if (emergencyData.has("full name")) emergencyData.getString("full name") else "-"
        findViewById<TextView>(R.id.reading_field_address).text = if (emergencyData.has("address")) emergencyData.getString("address") else "-"
        findViewById<TextView>(R.id.reading_field_healthInsurance).text = if (emergencyData.has("health insurance")) emergencyData.getString("health insurance") else "-"
        findViewById<TextView>(R.id.reading_field_blood).text = if (emergencyData.has("blood")) emergencyData.getString("blood") else "-"
        findViewById<TextView>(R.id.reading_field_allergies).text = if (emergencyData.has("allergies")) emergencyData.getString("allergies") else "-"
        findViewById<TextView>(R.id.reading_field_medicines).text = if (emergencyData.has("medicines")) emergencyData.getString("medicines") else "-"
        findViewById<TextView>(R.id.reading_field_donor).text = if (emergencyData.has("donor")) emergencyData.getString("donor") else "-"
        findViewById<TextView>(R.id.reading_field_medicalNotes).text = if (emergencyData.has("medial notes")) emergencyData.getString("medial notes") else "-"
    }
    private fun showContactData(emergencyData: JSONObject)
    {

    }
    private fun showPersonalData(emergencyData: JSONObject)
    {

    }

    private fun setupDialog()
    {
        val dialogView: View = View.inflate(this, R.layout.dialog_rchip_nopermission, null)

        dialogView.findViewById<ImageButton>(R.id.closeDialog).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<Button>(R.id.btn_request_permission).setOnClickListener(this)
        dialogView.findViewById<Button>(R.id.btn_emergency).setOnClickListener(this)

        val ctw = ContextThemeWrapper(this, R.style.Main)
        val builder = AlertDialog.Builder(ctw)
        builder.setView(dialogView)
        dialog = builder.create()
    }

    private fun createAlert(type: Int)
    {
        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()
        params["nserie"] = nfcManager.getNSerial(intent)
        params["type"] = if (type == TypeOfPermit.EMERGENCY) "1" else "0"

        DataBase().requestOperation(this, DataBase.Action.Add.ALERT, DataBase.Method.POST, params)
        { result ->
            when (result)
            {
                true -> Toast.makeText(this, "Notificacion creada", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "algo falló", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View) = when (v.id)
    {
        R.id.btn_request_permission ->
        {
//            dialog.dismiss()
        }
        R.id.btn_emergency ->
        {
            createAlert(TypeOfPermit.EMERGENCY)
            val chip = JSONObject(nfcManager.read(intent))
            showPropertyData(chip.getJSONObject("propietary"))
            showContactData(chip.getJSONObject("contact data"))
            showEmergencyData(chip.getJSONObject("emergency data"))
            dialog.dismiss()
            findViewById<android.support.design.card.MaterialCardView>(R.id.reading_propiertyData).visibility = View.VISIBLE
            findViewById<android.support.design.card.MaterialCardView>(R.id.reading_contactData).visibility = View.VISIBLE
            findViewById<android.support.design.card.MaterialCardView>(R.id.reading_emergencyData).visibility = View.VISIBLE
        }
        else -> {}
    }
}