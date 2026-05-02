package in.azhar.S3FileUpload.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public Order createRazorpayOrder(int amount) throws Exception {

        RazorpayClient client = new RazorpayClient(key, secret);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // paise me
        options.put("currency", "INR");
        options.put("receipt", "txn_123");

        return client.orders.create(options);
    }

    // verify signature
    public boolean verifyPayment(String orderId, String paymentId, String signature) {

        try {
            String data = orderId + "|" + paymentId;
            String generatedSignature = hmacSHA256(data, secret);
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSHA256(String data, String secret) throws Exception {

        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec keySpec =
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");

        mac.init(keySpec);

        byte[] hash = mac.doFinal(data.getBytes());

        return new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));
    }
}
