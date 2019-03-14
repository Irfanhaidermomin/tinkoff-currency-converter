package ru.tinkoff.converter.feature.converter.adapter

import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.viewpager.widget.ViewPager

class FancyPageChangeListener(
    private val adapter: CurrencyPagerAdapter
) : ViewPager.SimpleOnPageChangeListener() {
    private val interpolator: Interpolator = FastOutLinearInInterpolator()

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        adapter.getView(position)?.alpha = interpolator.getInterpolation(1 - positionOffset)
        val interpolation = interpolator.getInterpolation(positionOffset)
        adapter.getView(position + 1)?.alpha = interpolation
        adapter.getView(position - 1)?.alpha = interpolation
    }
}
