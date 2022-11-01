package io.storage.imagestorageapp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;

/**
 * Author: Samandar_Akbarov
 * Date: 11/01/2022
 */
public class S3EvenHandler implements RequestHandler<S3Event, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(S3Event s3Event, Context context) {
        var logger = context.getLogger();
        s3Event.getRecords().stream()
                .map(record -> record.getS3().getObject().getKey())
                .forEach(key -> logger.log("Image Uploaded -> Key: " + key));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("")
                .withIsBase64Encoded(false);
    }
}
