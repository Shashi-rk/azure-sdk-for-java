// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.autoconfigure.jms;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.support.QosSettings;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;

/**
 * {@link ConfigurationProperties} for configuring Azure Service Bus JMS.
 */
@Validated
@ConfigurationProperties("spring.jms.servicebus")
public class AzureServiceBusJMSProperties {

    private String connectionString;

    /**
     * JMS clientID. Only works for the bean of topicJmsListenerContainerFactory.
     */
    private String topicClientId;

    private int idleTimeout = 1800000;

    private String pricingTier;

    private final Listener listener = new Listener();

    private final PrefetchPolicy prefetchPolicy = new PrefetchPolicy();

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getTopicClientId() {
        return topicClientId;
    }

    public void setTopicClientId(String topicClientId) {
        this.topicClientId = topicClientId;
    }

    public String getPricingTier() {
        return pricingTier;
    }

    public void setPricingTier(String pricingTier) {
        this.pricingTier = pricingTier;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Listener getListener() {
        return listener;
    }

    public PrefetchPolicy getPrefetchPolicy() {
        return prefetchPolicy;
    }

    /**
     * Validate spring.jms.servicebus related properties.
     *
     * @throws IllegalArgumentException If connectionString is empty.
     */
    @SuppressFBWarnings
    @PostConstruct
    public void validate() {
        if (!StringUtils.hasText(connectionString)) {
            throw new IllegalArgumentException("'spring.jms.servicebus.connection-string' should be provided");
        }

        if (!pricingTier.matches("(?i)premium|standard|basic")) {
            throw new IllegalArgumentException("'spring.jms.servicebus.pricing-tier' is not valid");
        }
    }

    /**
     * Properties to configure {@link org.apache.qpid.jms.policy.JmsDefaultPrefetchPolicy} for
     * {@link org.apache.qpid.jms.JmsConnectionFactory} .
     */
    public static class PrefetchPolicy {

        private int all = 0;

        private int durableTopicPrefetch = 0;

        private int queueBrowserPrefetch = 0;

        private int queuePrefetch = 0;

        private int topicPrefetch = 0;

        public int getAll() {
            return Math.max(all, 0);
        }

        public void setAll(int all) {
            this.all = all;
        }

        /**
         * @return Returns the durableTopicPrefetch.
         */
        public int getDurableTopicPrefetch() {
            return durableTopicPrefetch > 0 ? durableTopicPrefetch : getAll();
        }

        /**
         * @param durableTopicPrefetch Sets the durable topic prefetch value
         */
        public void setDurableTopicPrefetch(int durableTopicPrefetch) {
            this.durableTopicPrefetch = durableTopicPrefetch;
        }

        /**
         *
         * @return Returns the queueBrowserPrefetch.
         */
        public int getQueueBrowserPrefetch() {
            return queueBrowserPrefetch > 0 ? queueBrowserPrefetch : getAll();
        }

        /**
         * @param queueBrowserPrefetch The queueBrowserPrefetch to set.
         */
        public void setQueueBrowserPrefetch(int queueBrowserPrefetch) {
            this.queueBrowserPrefetch = queueBrowserPrefetch;
        }

        /**
         * @return Returns the queuePrefetch.
         */
        public int getQueuePrefetch() {
            return queuePrefetch > 0 ? queuePrefetch : getAll();
        }

        /**
         * @param queuePrefetch The queuePrefetch to set.
         */
        public void setQueuePrefetch(int queuePrefetch) {
            this.queuePrefetch = queuePrefetch;
        }

        /**
         * @return Returns the topicPrefetch.
         */
        public int getTopicPrefetch() {
            return topicPrefetch > 0 ? topicPrefetch : getAll();
        }

        /**
         * @param topicPrefetch The topicPrefetch to set.
         */
        public void setTopicPrefetch(int topicPrefetch) {
            this.topicPrefetch = topicPrefetch;
        }

    }


    /**
     * Properties to configure {@link org.springframework.jms.annotation.JmsListener} for
     * {@link org.springframework.jms.config.AbstractJmsListenerContainerFactory}.
     */
    public static class Listener {

        /**
         * Whether the reply destination type is topic. Only works for the bean of topicJmsListenerContainerFactory.
         */
        private Boolean replyPubSubDomain;

        /**
         * Configure the {@link QosSettings} to use when sending a reply.
         */
        private QosSettings replyQosSettings;

        /**
         * Whether to make the subscription durable. Only works for the bean of topicJmsListenerContainerFactory.
         */
        private Boolean subscriptionDurable = Boolean.TRUE;

        /**
         * Whether to make the subscription shared. Only works for the bean of topicJmsListenerContainerFactory.
         */
        private Boolean subscriptionShared;

        /**
         * Specify the phase in which this container should be started and
         * stopped.
         */
        private Integer phase;

        public Boolean isReplyPubSubDomain() {
            return replyPubSubDomain;
        }

        public void setReplyPubSubDomain(Boolean replyPubSubDomain) {
            this.replyPubSubDomain = replyPubSubDomain;
        }

        public QosSettings getReplyQosSettings() {
            return replyQosSettings;
        }

        public void setReplyQosSettings(QosSettings replyQosSettings) {
            this.replyQosSettings = replyQosSettings;
        }

        public Boolean isSubscriptionDurable() {
            return subscriptionDurable;
        }

        public void setSubscriptionDurable(Boolean subscriptionDurable) {
            this.subscriptionDurable = subscriptionDurable;
        }

        public Boolean isSubscriptionShared() {
            return subscriptionShared;
        }

        public void setSubscriptionShared(Boolean subscriptionShared) {
            this.subscriptionShared = subscriptionShared;
        }

        public Integer getPhase() {
            return phase;
        }

        public void setPhase(Integer phase) {
            this.phase = phase;
        }
    }
}
