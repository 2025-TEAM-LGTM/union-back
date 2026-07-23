package com.union.demo.listener;

import com.union.demo.event.PortfolioChangedEvent;
import com.union.demo.service.VectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PortfolioVectorizeListener {
    private final VectorService vectorService;

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void handle(PortfolioChangedEvent event){
        vectorService.vectorizePortfolio(event.portfolioId());
    }
}
