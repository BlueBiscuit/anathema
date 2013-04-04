package net.sf.anathema.platform.tool;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.sf.anathema.platform.fx.ResourceLoader;
import net.sf.anathema.platform.fx.NodeHolder;

import java.io.InputStream;

public class SetImage implements Runnable {
  private final NodeHolder<ImageView> imageView;
  private final String relativePath;

  public SetImage(NodeHolder<ImageView> imageView, String relativePath) {
    this.imageView = imageView;
    this.relativePath = relativePath;
  }

  @Override
  public void run() {
    imageView.getNode().setImage(createImage(relativePath));
  }

  private Image createImage(String pathToImage) {
    ResourceLoader resourceLoader = new ResourceLoader();
    InputStream imageStream = resourceLoader.loadResource(pathToImage);
    return new Image(imageStream, 20, 20, true, true);
  }
}