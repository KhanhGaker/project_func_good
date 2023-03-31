package org.sirekanyan.knigopis.common.functions

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.extensions.isVisible
import org.sirekanyan.knigopis.common.extensions.show
import org.sirekanyan.knigopis.common.extensions.showToast
import retrofit2.HttpException

fun handleError(
    throwable: Throwable,
    emptyPlaceholder: View,
    errorPlaceholder: TextView,
    adapter: RecyclerView.Adapter<*>,
) {
    if (emptyPlaceholder.isVisible || adapter.itemCount > 0) {
        emptyPlaceholder.context.showToast(throwable.messageRes)
    } else {
        errorPlaceholder.setText(throwable.messageRes)
        errorPlaceholder.show()
    }
}

private val Throwable.messageRes
    get() = if (this is HttpException && code() == 401) {
        R.string.main_error_unauthorized
    } else {
        R.string.common_error_network
    }