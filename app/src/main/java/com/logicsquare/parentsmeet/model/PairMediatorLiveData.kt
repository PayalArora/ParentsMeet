package com.logicsquare.parentsmeet.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class PairMediatorLiveData<ForumDetailResponse, GetAllCommentsResponse>(firstLiveData: MutableLiveData<ForumDetailResponse>, secondLiveData: MutableLiveData<GetAllCommentsResponse>) : MediatorLiveData<Pair<ForumDetailResponse?, GetAllCommentsResponse?>>() {
    init {
        addSource(firstLiveData) { firstLiveDataValue: ForumDetailResponse -> value = firstLiveDataValue to secondLiveData.value }
        addSource(secondLiveData) { secondLiveDataValue: GetAllCommentsResponse -> value = firstLiveData.value to secondLiveDataValue }
    }
}