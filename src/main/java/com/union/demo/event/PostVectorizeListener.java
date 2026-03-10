package com.union.demo.event;

import com.union.demo.service.VectorService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostVectorizeListener {

    private final VectorService vectorService;

    public PostVectorizeListener(VectorService vectorService) {
        this.vectorService = vectorService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        vectorService.vectorizePost(event.getPostId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostUpdatedEvent event) {
        vectorService.vectorizePost(event.getPostId());
    }
}