package tech.initlabs.eventHubEmulatorCompose.function

import com.azure.messaging.eventhubs.EventData
import com.azure.messaging.eventhubs.EventHubProducerClient
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("default", "local", "compose")
class ComposeHealthCheckFunction(
    private val eventHubProducer: EventHubProducerClient,
) {
    @FunctionName("HealthCheck")
    fun healthCheck(
        @HttpTrigger(
            name = "request",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.ANONYMOUS,
        )
        request: HttpRequestMessage<String?>,
        context: ExecutionContext,
    ): HttpResponseMessage {
        val isEventHubConnected = checkEventHubConnection(context)

        context.logger.info("HealthCheck called with payload: ${request.body}")

        return if (isEventHubConnected) {
            context.logger.info(
                "Messaging emulators are up and running.",
            )
            request.createResponseBuilder(HttpStatus.OK).build()
        } else {
            context.logger.info(
                "Azure event hub emulator is not ready or" +
                    " ${eventHubProducer.eventHubName} does not exists.",
            )

            request.createResponseBuilder(HttpStatus.SERVICE_UNAVAILABLE).build()
        }
    }

    private fun checkEventHubConnection(context: ExecutionContext): Boolean {
        try {
            eventHubProducer.send(
                listOf(
                    EventData(
                        """
                        {
                           "message": "Hello event hub emulator!"
                        }
                        """.trimIndent(),
                    ),
                ),
            )
            return true
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Exception,
        ) {
            context.logger.warning("Exception occurred while checking eventhub connection: ${e.message}")
            return false
        }
    }
}
