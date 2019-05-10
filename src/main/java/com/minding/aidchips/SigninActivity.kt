package com.minding.aidchips

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SigninActivity : AppCompatActivity(), OnClickListener
{
    private val IP = "http://192.168.1.64"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        findViewById<Button>(R.id.signin_btn_switch).setOnClickListener(this)
        findViewById<Button>(R.id.signin_btn_signin ).setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        when (v.id)
        {
            R.id.signin_btn_switch ->
            {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                finish()
            }
            R.id.signin_btn_signin ->
            {
                val name: String = findViewById<EditText>(R.id.signin_edit_name).text.toString()
                val pass: String = findViewById<EditText>(R.id.signin_edit_pass).text.toString()
                val passCo: String = findViewById<EditText>(R.id.signin_edit_passConfirm).text.toString()
                val email : String = findViewById<EditText>(R.id.signin_edit_email).text.toString()
                val tel : String = findViewById<EditText>(R.id.signin_edit_phone).text.toString()
                when
                {
                    tel.isBlank() || email.isBlank() || passCo.isBlank() || pass.isBlank() || name.isBlank() -> { adviseEmptyField()}
                    pass != passCo -> adviseDifferentPasswords()
                    else ->
                    {
                        val params : MutableMap<String, String> = HashMap()
                        params["name"] = name
                        params["email"] = email
                        params["tel"] = tel
                        params["pass"] = pass
                        DataBase().requestOperation(this, DataBase.Action.Add.USER, DataBase.Method.POST, params)
                        { result ->
                            when (result)
                            {
                                true ->
                                {
                                    val paramsa : MutableMap<String, String> = HashMap()
                                    paramsa["name"] = name

                                    DataBase().requestJSON(this, DataBase.Action.Get.USER, DataBase.Method.POST, params)
                                    { userJSON ->
                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.LOGED, true)
                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.GOOGLESIGNIN, false)

                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID, userJSON!!.getInt("id_usu"))
                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.NAME, userJSON.getString("nmc_usu"))
                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.EMAIL, userJSON.getString("ema_usu"))
                                        SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.TEL, userJSON.getString("cel_usu"))

                                        startActivity(Intent(this, ClientActivity::class.java))
                                        overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                                        finish()
                                    }
                                }
                                false -> problemAlreadyExitsUser()
                                null -> Toast.makeText(this, "Problema en conexcion con el servidor", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

    }
    private fun problemAlreadyExitsUser() {
        findViewById<TextView>(R.id.signin_advise).text = resources.getText(R.string.problem_existent_user)
    }
    private fun adviseDifferentPasswords() {
        findViewById<TextView>(R.id.signin_advise).text = resources.getText(R.string.advise_different_passwords)
    }
    private fun adviseEmptyField() {
        findViewById<TextView>(R.id.signin_advise).text = resources.getText(R.string.advise_empty_field)
    }
}
