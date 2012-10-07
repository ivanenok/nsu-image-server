package org.nsu.images.io.writer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import org.nsu.images.events.ImageReceivedEvent;
import org.nsu.images.events.StopReaderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagesOutputProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImagesOutputProcessor.class);

  private final EventBus eventBus;
  private final String outputPath;
  private CountDownLatch countDownLatch;
  private final HashMap<String, ImageOutputWriter> writers = new HashMap<String, ImageOutputWriter>();

  public ImagesOutputProcessor(List<String> imagesFileNames, int imagesPartsCount, EventBus eventBus, String outputPath, CountDownLatch countDownLatch) throws IOException, URISyntaxException {
    this.eventBus = eventBus;
    this.outputPath = outputPath;
    this.countDownLatch = countDownLatch;
    prepareImagesDir(outputPath);
    for (String imagesFileName : imagesFileNames) {
      writers.put(imagesFileName, new ImageOutputWriter(imagesFileName, imagesPartsCount));
    }
    eventBus.register(this);
  }

  private void prepareImagesDir(String outputPath) throws IOException {
    File file = new File(outputPath);
    if (!file.exists()){
      if (!file.mkdir()){
        throw new IOException("Problem on creation root dir for images");
      }
    }
  }

  @Subscribe
  public void processImageData(ImageReceivedEvent event){
    String imageName = event.getPartInfo().getImageName();
    if (writers.containsKey(imageName)){
      ImageOutputWriter imageOutputWriter = writers.get(imageName);
      if (!imageOutputWriter.isAllDataCollected()) {
        imageOutputWriter.putData(event.getPartInfo());
        if (imageOutputWriter.isAllDataCollected()){
          File file = new File(outputPath + imageOutputWriter.getImageFileName());
          try {
            Files.write(imageOutputWriter.getData(), file);
          } catch (IOException e) {
            LOGGER.error("Problem on storing info to file: " + file);
          }
          eventBus.post(new StopReaderEvent(event.getProducer()));
          countDownLatch.countDown();
        }
      }
    } else {
      LOGGER.error("Unexpected image name: " + imageName);
    }
  }
}
