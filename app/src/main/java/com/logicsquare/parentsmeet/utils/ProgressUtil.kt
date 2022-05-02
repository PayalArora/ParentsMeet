package com.logicsquare.parentsmeet.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.logicsquare.parentsmeet.R

object ProgressUtil{

    private lateinit var dialog: Dialog


    fun showLoading(ctx: Context){
        dialog = Dialog(ctx)

        if (dialog != null && dialog.isShowing) {
            return
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_progress_dialog);
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00141414")))
        dialog.show()

    }


    fun hideLoading(){
        try {
            if(dialog.isShowing){
                dialog.dismiss()
            }
        } catch (e: UninitializedPropertyAccessException) {
            //  Log.d("TAG","AlertDialog UninitializedPropertyAccessException")
        }
    }


}
