package com.trisiss.productivityinsidetesttask.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.trisiss.productivityinsidetesttask.R
import com.trisiss.productivityinsidetesttask.ui.main.OnDialogItemSelected

/**
 * Created by trisiss on 8/14/2021.
 */
class SelectDialog(val list: Array<String>, val listener: OnDialogItemSelected) : DialogFragment() {

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.title_dialog)
                .setItems(list) { _, which ->
                    listener.onItemSelected(which)
                }
                .setNegativeButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
