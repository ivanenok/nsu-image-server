package org.nsu.images.io.reader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.nsu.images.events.StopReaderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkInputProcessor {
  private static Logger LOGGER = LoggerFactory.getLogger(NetworkInputProcessor.class);
  private static final ThreadPoolExecutor EXECUTOR  = new ThreadPoolExecutor(5, 100, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(50));
  private static final ArrayList<NetworkReader> READERS = new ArrayList<NetworkReader>();
  private EventBus eventBus;
  private List<String> serversUrls;

  public NetworkInputProcessor(EventBus eventBus, List<String> serversUrls) {
    this.serversUrls = serversUrls;
    this.eventBus = eventBus;
    startRaders();
  }

  @Subscribe
  public void stopReader(StopReaderEvent event){
    int index = READERS.indexOf(event.getReader());
    if (index != -1){
      READERS.remove(index).stopReader();
      LOGGER.debug("Reader stopped: " + event.getReader());
    }
    if (READERS.isEmpty()){
      EXECUTOR.shutdownNow();
    }
  }

  public void stopAllReaders(){
    for (NetworkReader reader : READERS) {
      reader.stopReader();
    }
    READERS.clear();
  }

  public void startRaders() {
    stopAllReaders();
    for (String serversUrl : serversUrls) {
      NetworkReader reader = new NetworkReader(eventBus, serversUrl);
      READERS.add(reader);
      EXECUTOR.submit(reader);
    }
    LOGGER.debug("Readers started.");
  }
}
