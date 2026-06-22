package com.investkaro.service.Impl;

import com.investkaro.dto.BookmarkResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.BookmarkService;
import com.investkaro.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, StartupRepository startupRepository, InvestorRepository investorRepository, UserRepository userRepository, NotificationService notificationService) {
        this.bookmarkRepository = bookmarkRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public BookmarkResponse addBookmark(Long startupId, String email) {
        InvestorProfile investor = getInvestor(email);
        Startup startup = getStartup(startupId);
        boolean existed = bookmarkRepository.existsByInvestorAndStartup(investor, startup);
        Bookmark bookmark = bookmarkRepository.findByInvestorAndStartup(investor, startup).orElseGet(() -> {
            Bookmark item = new Bookmark();
            item.setInvestor(investor);
            item.setStartup(startup);
            return bookmarkRepository.save(item);
        });
        if (!existed) {
            notificationService.createNotification(
                    startup.getFounder().getUser(),
                    "Startup bookmarked",
                    investor.getUser().getFullName() + " saved " + startup.getCompanyName(),
                    NotificationType.BOOKMARK
            );
        }
        return toResponse(bookmark);
    }

    @Override
    public List<BookmarkResponse> getBookmarks(String email) {
        InvestorProfile investor = getInvestor(email);
        return bookmarkRepository.findByInvestorOrderByCreatedAtDesc(investor).stream().map(this::toResponse).toList();
    }

    @Transactional
    @Override
    public void removeBookmark(Long startupId, String email) {
        InvestorProfile investor = getInvestor(email);
        Startup startup = getStartup(startupId);
        Bookmark bookmark = bookmarkRepository.findByInvestorAndStartup(investor, startup)
                .orElseThrow(() -> new EntityNotFoundException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    private InvestorProfile getInvestor(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return investorRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("Investor not found"));
    }

    private Startup getStartup(Long startupId) {
        return startupRepository.findById(startupId).orElseThrow(() -> new EntityNotFoundException("Startup not found"));
    }

    private BookmarkResponse toResponse(Bookmark bookmark) {
        Startup startup = bookmark.getStartup();
        return new BookmarkResponse(
                bookmark.getId(),
                startup.getId(),
                startup.getCompanyName(),
                startup.getIndustry() != null ? startup.getIndustry().getName() : null,
                startup.getAmountRequired(),
                bookmark.getCreatedAt()
        );
    }
}
