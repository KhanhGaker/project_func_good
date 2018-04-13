package me.vadik.knigopis.dialog

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_item.view.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_view.*
import me.vadik.knigopis.R
import me.vadik.knigopis.inflate

interface DialogFactory {

    fun showDialog(title: String, vararg items: DialogItem)

}

class BottomSheetDialogFactory(private val context: Context) : DialogFactory {

    override fun showDialog(title: String, vararg items: DialogItem) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_dialog_view)
        dialog.bottomSheetTitle.text = title
        val container = dialog.bottomSheetContainer
        items.forEach { item ->
            val itemView = container.inflate(R.layout.bottom_sheet_dialog_item)
            itemView.bottomSheetItemIcon.setImageResource(item.iconRes)
            item.title.setValueTo(itemView.bottomSheetItemText)
            itemView.setOnClickListener {
                item.onClick()
                dialog.hide()
            }
            container.addView(itemView)
        }
        dialog.show()
    }

}