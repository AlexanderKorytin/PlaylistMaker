package com.example.playlistmaker.data.EditText

import android.view.View
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.domain.impl.SearchHistoryInteractor
import com.example.playlistmaker.presentetion.ui.find.FindAdapter

class EditTextInteractor(val bindingFindActivity: ActivityFindBinding) {
    fun setVisibilityViewsForShowSearchHistory(
        flag: Boolean,
        findAdapter: FindAdapter,
        historyAdapter: FindAdapter,
        iteractor: SearchHistoryInteractor
    ) {
        if (flag) {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            findAdapter.trackList.clear()
            findAdapter.notifyDataSetChanged()
            historyAdapter.trackList = iteractor.getTracksList()
            historyAdapter.notifyDataSetChanged()
        } else {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.VISIBLE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            historyAdapter.trackList = iteractor.getTracksList()
            historyAdapter.notifyDataSetChanged()
        }
    }

}