package com.investkaro.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    private InvestorProfile investor;

    @ManyToOne
    @JoinColumn(name = "founder_id")
    private FounderProfile founder;

    @ManyToOne
    @JoinColumn(name = "startup_id")
    private Startup startup;

    private LocalDateTime meetingDate;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status = MeetingStatus.PENDING;
}
