FROM mcr.microsoft.com/azure-functions/java:4-java21-slim

# Install curl for internal health check
RUN apt-get update && apt-get install -y curl --no-install-recommends

COPY ./build/azure-functions/eventHubEmulatorCompose /home/site/wwwroot
ENV AzureWebJobsScriptRoot=/home/site/wwwroot \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true \
    AzureWebJobsSecretStorageType=files \
    AzureWebJobsStorage="DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://azurite:10000/devstoreaccount1;" \
    EHUB_CONNECTION="Endpoint=sb://eventhubs-emulator/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true" \
    EHUB_NAME="event-hub" \
    EHUB_CONSUMER_GROUP="consumer-group" \
    MAIN_CLASS="tech.initlabs.eventHubEmulatorCompose.AzureFunctionApplication"
