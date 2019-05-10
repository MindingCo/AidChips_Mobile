package com.minding.aidchips

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.OptionalPendingResult
import com.google.android.gms.common.api.ResultCallback
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity(), OnClickListener, GoogleApiClient.OnConnectionFailedListener
{
    private lateinit var apiClient: GoogleApiClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_btn_switch).setOnClickListener(this)
        findViewById<Button>(R.id.login_btn_login).setOnClickListener(this)
        findViewById<com.google.android.gms.common.SignInButton>(R.id.login_btn_auth_google).setOnClickListener(this)

//        Api Client
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        apiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }
    override fun onStart()
    {
        super.onStart()

        val opr: OptionalPendingResult<GoogleSignInResult> = Auth.GoogleSignInApi.silentSignIn(apiClient)
        if (opr.isDone)
            handleLoginResult(opr.get())
        else
            opr.setResultCallback { ResultCallback<GoogleSignInResult>
            {
                handleLoginResult(it)
            } }
    }
    override fun onClick(v: View)
    {
        when (v.id)
        {
            R.id.login_btn_switch ->
            {
                startActivity(Intent(this, SigninActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                finish()
            }
            R.id.login_btn_login ->
            {
                val name: String = findViewById<EditText>(R.id.login_edit_name).text.toString()
                val pass: String = findViewById<EditText>(R.id.login_edit_pass).text.toString()
                when
                {
                    pass.isBlank() || name.isBlank() -> { adviseEmptyField()}
                    else ->
                    {
                        val params : MutableMap<String, String> = HashMap()
                        params["name"] = name
                        params["pass"] = pass
                        DataBase().requestJSON(this, DataBase.Action.LOGIN, DataBase.Method.POST, params)
                        { userJSON ->
                            if (userJSON != null)
                            {
                                if (userJSON.getString("pas_usu") == pass)
                                {
                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.LOGED, true)
                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.GOOGLESIGNIN, false)

                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID, userJSON.getInt("id_usu"))
                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.NAME, userJSON.getString("nmc_usu"))
                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.EMAIL, userJSON.getString("ema_usu"))
                                    SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.TEL, userJSON.getString("cel_usu"))

                                    startActivity(Intent(this, ClientActivity::class.java))
                                    overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                                    finish()
                                }
                                else problemWrongPass()
                            }
                            else problemNoExistsUser()
                        }
                    }
                }
            }
            R.id.login_btn_auth_google -> Toast.makeText(this, "Funcion inhabilitada por el memento", Toast.LENGTH_SHORT).show()
            //startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(apiClient), RC_SIGN_IN)
        }

    }
    override fun onConnectionFailed(p0: ConnectionResult)
    {
        Toast.makeText(this,"Error de Coneccion",Toast.LENGTH_SHORT).show()
        Log.e("GoogleSignIn", "On Connection Failed")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
            handleLoginResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data))
    }
    private fun handleLoginResult(result: GoogleSignInResult)
    {
        if (result.isSuccess)
        {
//            Aqui se muestran los datos obtenidos
//            Toast.makeText(this, result.signInAccount!!.displayName, Toast.LENGTH_SHORT).show()
//            Toast.makeText(this,result.signInAccount!!.email, Toast.LENGTH_SHORT).show()
            SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.GOOGLESIGNIN, false)
            startActivity(Intent(this, ClientActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            finish()
        }
    }
    private fun problemAlreadyExitsUser() {
        findViewById<TextView>(R.id.login_advise).text = resources.getText(R.string.problem_existent_user)
    }
    private fun problemNoExistsUser() {
        findViewById<TextView>(R.id.login_advise).text = resources.getText(R.string.problem_no_exists_user)
    }
    private fun problemWrongPass() {
        findViewById<TextView>(R.id.login_advise).text = resources.getText(R.string.problem_wrong_password)
    }
    private fun adviseDifferentPasswords() {
        findViewById<TextView>(R.id.login_advise).text = resources.getText(R.string.advise_different_passwords)
    }
    private fun adviseEmptyField() {
        findViewById<TextView>(R.id.login_advise).text = resources.getText(R.string.advise_empty_field)
    }
}