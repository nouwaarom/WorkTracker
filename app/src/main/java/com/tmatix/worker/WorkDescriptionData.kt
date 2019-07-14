package com.tmatix.worker

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class WorkDescriptionData(private var _title: String) : BaseObservable() {
    var title: String
        @Bindable get() = _title
        set(title) {
            _title = title
            notifyPropertyChanged(BR.title)
        }
}