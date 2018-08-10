package com.sirekanyan.knigopis.dependency

import android.app.Activity
import com.sirekanyan.knigopis.common.android.Permissions
import com.sirekanyan.knigopis.common.android.PermissionsImpl
import com.sirekanyan.knigopis.common.android.dialog.BottomSheetDialogFactory
import com.sirekanyan.knigopis.common.android.dialog.DialogFactory

fun Activity.provideDialogs(): DialogFactory =
    BottomSheetDialogFactory(this)

fun Activity.providePermissions(): Permissions =
    PermissionsImpl(this)