package tech.initlabs.eventHubEmulatorCompose

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import tech.initlabs.eventHubEmulatorCompose.config.AzureEventHubProperties

@SpringBootApplication
@EnableConfigurationProperties(
    value = [
        AzureEventHubProperties::class,
    ],
)
open class AzureFunctionApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    (runApplication<AzureFunctionApplication>(*args))
}
