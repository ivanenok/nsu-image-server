package org.nsu.images.io.reader;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import org.nsu.images.domain.ImagePartInfo;
import org.nsu.images.events.ImageReceivedEvent;
import org.nsu.images.utils.HttpDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkReader implements Runnable {
  public static final HttpDownloader INSTANCE = HttpDownloader.getInstance();
  private static Logger LOGGER = LoggerFactory.getLogger(NetworkInputProcessor.class);

  private static final Gson GSON = new Gson();
  private volatile boolean isStopped = false;
  private EventBus bus;
  private String serverUrl;

  NetworkReader(EventBus bus, String serverUrl) {
    this.bus = bus;
    this.serverUrl = serverUrl;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public void stopReader() {
    isStopped = true;
  }

  @Override
  public void run() {
    while (!isStopped) {
      try {
        String response = INSTANCE.getContentString(serverUrl);
        ImagePartInfo imagePartInfo = GSON.fromJson(response, ImagePartInfo.class);
        if (imagePartInfo != null){
          bus.post(new ImageReceivedEvent(imagePartInfo, this));
        }
      } catch (IOException e) {
        LOGGER.warn("Problem on reading data from server", e);
      }
    }
  }

  @Override
  public String toString() {
    return "NetworkReader{" +
        "isStopped=" + isStopped +
        ", bus=" + bus +
        ", serverUrl='" + serverUrl + '\'' +
        '}';
  }
}
