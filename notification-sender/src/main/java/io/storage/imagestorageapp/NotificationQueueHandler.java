package io.storage.imagestorageapp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishBatchRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import io.storage.imagestorageapp.client.SnsClient;
import io.storage.imagestorageapp.client.SqsClient;
import io.storage.imagestorageapp.consts.AwsProps;

import java.util.List;
import java.util.Map;

/**
 * Author: Samandar_Akbarov
 * Date: 10/31/2022
 */
public class NotificationQueueHandler implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {
    private LambdaLogger logger;
    private final AmazonSNS snsClient = SnsClient.getInstance();
    private final AmazonSQS sqsClient = SqsClient.getInstance();

    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> input, Context context) {
        var detailType = getDetailType(input.get("detail-type"));
        logger = context.getLogger();
        logger.log("Lambda handle event:"
                + "\nDetail-type: " + detailType
                + "\nFunction-name: " + context.getFunctionName()
        );
        var messages = this.readMessages();
        logger.log("Message size = " + messages.size());
        messages.forEach(message -> this.sendMessageToTopic(message.getBody()));
        logger.log("Deleted fetched messages");
        messages.stream()
                .map(Message::getReceiptHandle)
                .forEach(receipt -> sqsClient.deleteMessage(AwsProps.SQS_URL, receipt));
        logger.log("Lambda event finished");
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("")
                .withIsBase64Encoded(false);
    }

    private String getDetailType(Object detailType) {
        if (detailType != null) {
            return String.valueOf(detailType);
        }
        return "API";
    }

    private List<Message> readMessages() {
        logger.log("Get connection from sqs client");
        var queueUrl = AwsProps.SQS_URL;
        logger.log("Start fetching process");
        var request = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(2)
                .withMaxNumberOfMessages(10);

        return sqsClient.receiveMessage(request).getMessages();
    }

    public void sendMessageToTopic(String message) {
        logger.log("Send messages to sns topic");
        var publishRequest = new PublishRequest()
                .withMessage(message)
                .withTopicArn(AwsProps.SNS_ARN);
        snsClient.publish(publishRequest);
    }

}
