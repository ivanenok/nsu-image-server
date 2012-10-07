package org.nsu.images.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 9/18/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePartInfo implements Serializable{
  private long sizeOfPartInBytes;
  private long sizeOfImageInBytes;
  private String imageName;
  private int imagePartNumber;
  private String base64Data;

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public long getSizeOfPartInBytes() {
    return sizeOfPartInBytes;
  }

  public void setSizeOfPartInBytes(long sizeOfPartInBytes) {
    this.sizeOfPartInBytes = sizeOfPartInBytes;
  }

  public long getSizeOfImageInBytes() {
    return sizeOfImageInBytes;
  }

  public void setSizeOfImageInBytes(long sizeOfImageInBytes) {
    this.sizeOfImageInBytes = sizeOfImageInBytes;
  }

  public int getImagePartNumber() {
    return imagePartNumber;
  }

  public void setImagePartNumber(int imagePartNumber) {
    this.imagePartNumber = imagePartNumber;
  }

  public String getBase64Data() {
    return base64Data;
  }

  public void setBase64Data(String base64Data) {
    this.base64Data = base64Data;
  }

  @Override
  public String toString() {
    return "ImagePartInfo{" +
        "sizeOfPartInBytes=" + sizeOfPartInBytes +
        ", sizeOfImageInBytes=" + sizeOfImageInBytes +
        ", imageName='" + imageName + '\'' +
        ", imagePartNumber=" + imagePartNumber +
        '}';
  }
}
