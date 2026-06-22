package com.investkaro.controller;

import com.investkaro.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{startupId}")
    public ResponseEntity<?> addBookmark(@PathVariable Long startupId, Authentication authentication) {
        return ResponseEntity.ok(bookmarkService.addBookmark(startupId, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<?> getBookmarks(Authentication authentication) {
        return ResponseEntity.ok(bookmarkService.getBookmarks(authentication.getName()));
    }

    @DeleteMapping("/{startupId}")
    public ResponseEntity<?> removeBookmark(@PathVariable Long startupId, Authentication authentication) {
        bookmarkService.removeBookmark(startupId, authentication.getName());
        return ResponseEntity.ok("Bookmark removed");
    }
}
