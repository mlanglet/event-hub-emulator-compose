package tech.initlabs.eventHubEmulatorCompose.config

import com.azure.messaging.eventhubs.EventHubClientBuilder
import com.azure.messaging.eventhubs.EventHubProducerClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AzureEventHubConfig {
    @Bean
    fun eventHubProducer(properties: AzureEventHubProperties): EventHubProducerClient =
        EventHubClientBuilder()
            .connectionString(properties.connectionString)
            .eventHubName(properties.eventHubName)
            .buildProducerClient()
}

@ConfigurationProperties("app.azure.eventhub")
data class AzureEventHubProperties(
    val connectionString: String,
    val eventHubName: String,
)
