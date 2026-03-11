package com.union.demo.event;

public class PortfolioUpdatedEvent {
    private final Long portfolioId;

    public PortfolioUpdatedEvent(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }
}
