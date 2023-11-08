package com.example.frontend

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

class LoadingDialog {

    private lateinit var activity: Activity
    private lateinit var dialog: AlertDialog

    constructor(MyActivity: Activity){
        this.activity = MyActivity
    }

    fun LoadingDialog(MyActivity: Activity) {
        this.activity = MyActivity
    }

    fun startLoadingDialog() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loader, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}