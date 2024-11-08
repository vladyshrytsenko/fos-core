package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Value("${subscription.daily.cron}")
    private String dailyCron;

    @Value("${subscription.weekly.cron}")
    private String weeklyCron;

    @Value("${subscription.monthly.cron}")
    private String monthlyCron;

    @Scheduled(cron = "#{@lunchSubscriptionService.dailyCron}")
    public void dailySubscription() {
    }

    @Scheduled(cron = "#{@lunchSubscriptionService.weeklyCron}")
    public void weeklySubscription() {
    }

    @Scheduled(cron = "#{@lunchSubscriptionService.monthlyCron}")
    public void monthlySubscription() {
    }
}

