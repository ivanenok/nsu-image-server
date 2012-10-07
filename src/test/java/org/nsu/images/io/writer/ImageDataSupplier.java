package org.nsu.images.io.writer;

import com.google.common.io.InputSupplier;
import org.apache.commons.codec.binary.Base64;
import org.nsu.images.domain.ImagePartInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */

public class ImageDataSupplier implements InputSupplier<InputStream> {
  private static Logger LOGGER = LoggerFactory.getLogger(ImageDataSupplier.class);

  private byte[] data = null;

  @Override
  public InputStream getInput() throws IOException {
    return new ByteArrayInputStream(data);
  }

  public void putImagePart(ImagePartInfo partInfo){
    String base64Data = partInfo.getBase64Data();
    data = Base64.decodeBase64(base64Data);
    LOGGER.debug("Data collected to supplier: " + partInfo);
  }

  public boolean isDataCollected() {
    return data != null;
  }
}

