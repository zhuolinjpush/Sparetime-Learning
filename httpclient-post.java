package cn.jpush.abtest.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.jpush.abtest.iceclient.ICEClientImpl;
import cn.jpush.utils.SystemConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ActiveUserFilterHelper {

    private static Logger logger = LogManager.getLogger(ICEClientImpl.class);
    private static final String CHARSET = "utf-8";
    private String filterUrl;
    private CloseableHttpClient httpClient;
    
    public ActiveUserFilterHelper() {
        filterUrl = SystemConfig.getProperty("active_user.filter.post.url");
        logger.info("filter url:" + this.filterUrl);
        init();
    }
    
    public void init() {
        try {
            this.httpClient = HttpClients.createDefault();
        } catch (Exception e) {
            logger.error("init error", e);
        }
    }
    
    public String doPost(String url, String body, String charset) {
        Long start = System.currentTimeMillis();
        HttpPost httpPost = null;
        StringEntity entity = null;
        HttpResponse response = null;
        HttpEntity resEntity = null;
        String result = null;
        try {
            entity = new StringEntity(body);  
            entity.setContentEncoding(charset);  
            entity.setContentType("application/json"); 
            httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            response =  httpClient.execute(httpPost);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, charset);
                    }
                } else {
                    resEntity = response.getEntity();
                    if (resEntity != null) {
                        logger.info("response:" + EntityUtils.toString(resEntity, charset));
                    }
                }
                
            } else {
                logger.info("response is null");
            }
        } catch (Exception e) {
            logger.error("doPost error", e);
        } finally {
            //
        }
        logger.info("dopost cost=" + (System.currentTimeMillis() - start));
        return result;
    }
    
    public void closeHttpClient() {
        try {
            if (httpClient != null) {
                this.httpClient.close();
            }
            logger.info("close http client");
        } catch (Exception e) {
            logger.error("close http client error", e);
        }
    }
    
    public List<Long> getActiveUserUidList(String body) {
        //{"appkey":"edwardAppkey","platform":"a","testId":"testId1","activeDay":30,"uids":[8909257245,7898692779]}
        List<Long> uidList = new ArrayList<Long>();
        try {
            String result = doPost(this.filterUrl, body, CHARSET);
            //{"uids":[8909257245,7898692779]}
            if(null != result) {
                JSONObject obj = JSON.parseObject(result);
                JSONArray uidsArray = obj.getJSONArray("uids");
                for (int i = 0; i < uidsArray.size(); i++) {
                    uidList.add(uidsArray.getLong(i));
                }
            }
            logger.info("post response uids size=" + uidList.size());
        } catch (Exception e) {
            logger.error("get active user uidlist error", e);
        }
        return uidList;
    }
    
    public static void main(String[] args) {
        ActiveUserFilterHelper helper = new ActiveUserFilterHelper();
        String body = "{\"appkey\":\"5c883e3cbf64854eeae76eb3\",\"platform\":\"i\",\"uids\":[8739625000,8780521835,8964085914,8780504029,8739922336,8780968836,9353759733,5809415258,8748724009,8738295031,8748549931,8959992616,8887821260,8958254339,8887507483,5809466751,8780779182,5805197292,9018464685,9063580664,8958347973,9058036797,8964176978],\"activeDay\":30,\"testId\":\"b56a5942-aef5-487e-b3ad-37f0426d0856,1d222267-ace6-4e36-8572-e355486d8cc8\"}";
        helper.getActiveUserUidList(body);
    }
}

