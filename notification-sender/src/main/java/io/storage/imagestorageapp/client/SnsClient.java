package io.storage.imagestorageapp.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import io.storage.imagestorageapp.consts.AwsProps;

/**
 * Author: Samandar_Akbarov
 * Date: 10/31/2022
 */
public final class SnsClient {
    private static SnsClient instance;
    private final AmazonSNS amazonSNS;

    private SnsClient(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;

    }

    public static AmazonSNS getInstance() {
        if (instance == null) {
            synchronized (SnsClient.class) {
                instance = new SnsClient(AmazonSNSClient
                        .builder()
                        .withCredentials(new AWSStaticCredentialsProvider(AwsProps.getAuth()))
                        .withRegion(AwsProps.REGION)
                        .build());
            }
        }
        return instance.amazonSNS;
    }


}
