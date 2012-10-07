package org.nsu.images.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by www.propertyminder.com.
 * User: Maxim S. Ivanov
 * Date: 2/29/12
 * Time: 3:23 PM
 */
public class HttpDownloader {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpDownloader.class);

  private static final DefaultHttpClient DEFAULT_HTTP_CLIENT;

  public static final int MAX_THREADS_FOR_SYNC_REQUEST = 200;

  static {
    ThreadSafeClientConnManager safeClientConnManager = new ThreadSafeClientConnManager();
    safeClientConnManager.setDefaultMaxPerRoute(MAX_THREADS_FOR_SYNC_REQUEST);
    safeClientConnManager.setMaxTotal(MAX_THREADS_FOR_SYNC_REQUEST);
    DEFAULT_HTTP_CLIENT = new DefaultHttpClient(safeClientConnManager);
  }

  private HttpClient httpClient;

  private HttpDownloader(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public static HttpDownloader getInstance(){
    return new HttpDownloader(DEFAULT_HTTP_CLIENT);
  }

  private HttpResponse getHttpResponse(String url) throws IOException{
    HttpUriRequest request = new HttpGet(url);
    return httpClient.execute(request);
  }

  public JsonElement getContentJSON(String url) throws IOException{
    final String contentString = getContentString(url);
    return new JsonParser().parse(contentString);
  }

  public String getContentString(String url) throws IOException{
    String result = null;
    final HttpResponse response = getHttpResponse(url);
    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
      result = EntityUtils.toString(response.getEntity(), "UTF-8");
    } else {
      LOGGER.error("Unexpected response: " + response.getStatusLine().toString());
    }
    return result;
  }

  public InputStream getContentStream(String url) throws IOException{
    InputStream result = null;
    final HttpResponse response = getHttpResponse(url);
    if (response.getStatusLine().getStatusCode() == 200){
      result = new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
    } else {
      LOGGER.error("Unexpected response: " + response.getStatusLine().toString());
    }
    return result;
  }
}
