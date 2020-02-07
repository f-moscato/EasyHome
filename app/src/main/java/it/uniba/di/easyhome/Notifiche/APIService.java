package it.uniba.di.easyhome.Notifiche;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Autorizathion:key-AAAAmWNFMKw:APA91bGCQC7RVXwjwdmhA_KLybLj__jVioWgcKekRCeUNpGROsOra0fmFxrUdnr_NcLpLzks0FJ7fvr2XQ3AZ4zPu_V0q2CAbmR1cl4QT8pGcDxHlRus0R-mSwMQ0B837k6xFfAtmr9C"
    })

    @POST("fcm/send")
    Call<MyRespnse> sendNotification(@Body Sender body);
}
