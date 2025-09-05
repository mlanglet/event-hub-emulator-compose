# Azure Event Hub Emulator in Compose setup

This project was created to reproduce the connection problem from Azure function app binding to a local event hub
emulator setup.

## Components

### ComposeHealthCheckFunction

[ComposeHealthCheckFunction.kt](src/main/kotlin/tech/initlabs/eventHubEmulatorCompose/function/ComposeHealthCheckFunction.kt)

This is function with an HTTP binding that is called via the compose healthcheck to determine container health. It
connects to the event
hub emulator using a producer client and sends a message. When it sends a message successfully it will report itself
healthy.

### EventHubConsumerFunction

[EventHubConsumerFunction.kt](src/main/kotlin/tech/initlabs/eventHubEmulatorCompose/function/EventHubConsumerFunction.kt)

This function is configured to read events from the event hubs emulator, but it cannot connect.

### Python SDK test

[eventhub_test.py](event-hub-emulator/eventhub_test.py)

As extra confirmation that this is not related to the function runtime binding implementaion this shows that the same
problem is present in the python consumer SDK as well.

## Setup

```
./gradlew azureFunctionsPackage
docker compose build
docker compose up -d
```

## Inspect

Inspect the logs for the different containers to see that

1. `docker logs eventhubs-emulator` the event hubs emulator starts up and the entity and consumer group is created
2. `docker logs azure-functions|grep Functions.HealthCheck` the internal health check functions connects to the entity
   and can produce events
3. `docker logs azure-functions|grep Functions.EventHubConsumerFunction` the function binding to consume events cannot
   connect to the event hub emulator
4. `docker logs eventhub-python-sdk-test` the same is true for the python SDK trying to connect to the emulator

## Expected outcome

The message

```
{
    "message": "Hello event hub emulator!"
}
```

should be repeatedly printed in the `docker logs azure-functions` logs

## Hypothesis

It seems like the emulator is advertising the wrong endpoint for the consumers
