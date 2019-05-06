package com.minding.aidchips

import android.animation.ObjectAnimator
import android.support.annotation.NonNull
import android.util.Property
import android.view.View
import android.view.ViewTreeObserver

class CardViewCollapsible(@NonNull val viewUwU: View)
{
    private var compactHeight = 1
    internal var expandedHeight = 0

    init
    {
        viewUwU.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener
            {
                override fun onPreDraw() =
                    true.also {
                        viewUwU.viewTreeObserver.removeOnPreDrawListener(this)
                        expandedHeight = viewUwU.height
                        collapseViewQuickly()
                    }
            })
    }

    private fun collapseViewQuickly() =
        ObjectAnimator.ofInt(viewUwU, HeightProperty(), compactHeight).setDuration(ObjectAnimator().duration/10).start()
    private fun collapseView() =
        ObjectAnimator.ofInt(viewUwU, HeightProperty(), compactHeight).start()
    private fun expandView(height: Int) =
        ObjectAnimator.ofInt(viewUwU, HeightProperty(), height).start()
    fun toggle() =
        if (viewUwU.height == compactHeight)
            expandView(expandedHeight)
        else
            collapseView()
}
fun View.buildCollapsible(): CardViewCollapsible =
    CardViewCollapsible(this)

internal class HeightProperty : Property<View, Int>(Int::class.java, "height")
{
    override fun get(`object`: View): Int = `object`.height
    override fun set(`object`: View, value: Int)
    {
        `object`.layoutParams = `object`.layoutParams.apply { height = value }
    }
}