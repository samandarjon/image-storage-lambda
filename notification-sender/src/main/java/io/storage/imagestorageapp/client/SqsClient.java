package io.storage.imagestorageapp.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import io.storage.imagestorageapp.consts.AwsProps;

/**
 * Author: Samandar_Akbarov
 * Date: 10/31/2022
 */
public final class SqsClient {
    private static SqsClient instance;
    private final AmazonSQS amazonSQS;

    private SqsClient(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;

    }

    public static AmazonSQS getInstance() {
        if (instance == null) {
            synchronized (SqsClient.class) {
                instance = new SqsClient(AmazonSQSClient
                        .builder()
                        .withCredentials(new AWSStaticCredentialsProvider(AwsProps.getAuth()))
                        .withRegion(AwsProps.REGION)
                        .build());
            }
        }
        return instance.amazonSQS;
    }


}
