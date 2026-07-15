package com.example.logisticstrackingservice.notification.provider;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import com.example.logisticstrackingservice.exception.AllProvidersFailedException;
import com.example.logisticstrackingservice.exception.ProviderException;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProviderSelector {

    private final List<NotificationProvider> providers;
    // comparator and comparable
    public NotificationChannel send(NotificationDto notification, List<NotificationChannel> preferredChannels) {
        List<NotificationProvider> orderedProviders = providers.stream()
                .sorted(Comparator.comparingDouble(NotificationProvider::getCost)
                        .thenComparingInt(NotificationProvider::getPriority))
                .toList();

        for (NotificationProvider provider : orderedProviders) {
            for (NotificationChannel channel : preferredChannels) {
                try {
                    sendViaChannel(provider, channel, notification);
                    log.info("[provider-selector] sent via {} on channel {}", provider.getName(), channel);
                    return channel;
                } catch (ProviderException e) {
                    log.warn("[provider-selector] provider {} failed for channel {}: {}",
                            provider.getName(), channel, e.getMessage());
                }
            }
        }
        throw new AllProvidersFailedException(
                "All providers failed for all preferred channels: " + preferredChannels);
    }

    private void sendViaChannel(NotificationProvider provider, NotificationChannel channel, NotificationDto notification)
            throws ProviderException {
        switch (channel) {
            case SMS -> provider.sendSms(notification);
            case EMAIL -> provider.sendEmail(notification);
            case PUSH -> provider.sendPush(notification);
        }
    }
}