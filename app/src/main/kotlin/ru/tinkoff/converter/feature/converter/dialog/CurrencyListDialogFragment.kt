package ru.tinkoff.converter.feature.converter.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.tinkoff.converter.R
import ru.tinkoff.converter.model.Currency

class CurrencyListDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_CURRENCY_LIST = "ARG_CURRENCY_LIST"

        @JvmStatic
        fun newInstance(currencies: ArrayList<Currency>): CurrencyListDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_CURRENCY_LIST, currencies)
            val fragment = CurrencyListDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
                .setTitle(R.string.currency_list_title)
                .setItems(getItems()) { _, which ->
                    callbacks?.onCurrencySelected(tag, which)
                }
                .setNegativeButton(R.string.cancel, null)
                .create()
    }

    private fun getItems(): Array<CharSequence> {
        @Suppress("UNCHECKED_CAST")
        val currencies = arguments?.getSerializable(ARG_CURRENCY_LIST) as List<Currency>
        return currencies
                .map {
                    SpannableStringBuilder(it.id)
                            .append("\n")
                            .append(it.name,
                                    ForegroundColorSpan(requireContext().getColor(R.color.grey)),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                .toTypedArray()
    }

    interface Callbacks {
        fun onCurrencySelected(tag: String?, pos: Int)
    }
}