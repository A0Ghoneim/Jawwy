package com.example.jawwy

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu

class MyPopupMenu(context:Context, anchor: View) : PopupMenu(context, anchor) {

    override fun setForceShowIcon(forceShowIcon: Boolean) {
        super.setForceShowIcon(forceShowIcon)
    }
}