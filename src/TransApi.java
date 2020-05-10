import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class TransApi {

    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";
    private static final String APP_ID = "2f298534dea096b2";
    private static final String SECURITY_KEY = "miwxsCUq7N9j6x3nJ4emwsnmrEC0tGcD";

    public String buildParams(String query, String from, String to) throws IOException {

        Map<String, String> params = new HashMap<>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", from);
        params.put("to", to);
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_ID + truncate(query) + salt + curtime + SECURITY_KEY;
        String sign = getDigest(signStr);
        params.put("appKey", APP_ID);
        params.put("q", query);
        params.put("salt", salt);
        params.put("sign", sign);
        return requestForHttp(YOUDAO_URL, params);
    }

    public static String requestForHttp(String url, Map<String, String> params) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<>();
        //entrySet() 返回此地图中包含的映射的Set视图
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        String json = null;
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        //编码
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            json = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}