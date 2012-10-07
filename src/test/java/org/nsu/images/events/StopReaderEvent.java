package org.nsu.images.events;

import org.nsu.images.io.reader.NetworkReader;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopReaderEvent {
  private NetworkReader reader;

  public StopReaderEvent(NetworkReader reader) {
    this.reader = reader;
  }

  public NetworkReader getReader() {
    return reader;
  }
}
