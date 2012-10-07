package org.nsu.images;

import com.google.common.eventbus.EventBus;
import org.nsu.images.io.reader.NetworkInputProcessor;
import org.nsu.images.io.writer.ImagesOutputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 9/25/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagesLoaderExample {
  public static final Logger LOGGER = LoggerFactory.getLogger(ImagesLoaderExample.class);

  private static final EventBus EVENT_BUS = new EventBus();

  private static final String SERVER_URL = "http://localhost";
  private static final String OUTPUT_PATH = "W:\\test-images\\";

  private static final int IMAGES_PARTS_COUNT = 3;
  private static final int IMAGES_COUNT = 5;
  private static final int SERVERS_COUNT = 5;


  public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {

    final CountDownLatch countDownLatch = new CountDownLatch(IMAGES_COUNT);

    ImagesOutputProcessor outputProcessor = new ImagesOutputProcessor(getImagesNames(), IMAGES_PARTS_COUNT, EVENT_BUS, OUTPUT_PATH, countDownLatch);
    EVENT_BUS.register(outputProcessor);

    NetworkInputProcessor inputProcessor = new NetworkInputProcessor(EVENT_BUS, getServerUrls());
    EVENT_BUS.register(inputProcessor);

    countDownLatch.await();
    EVENT_BUS.unregister(inputProcessor);
    EVENT_BUS.unregister(outputProcessor);
  }

  private static List<String> getImagesNames() {
    ArrayList<String> imageNames = new ArrayList<String>();
    for (int imageIdx = 1; imageIdx <= IMAGES_COUNT; imageIdx++) {
      imageNames.add("image" + imageIdx + ".bmp");
    }
    return imageNames;
  }

  private static List<String> getServerUrls() {
    ArrayList<String> serverUrls = new ArrayList<String>();
    for (int serverIdx = 1; serverIdx <= SERVERS_COUNT; serverIdx++) {
      serverUrls.add(SERVER_URL + "/server" + serverIdx);
    }
    return serverUrls;
  }
}
