package com.investkaro.service;

import com.investkaro.dto.BookmarkResponse;

import java.util.List;

public interface BookmarkService {
    BookmarkResponse addBookmark(Long startupId, String email);
    List<BookmarkResponse> getBookmarks(String email);
    void removeBookmark(Long startupId, String email);
}
