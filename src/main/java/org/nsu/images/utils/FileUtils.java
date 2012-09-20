package org.nsu.images.utils;

import org.apache.commons.codec.binary.Base64;
import org.nsu.images.domain.ImagePartInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 9/18/12
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {
  public static HashMap<Integer, ImagePartInfo> loadImageParts(String fileName, int partsCount) throws IOException, URISyntaxException {
    HashMap<Integer, ImagePartInfo> result = new HashMap<Integer, ImagePartInfo>();
    URL resource = FileUtils.class.getClassLoader().getResource("images/" + fileName);
    File f = new File(resource.toURI());
    FileInputStream fin = null;
    FileChannel ch = null;
    try {
      fin = new FileInputStream(f);
      ch = fin.getChannel();
      int size = (int) ch.size();
      MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, size);
      byte[] imageData = new byte[size];
      buf.get(imageData);
      int partSize = size / (partsCount - 1);
      int lastPartSize = size - ((partsCount - 1) * partSize);
      for (int i = 0; i < partsCount; i++){
        ImagePartInfo partInfo = new ImagePartInfo();
        partInfo.setImagePartNumber(i + 1);
        partInfo.setImageName(fileName);
        partInfo.setSizeOfImageInBytes(size);
        byte[] bytes = i == (partsCount - 1) && lastPartSize != 0 ? new byte[lastPartSize] : new byte[partSize];
        partInfo.setSizeOfPartInBytes(bytes.length);
        bytes = Arrays.copyOfRange(imageData, i * partSize, (i * partSize) + bytes.length);
        partInfo.setBase64Data(Base64.encodeBase64String(bytes));
        result.put(i + 1, partInfo);
      }
    } finally {
        if (fin != null) {
          fin.close();
        }
        if (ch != null) {
          ch.close();
        }
    }
    return result;
  }
}
