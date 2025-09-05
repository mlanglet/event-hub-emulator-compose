import logging
import os
from azure.eventhub import EventHubConsumerClient

CONNECTION_STR = os.getenv("GPS_EHUB_CONNECTION")
CONSUMER_GROUP = os.getenv("GPS_EHUB_CONSUMER_GROUP", "$Default")
EVENTHUB_NAME = os.getenv("GPS_EHUB_NAME")


def on_event(partition_context, event):
    print(f"Partition {partition_context.partition_id} received event: {event.body_as_str()}")
    print(f"Message annotations: {event.raw_amqp_message.annotations}")
    partition_context.update_checkpoint(event)


def main():
    client = EventHubConsumerClient.from_connection_string(
        conn_str=CONNECTION_STR,
        consumer_group=CONSUMER_GROUP,
        eventhub_name=EVENTHUB_NAME
    )

    try:
        print("Listening for events...")
        with client:
            client.receive(
                on_event=on_event,
                starting_position="-1"  # read from the beginning
            )
    except KeyboardInterrupt:
        print("Stopped by user.")
    except Exception as e:
        print(f"Error: {e}")


if __name__ == "__main__":
    main()
