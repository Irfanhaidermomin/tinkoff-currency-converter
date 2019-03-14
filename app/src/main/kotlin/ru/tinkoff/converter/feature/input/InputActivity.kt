package ru.tinkoff.converter.feature.input

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_input.*
import ru.tinkoff.converter.R
import ru.tinkoff.converter.ext.asString
import ru.tinkoff.converter.ext.textAsBigDecimal
import java.math.BigDecimal
import kotlin.LazyThreadSafetyMode.NONE

class InputActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"

        @JvmStatic
        fun getStartIntent(context: Context, amount: BigDecimal): Intent {
            val intent = Intent(context, InputActivity::class.java)
            intent.putExtra(EXTRA_AMOUNT, amount)
            return intent
        }
    }

    private val amount: BigDecimal by lazy(NONE) {
        intent.getSerializableExtra(EXTRA_AMOUNT) as BigDecimal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        toolbar.setNavigationOnClickListener { finish() }
        editAmount.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                finishWithResult()
                true
            } else {
                false
            }
        }
        if (savedInstanceState == null) {
            editAmount.setText(amount.asString())
        }
    }

    private fun finishWithResult() {
        val result = Intent().putExtra(EXTRA_AMOUNT, editAmount.textAsBigDecimal(amount))
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}