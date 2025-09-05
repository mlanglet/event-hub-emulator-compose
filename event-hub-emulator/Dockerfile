FROM python:3.11-slim

WORKDIR /app

RUN pip install azure-eventhub

COPY eventhub_test.py .

ENV GPS_EHUB_CONNECTION="Endpoint=sb://eventhubs-emulator/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE"
ENV GPS_EHUB_NAME="event-hub"
ENV GPS_EHUB_CONSUMER_GROUP="consumer-group"

CMD ["python", "eventhub_test.py"]
