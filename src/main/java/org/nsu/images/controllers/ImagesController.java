package org.nsu.images.controllers;

import org.nsu.images.domain.ImagePartInfo;
import org.nsu.images.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 9/18/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ImagesController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
  private static final int MAX_SLEEP_TIME = 10000;

  private static final HashMap<Integer, Map<Integer, ImagePartInfo>> IMAGES_DATA = new HashMap<Integer, Map<Integer, ImagePartInfo>>();
  private static final int PARTS_COUNT = 3;


  public ImagesController() {
    prepareImagesData();
  }

  private void prepareImagesData() {
    String[] fileNames = new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg"};
    for (int i = 0, fileNamesLength = fileNames.length; i < fileNamesLength; i++) {
      String fileName = fileNames[i];
      try {
        Map<Integer, ImagePartInfo> integerMap = FileUtils.loadImageParts(fileName, PARTS_COUNT);
        IMAGES_DATA.put(i + 1, integerMap);
      } catch (Exception e) {
        LOGGER.error("Problem on loading image from file: " + fileName, e);
      }
    }
  }

  @RequestMapping(value = "/to_hackers.do", method = RequestMethod.GET)
  public String toHackers(){
    return "to_hackers";
  }

  @RequestMapping(value = "/process-request.do", method = RequestMethod.GET)
  public @ResponseBody ImagePartInfo process(@RequestParam("serverId") int serverId, HttpServletRequest request, HttpServletResponse response){
    LOGGER.debug("Request received for server with ID: " + serverId);
    ImagePartInfo imagePartInfo = processRequest(serverId, request, response);
    LOGGER.debug("Request processed for server with ID: " + serverId);
    return imagePartInfo;
  }

  private ImagePartInfo processRequest(int serverId, HttpServletRequest request, HttpServletResponse response) {
    ImagePartInfo result = null;
    ServerBehaviour behaviour = resolverServerBehaviour();
    LOGGER.debug("Behaviour resolved for server. Behaviour: " + behaviour);
    switch (behaviour) {
      case RESPONSE_OK:
        result = getImagePartByServerId(serverId);
        break;
      case RESPONSE_503:
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        break;
      case RESPONSE_CLOSE_CONNECTION:
        // will return empty response for simulation ths behaviour
        break;
      case RESPONSE_WAIT_TIMEOUT:
        sleepSomeTime();
        break;
      default:
        throw new IllegalStateException();
    }
    return result;
  }

  private ImagePartInfo getImagePartByServerId(int serverId) {
    Map<Integer, ImagePartInfo> integerImagePartInfoMap = IMAGES_DATA.get(serverId);
    return integerImagePartInfoMap.get(RANDOM_GENARATOR.nextInt(PARTS_COUNT));
  }

  private void sleepSomeTime() {
    try {
      Thread.sleep(RANDOM_GENARATOR.nextInt(MAX_SLEEP_TIME));
    } catch (InterruptedException e) {
    }
  }

  private static final Random RANDOM_GENARATOR = new Random(System.nanoTime());

  private ServerBehaviour resolverServerBehaviour() {
    return ServerBehaviour.values()[RANDOM_GENARATOR.nextInt(ServerBehaviour.LAST_VALUE.ordinal())];
  }
}
