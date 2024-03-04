/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.async.logger.internal;

import com.lmax.disruptor.WaitStrategy;
import org.apache.logging.log4j.async.logger.AsyncLoggerConfigDisruptor;
import org.apache.logging.log4j.async.logger.AsyncLoggerDisruptor;
import org.apache.logging.log4j.async.logger.DisruptorConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.Log4jPropertyKey;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.plugins.Factory;
import org.apache.logging.log4j.plugins.Named;
import org.apache.logging.log4j.plugins.SingletonFactory;
import org.apache.logging.log4j.plugins.condition.ConditionalOnMissingBinding;
import org.apache.logging.log4j.plugins.condition.ConditionalOnPresentBindings;

/**
 * Provides default services for the per-context instance factory.
 */
public class DefaultBundle {

    @Factory
    @Named("AsyncLogger")
    @ConditionalOnMissingBinding
    public WaitStrategy asyncLoggerWaitStrategy() {
        return DisruptorUtil.createWaitStrategy(Log4jPropertyKey.ASYNC_LOGGER_WAIT_STRATEGY, null);
    }

    @SingletonFactory
    @ConditionalOnMissingBinding
    public AsyncLoggerDisruptor.Factory asyncLoggerDisruptorFactory(
            final @Named("AsyncLogger") WaitStrategy waitStrategy) {
        return contextName -> new AsyncLoggerDisruptor(contextName, waitStrategy);
    }

    @Factory
    @Named("AsyncLoggerConfig")
    @ConditionalOnPresentBindings(bindings = Configuration.class)
    public WaitStrategy defaultAsyncLoggerWaitStrategy(final Configuration configuration) {
        final DisruptorConfiguration disruptorConfiguration = configuration.getExtension(DisruptorConfiguration.class);
        return DisruptorUtil.createWaitStrategy(
                Log4jPropertyKey.ASYNC_CONFIG_WAIT_STRATEGY,
                disruptorConfiguration != null ? disruptorConfiguration.getWaitStrategyFactory() : null);
    }

    @Factory
    @ConditionalOnPresentBindings(bindings = Configuration.class)
    public AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor(
            final @Named("AsyncLoggerConfig") WaitStrategy waitStrategy, final LogEventFactory logEventFactory) {
        return new AsyncLoggerConfigDisruptor(waitStrategy, logEventFactory);
    }
}
