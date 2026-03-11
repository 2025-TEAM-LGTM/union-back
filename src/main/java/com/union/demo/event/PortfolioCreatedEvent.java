package com.union.demo.event;

public class PortfolioCreatedEvent {
    private final Long portfolioId;

    public PortfolioCreatedEvent(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }
}
