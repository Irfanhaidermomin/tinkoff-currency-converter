package ru.tinkoff.converter.feature.converter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_converter.*
import ru.tinkoff.converter.R
import ru.tinkoff.converter.ext.asString
import ru.tinkoff.converter.ext.textAsBigDecimal
import ru.tinkoff.converter.feature.converter.adapter.CurrencyPagerAdapter
import ru.tinkoff.converter.feature.converter.adapter.FancyPageChangeListener
import ru.tinkoff.converter.feature.converter.dialog.CurrencyListDialogFragment
import ru.tinkoff.converter.feature.converter.drawable.BackgroundDrawable
import ru.tinkoff.converter.feature.input.InputActivity
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.moxy.MvpAndroidXAppCompatActivity
import java.math.BigDecimal

class ConverterActivity : MvpAndroidXAppCompatActivity(), ConverterView,
    CurrencyListDialogFragment.Callbacks {

    companion object {
        private const val REQUEST_CODE_INPUT_IN = 1
        private const val REQUEST_CODE_INPUT_OUT = 2
        private const val TAG_CURRENCY_LIST_IN = "TAG_CURRENCY_LIST_IN"
        private const val TAG_CURRENCY_LIST_OUT = "TAG_CURRENCY_LIST_OUT"
    }

    @InjectPresenter
    lateinit var presenter: ConverterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun getAmount(): BigDecimal {
            return data?.getSerializableExtra(InputActivity.EXTRA_AMOUNT) as? BigDecimal
                ?: BigDecimal.ONE
        }

        when (requestCode) {
            REQUEST_CODE_INPUT_IN -> if (resultCode == Activity.RESULT_OK) {
                val newAmount = getAmount()
                textAmountIn.text = newAmount.asString()
                presenter.convert(pagerCurrencyIn.currentItem, pagerCurrencyOut.currentItem, newAmount)

            }
            REQUEST_CODE_INPUT_OUT -> if (resultCode == Activity.RESULT_OK) {
                val newAmount = getAmount()
                textAmountOut.text = newAmount.asString()
                presenter.convertInverse(pagerCurrencyOut.currentItem, pagerCurrencyIn.currentItem, newAmount)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showCurrencies(currencies: List<Currency>) {
        val adapterCurrencyIn = CurrencyPagerAdapter(currencies, false) {
            val fragment = CurrencyListDialogFragment.newInstance(ArrayList(currencies))
            fragment.show(supportFragmentManager, TAG_CURRENCY_LIST_IN)
        }
        pagerCurrencyIn.adapter = adapterCurrencyIn
        pagerCurrencyIn.addOnPageChangeListener(FancyPageChangeListener(adapterCurrencyIn))
        pagerCurrencyIn.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                // TODO
            }
        })

        val adapterCurrencyOut = CurrencyPagerAdapter(currencies, true) {
            val fragment = CurrencyListDialogFragment.newInstance(ArrayList(currencies))
            fragment.show(supportFragmentManager, TAG_CURRENCY_LIST_OUT)
        }
        pagerCurrencyOut.adapter = adapterCurrencyOut
        pagerCurrencyOut.addOnPageChangeListener(FancyPageChangeListener(adapterCurrencyOut))
        pagerCurrencyOut.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                // TODO
            }
        })

        buttonSwap.isEnabled = true
    }

    override fun showError(noCurrencies: Boolean) {
        if (noCurrencies) {
            Snackbar.make(viewRoot, R.string.error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { presenter.loadCurrencies() }
                .show()
        } else {
            Snackbar.make(viewRoot, R.string.error, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun showResult(from: Currency, amountIn: BigDecimal, to: Currency, amountOut: BigDecimal) {
        textAmountIn.setText(amountIn, from)
        textAmountOut.setText(amountOut, to)
    }

    override fun onCurrencySelected(tag: String?, pos: Int) {
        when (tag) {
            TAG_CURRENCY_LIST_IN -> pagerCurrencyIn.setCurrentItem(pos, false)
            TAG_CURRENCY_LIST_OUT -> pagerCurrencyOut.setCurrentItem(pos, false)
        }
    }

    private fun initViews() {
        viewRoot.background = BackgroundDrawable(getColor(R.color.colorPrimary))
        buttonSwap.setOnClickListener {
            onButtonSwapClick()
        }
        buttonSwap.isEnabled = false
        textAmountIn.setOnClickListener {
            val intent = InputActivity.getStartIntent(this, textAmountIn.textAsBigDecimal())
            startActivityForResult(intent, REQUEST_CODE_INPUT_IN)
        }
        textAmountOut.setOnClickListener {
            val intent = InputActivity.getStartIntent(this, textAmountOut.textAsBigDecimal())
            startActivityForResult(intent, REQUEST_CODE_INPUT_OUT)
        }
    }

    private fun onButtonSwapClick() {
        // TODO

        val rotation = if (buttonSwap.rotation > 0) 0f else 180f
        buttonSwap.animate()
            .rotation(rotation)
            .setInterpolator(DecelerateInterpolator(3f))
            .setDuration(300)
            .start()
    }

    private fun TextView.setText(amount: BigDecimal, currency: Currency) {
        if (currency.symbol.isEmpty()) {
            text = amount.asString()
        } else {
            text = SpannableStringBuilder(amount.asString())
                .append(" ")
                .append(currency.symbol, ForegroundColorSpan(getColor(R.color.grey)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
