package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RestExtract {

    public static String ExtractFeaturesRest(String url, String parameter, String tweet)
            throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        List<NameValuePair> lstParameter = new ArrayList<>();
        lstParameter.add(new BasicNameValuePair(parameter, tweet));
        postRequest.setEntity(new UrlEncodedFormEntity(lstParameter));

        HttpResponse response = httpClient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Faild: Http error Code: " + response.getStatusLine().getStatusCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        String output;
        String Val = "";
        String result = "";
        while ((output = br.readLine()) != null) {
            Val += output;
            JsonObject jObj = new Gson().fromJson(Val, JsonObject.class);
            if (parameter.equals("sentence")) {
                result = jObj.get("keyword").getAsString();
            } else {
                result = jObj.get("value").getAsString();
            }
        }
        httpClient.getConnectionManager().shutdown();
        return result;
    }

}
