package org.nsu.images.io.writer;

import com.google.common.io.ByteStreams;
import org.nsu.images.domain.ImagePartInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/7/12
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOutputWriter {
  private final String imageFileName;
  private final ArrayList<ImageDataSupplier> suppliers = new ArrayList<ImageDataSupplier>();

  public ImageOutputWriter(String imageFileName, int partsCount) {
    this.imageFileName = imageFileName;
      for (int i = 0; i < partsCount; i++) {
        ImageDataSupplier supplier = new ImageDataSupplier();
        suppliers.add(supplier);
      }
  }

  public String getImageFileName() {
    return imageFileName;
  }

  public void putData(ImagePartInfo partInfo) {
    ImageDataSupplier supplier = suppliers.get(partInfo.getImagePartNumber() - 1);
    if (supplier != null){
      if (!supplier.isDataCollected()){
        supplier.putImagePart(partInfo);
      }
    }
  }

  public boolean isAllDataCollected() {
    boolean result = true;
    for (ImageDataSupplier supplier : suppliers) {
      result &= supplier.isDataCollected();
    }
    return result;
  }

  public byte[] getData() throws IOException {
    return ByteStreams.toByteArray(ByteStreams.join(suppliers));
  }
}
