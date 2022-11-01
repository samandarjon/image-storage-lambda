package io.storage.imagestorageapp.consts;

import com.amazonaws.auth.BasicAWSCredentials;

/**
 * Author: Samandar_Akbarov
 * Date: 10/31/2022
 */
public interface AwsProps {
    String KEY = "";
    String SECRET = "";
    String SQS_URL = "";
    String SNS_ARN = "";
    String REGION = "";

    static BasicAWSCredentials getAuth() {
        return new BasicAWSCredentials(KEY, SECRET);
    }

}
