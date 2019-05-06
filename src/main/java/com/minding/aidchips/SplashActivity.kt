package com.minding.aidchips

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.OptionalPendingResult

class SplashActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener
{
    private lateinit var apiClient: GoogleApiClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?)
    {
        this.setTheme(R.style.Splash_Post)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (SavedData().getBooleanSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.LOGED))
        {
            startActivity(Intent(this, ClientActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            finish()
        }

        //        Api Client
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        apiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun onResume()
    {
        this.setTheme(R.style.Splash_Post)
        super.onResume()

        val opr: OptionalPendingResult<GoogleSignInResult> = Auth.GoogleSignInApi.silentSignIn(apiClient)
//        catching a previous login
        if (opr.isDone)
            handleLoginResult(opr.get())
        else
        {
            val icon: ImageView = findViewById(R.id.logo)
            val (animX, animY) = SpringAnimation(icon, DynamicAnimation.X).setStartVelocity(0.25f).setSpring(SpringForce().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)) to SpringAnimation(icon, DynamicAnimation.Y).setStartVelocity(0.1f).setSpring(SpringForce().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY))

            animY.addEndListener { _, _, _, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                finish()
            }
            Handler().postDelayed(
            {
                animX.animateToFinalPosition(53f)
                Handler().postDelayed(
                {
                    animY.animateToFinalPosition(78f)
                }, 100)
            }, 1000)
        }
    }
    override fun onConnectionFailed(p0: ConnectionResult)
    {
        Toast.makeText(this,"Error de Coneccion", Toast.LENGTH_SHORT).show()
        Log.e("GoogleSignIn", "On Connection Failed")
    }
    private fun handleLoginResult(result: GoogleSignInResult)
    {
        if (result.isSuccess)
        {
            //            if Get a successful login
            startActivity(Intent(this, ClientActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.nothing)
            finish()
        }
        else
        {
            val icon: ImageView = findViewById(R.id.logo)
            val (animX, animY) = SpringAnimation(icon, DynamicAnimation.X).setStartVelocity(0.25f).setSpring(SpringForce().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)) to SpringAnimation(icon, DynamicAnimation.Y).setStartVelocity(0.1f).setSpring(SpringForce().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY))

            animY.addEndListener { _, _, _, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                finish()
            }
            Handler().postDelayed(
                {
                    animX.animateToFinalPosition(53f)
                    Handler().postDelayed(
                        {
                            animY.animateToFinalPosition(78f)
                        }, 100)
                }, 1000)
        }
    }
}