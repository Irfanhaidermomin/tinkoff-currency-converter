package ru.tinkoff.converter.feature.converter.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import ru.tinkoff.converter.R
import ru.tinkoff.converter.model.Currency

class CurrencyPagerAdapter(
        private val dataset: List<Currency>,
        private val dark: Boolean,
        private val clickListener: () -> Unit
) : PagerAdapter(), View.OnClickListener {

    private val views = SparseArray<View>(dataset.size)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)

        val view = layoutInflater.inflate(R.layout.item_currency, container, false)
        view.alpha = 0f

        val currency = dataset[position]
        val currencyCode: TextView = view.findViewById(R.id.currencyCode)
        currencyCode.text = currency.id
        if (dark) {
            currencyCode.setTextColor(container.context.getColor(R.color.colorPrimary))
        }

        val currencyName: TextView = view.findViewById(R.id.currencyName)
        currencyName.text = currency.name

        views.put(position, view)
        view.setOnClickListener(this)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val view = any as View
        container.removeView(view)
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int {
        return dataset.size
    }

    fun getView(position: Int): View? = views.get(position)

    override fun onClick(v: View) {
        clickListener()
    }
}
