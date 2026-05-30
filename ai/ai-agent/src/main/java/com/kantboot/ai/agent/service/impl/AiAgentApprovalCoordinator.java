package com.kantboot.ai.agent.service.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class AiAgentApprovalCoordinator {

    private final ConcurrentHashMap<Long, CompletableFuture<Boolean>> pending = new ConcurrentHashMap<>();

    public boolean awaitApproval(Long sessionId, int timeoutSeconds) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pending.put(sessionId, future);
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            pending.remove(sessionId);
        }
    }

    public void approve(Long sessionId) {
        CompletableFuture<Boolean> future = pending.get(sessionId);
        if (future != null) {
            future.complete(true);
        }
    }

    public void reject(Long sessionId) {
        CompletableFuture<Boolean> future = pending.get(sessionId);
        if (future != null) {
            future.complete(false);
        }
    }

    public boolean hasPending(Long sessionId) {
        return pending.containsKey(sessionId);
    }
}
