package com.investkaro.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String businessModel;

    // Give them default values so they are never null from the DB/serialization
    private Integer foundedYear = 0;
    private Integer teamSize = 0;

    private String problem;
    private String solution;
    private String targetMarket;
    private String usp;
    private String competitors;

    private Double revenue = 0.0;
    private Double burnRate = 0.0;
    private Double cac = 0.0;
    private Double ltv = 0.0;
    private Double totalFunding = 0.0;

    private Double amountRequired = 0.0;
    private Double equityOffered = 0.0;
    private Double valuation = 0.0;

    @ManyToOne
    @JoinColumn(name="founder_id")
    private FounderProfile founder;

    @ManyToOne
    @JoinColumn(name="industry_id")
    private Industry industry;
}
