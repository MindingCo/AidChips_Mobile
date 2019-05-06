package com.minding.aidchips

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.NonNull

class User (val id: Int?, @NonNull val nombre: String, val email :String?, @NonNull val tel:String, var image: Bitmap? , val pass: String?) {
    constructor(id: Int?, @NonNull nombre: String, email: String?, @NonNull tel :String, imageDra: Int, context: Context,pass: String?): this(id, nombre, email, tel, null, pass) {
        image = BitmapFactory.decodeResource(context.resources, imageDra)
    }
//    private val preimage = binaryImage!!.getBytes(1, binaryImage.length().toInt())
//    var image: Bitmap = BitmapFactory.decodeByteArray(preimage, 0, preimage.size)
}