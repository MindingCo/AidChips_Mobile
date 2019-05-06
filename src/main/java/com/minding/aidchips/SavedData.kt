package com.minding.aidchips

import android.content.Context

class SavedData
{
    interface NameGroup
    {
        companion object
        {
            val SESSION = "session"
            val SETTINGS = "settings"
        }
    }
    interface Elements
    {
        interface Session
        {
            companion object
            {
                val LOGED = "loged"
                val GOOGLESIGNIN = "google-signin"
                val ID = "id"
                val NAME = "name"
                val TEL = "phone"
                val EMAIL = "email"
            }
        }
        //        interface Settings {
//            companion object {
//
//            }
//        }
    }
    fun setSavedData(ctx: Context, nameGroup: String, element: String, value: String) =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).edit().apply {
            putString(element, value)
            apply()
        }
    fun setSavedData(ctx: Context, nameGroup: String, element: String, value: Boolean) =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).edit().apply {
            putBoolean(element, value)
            apply()
        }
    fun setSavedData(ctx: Context, nameGroup: String, element: String, value: Int) =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).edit().apply {
            putInt(element, value)
            apply()
        }
    fun setSavedData(ctx: Context, nameGroup: String, element: String, value: Long) =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).edit().apply {
            putLong(element, value)
            apply()
        }
    fun setSavedData(ctx: Context, nameGroup: String, element: String, value: Float) =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).edit().apply {
            putFloat(element, value)
            apply()
        }
    fun getStringSavedData(ctx: Context, nameGroup: String, element: String): String? =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).getString(element, null)
    fun getBooleanSavedData(ctx: Context, nameGroup: String, element: String): Boolean =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).getBoolean(element, false)
    fun getIntSavedData(ctx: Context, nameGroup: String, element: String): Int =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).getInt(element, 0)
    fun getLongSavedData(ctx: Context, nameGroup: String, element: String): Long =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).getLong(element, 0)
    fun getFloatSavedData(ctx: Context, nameGroup: String, element: String): Float =
        ctx.getSharedPreferences(nameGroup, Context.MODE_PRIVATE).getFloat(element, 0f)
}