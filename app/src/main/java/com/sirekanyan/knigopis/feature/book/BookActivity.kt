package com.sirekanyan.knigopis.feature.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.BaseActivity
import com.sirekanyan.knigopis.common.extensions.getParcelableExtraCompat
import com.sirekanyan.knigopis.common.functions.extra
import com.sirekanyan.knigopis.dependency.providePresenter
import com.sirekanyan.knigopis.model.EditBookModel

private val EXTRA_BOOK = extra("book")

fun Context.createBookIntent(book: EditBookModel): Intent =
    Intent(this, BookActivity::class.java).putExtra(EXTRA_BOOK, book)

class BookActivity : BaseActivity(), BookPresenter.Router {

    private val presenter by lazy { providePresenter(intent.getParcelableExtraCompat(EXTRA_BOOK)!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_edit)
        presenter.init()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun exit(ok: Boolean) {
        if (ok) setResult(RESULT_OK)
        finish()
    }

}