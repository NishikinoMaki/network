package nagaseiori.http;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    private static final HttpClient httpClient = httpClientBuilder.build();
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final Logger slowlog = LoggerFactory.getLogger("slow");
    private static final Logger errlog = LoggerFactory.getLogger("error");
    private static final int slow_threshold = 1000;

    public static int httpHead(String url) {
        return httpHead(url, Charset.forName("UTF-8").displayName());
    }

    public static int httpHead(String url, String defaultCharset) {
        return httpHead(httpClient, url, defaultCharset);
    }

    public static int httpHead(HttpClient client, String url, String defaultCharset) {
        HttpHead httpHead = null;
        StatusLine sl = null;
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpHead = new HttpHead(url);
            HttpResponse response = client.execute(httpHead);
            sl = response.getStatusLine();
            return sl.getStatusCode();
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpHead -ERROR- [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpHead -SLOW-  [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            } else {
                log.debug("httpHead -OK-    [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            }
            if (null != httpHead) {
                httpHead.abort();
            }
        }
        return Integer.MIN_VALUE;//
    }

    public static String httpGet(HttpClient client, String url, String defaultCharset) {
        HttpGet httpGet = null;
        String result = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, defaultCharset);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpGet  -ERROR- [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpGet  -SLOW-  [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            } else {
                log.debug("httpGet  -OK-    [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            }
            if (null != httpGet) {
                httpGet.abort();
            }
        }
        return null;
    }

    public static String httpGet(String url) {
        return httpGet(url,  Charset.forName("UTF-8").displayName());
    }

    public static String httpGet(String url, String defaultCharset) {
        return httpGet(httpClient, url, defaultCharset);
    }

    public static String httpPost(HttpClient client, String url, Map<String, Object> map, String defaultCharset) {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();  
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while(it.hasNext()){
            	String key = it.next();
            	String value = String.valueOf(map.get(key));
            	params.add(new BasicNameValuePair(key, value));
            }
            HttpEntity entity = new UrlEncodedFormEntity(params, defaultCharset);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            // if (content.length() > 120) {
            // content = content.substring(0, 50) + "...(" + content.length() + ")..." + content.substring(content.length() - 50, content.length());
            // }
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            }
            if (null != httpPost) {
                httpPost.abort();
            }
        }
        return null;
    }
    
    
    public static String httpPost(HttpClient client, String url, String postContent, String defaultCharset) {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(postContent, defaultCharset);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    postContent,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    postContent
                });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    postContent
                });
            }
            if (null != httpPost) {
                httpPost.abort();
            }
        }
        return null;
    }

    public static String httpPost(HttpClient client, String url, HttpEntity entity, Map<String, String> param, String defaultCharset) throws Exception {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            // httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param,
                });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param
                });
            }
            if (null != httpPost) {
                httpPost.abort();
            }
        }
        return null;
    }

    public static byte[] httpGet4Byte(String url) {
        return httpGet4Byte(httpClient, url);
    }

    public static byte[] httpGet4Byte(HttpClient client, String url) {
        HttpGet httpGet = null;
        byte[] result = null;
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity.isStreaming()) {
                result = EntityUtils.toByteArray(entity);
            }
            return result;
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpGet  -ERROR- [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpGet  -SLOW-  [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            } else {
                log.debug("httpGet  -OK-    [{}]url:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url
                });
            }
            if (null != httpGet) {
                httpGet.abort();
            }
        }
        return null;
    }

    public static String httpPost(String url, String postContent) {
        return httpPost(httpClient, url, postContent,  Charset.forName("UTF-8").displayName());
    }

    public static String httpPost(String url, Map<String, Object> params) {
        return httpPost(httpClient, url, params, Charset.forName("UTF-8").displayName());
    }
    
    public static String httpPost(String url, String postContent, String defaultCharset) {
        return httpPost(httpClient, url, postContent, defaultCharset);
    }

    public static String httpPost(String url, HttpEntity httpEntity, Map<String, String> param, String defaultCharset) throws Exception {
        return httpPost(httpClient, url, httpEntity, param, defaultCharset);
    }
    
    public static String httpPostSetParamInHeaders(HttpClient client, String url, HttpEntity entity, Map<String, String> param, String defaultCharset) throws Exception {
        HttpPost httpPost = null;
        String content = "";
        long before = System.currentTimeMillis();
        Exception ex = null;
        try {
            httpPost = new HttpPost(url);
            if(param != null){
            	for(String key : param.keySet()){
            		 httpPost.setHeader(key, param.get(key));
            	}
            }
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), defaultCharset);
            return content;
        } catch (Exception e) {
            ex = e;
        } finally {
            long span = System.currentTimeMillis() - before;
            if (ex != null) {
                errlog.error("httpPost -ERROR- [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param,
                    ex
                });
            } else if (span >= slow_threshold) {
                slowlog.warn("httpPost -SLOW-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param,
                });
            } else {
                log.debug("httpPost -OK-  [{}]url:{},postContent:{}", new Object[] {
                    HumanReadableUtil.timeSpan(span),
                    url,
                    param
                });
            }
            if (null != httpPost) {
                httpPost.abort();
            }
        }
        return null;
    }
    
    public static String httpPostSetParamInHeaders(String url, HttpEntity entity, Map<String, String> param, String defaultCharset) throws Exception{
    	return httpPostSetParamInHeaders(httpClient, url, entity, param, defaultCharset);
    }
}
