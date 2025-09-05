package tech.initlabs.eventHubEmulatorCompose.function

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.EventHubTrigger
import com.microsoft.azure.functions.annotation.ExponentialBackoffRetry
import com.microsoft.azure.functions.annotation.FunctionName
import org.springframework.stereotype.Component

@Component
class EventHubConsumerFunction {
    @FunctionName("EventHubConsumerFunction")
    @ExponentialBackoffRetry(maxRetryCount = 10, maximumInterval = "00:10:00", minimumInterval = "00:00:10")
    fun run(
        @EventHubTrigger(
            name = "EventHubConsumerFunctionTrigger",
            consumerGroup = "%EHUB_CONSUMER_GROUP%",
            eventHubName = "%EHUB_NAME%",
            connection = "EHUB_CONNECTION",
        )
        payload: String,
        context: ExecutionContext,
    ) {
        context.logger.info("EventHubConsumerFunction received payload: $payload")
    }
}
