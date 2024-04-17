package com.example.conper.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.conper.ContentProviderDataItem

class viewModel :ViewModel() {
    var imgState = mutableStateOf(emptyList<ContentProviderDataItem>())

    fun udataeImg(imgages : List<ContentProviderDataItem>){
        imgState.value = imgages
    }




}