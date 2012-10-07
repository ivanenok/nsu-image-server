package org.nsu.images.events;

import org.nsu.images.io.reader.NetworkReader;
import org.nsu.images.domain.ImagePartInfo;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageReceivedEvent {
  private ImagePartInfo partInfo;
  private NetworkReader producer;

  public ImageReceivedEvent(ImagePartInfo partInfo, NetworkReader producer) {
    this.partInfo = partInfo;
    this.producer = producer;
  }

  public ImagePartInfo getPartInfo() {
    return partInfo;
  }

  public NetworkReader getProducer() {
    return producer;
  }
}
