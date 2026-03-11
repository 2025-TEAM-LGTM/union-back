package com.union.demo.event;

import com.union.demo.service.VectorService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PortfolioVectorizeListener {

    private final VectorService vectorService;

    public PortfolioVectorizeListener(VectorService vectorService) {
        this.vectorService = vectorService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePortfolioCreated(PortfolioCreatedEvent event) {
        vectorService.vectorizePortfolio(event.getPortfolioId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePortfolioUpdated(PortfolioUpdatedEvent event) {
        vectorService.vectorizePortfolio(event.getPortfolioId());
    }
}