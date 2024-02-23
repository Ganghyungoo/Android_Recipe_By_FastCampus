package com.test.searchmediaproject.repository

import com.test.searchmediaproject.model.ListItem
import io.reactivex.rxjava3.core.Observable

interface SearchRepository {
    fun search(query:String): Observable<List<ListItem>>
}