package com.trackster.tracksterapp.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.trackster.tracksterapp.R

object DialogUtils {

    var dialogIsActive = false

    fun showGeneralErrorMessage(activity: Activity?) {
        if (activity == null || activity.isFinishing)
            return

        showMessage(activity, activity.getString(R.string.general_error_title), activity.getString(R.string.general_error_message), null, null)
    }

    fun showErrorMessage(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        showMessage(activity, activity.getString(R.string.general_error_title), message, null, null)
    }
    fun showSuccessMessage(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, activity.getString(R.string.general_success_title), message, null)
    }
    private fun createMessageDialog(context: Context, title: String, message: String?,
                                    clickListener: DialogInterface.OnClickListener?): AlertDialog {
        val builder = AlertDialog.Builder(context)

        builder.setMessage(message)

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title)

        if (clickListener == null) {
            val dismissListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                run {
                    dialogInterface.dismiss()
                    dialogIsActive = false
                }
            }
            builder.setPositiveButton(context.getString(R.string.general_error_button), dismissListener)
        } else {
            builder.setNeutralButton(context.getString(R.string.general_error_button), clickListener)
        }

        builder.setOnCancelListener {
            dialogIsActive = false
        }
        val dialog = builder.create()

        dialog.show()
        dialogIsActive = true

        return dialog
    }

    fun showErrorMessage(activity: Activity?, message: String, listener: DialogInterface.OnClickListener?): AlertDialog? =
        if (activity == null || activity.isFinishing) null
        else showMessage(activity, activity.getString(R.string.general_error_title), message, listener, null)

    fun showMessage(activity: Activity?, message: String, dismissListener: DialogInterface.OnDismissListener?) {
        if (activity == null || activity.isFinishing)
            return

        showMessage(activity, "", message, null, dismissListener)
    }

    private fun showMessage(context: Context, title: String, message: String?,
                            clickListener: DialogInterface.OnClickListener?,
                            dismissListener: DialogInterface.OnDismissListener?): AlertDialog {
        val builder = AlertDialog.Builder(context)

        builder.setMessage(message)

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title)

        val dialog = builder.create()

        if (dismissListener != null)
            dialog.setOnDismissListener(dismissListener)

        if (clickListener == null) {
            val dialogClickListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.general_error_button), dialogClickListener)
        } else
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.general_error_button), clickListener)

        dialog.show()

        return dialog
    }
}