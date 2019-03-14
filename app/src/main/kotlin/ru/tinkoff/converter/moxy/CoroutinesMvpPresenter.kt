package ru.tinkoff.converter.moxy

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesMvpPresenter<View : MvpView> : MvpPresenter<View>(), CoroutineScope {

    private val job = SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
}
