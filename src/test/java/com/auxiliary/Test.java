package com.auxiliary;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/11
 */
public class Test {

    public static void getImagePixel(String image, Color targetCorlor) throws Exception {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        System.out.println("width=" + width + ",height=" + height + ".");
        System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        bi=removeLine(bi,7);
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
//                if (pixel != targetCorlor.getRGB()) {
//                    bi.setRGB(i,j,0xFFF);
//                    continue;
//                }
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
                        + rgb[1] + "," + rgb[2] + ")");
            }
        }
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = it.next();
        File f = new File("d://update02.png");
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
    }

    private static BufferedImage removeLine(BufferedImage img, int px) {
        if (img != null) {
            int width = img.getWidth();
            int height = img.getHeight();

            for (int x = 0; x < width; x++) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int y = 0; y < height; y++) {
                    int count = 0;
                    while (y < height - 1 && img.getRGB(x, y) != Color.white.getRGB()) {
                        count++;
                        y++;
                    }
                    if (count <= px && count > 0) {
                        for (int i = 0; i <= count; i++) {
                            list.add(y - i);
                        }
                    }
                }
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        img.setRGB(x, list.get(i), Color.white.getRGB());
                    }
                }
            }
        }
        return img;

    }

    public static void main(String[] args) {

        try {
            getImagePixel("D://update.png",new Color(255,255,255));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 3; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    System.out.println(threadName);
//                    int j = 0;
//                    try {
//                        while (true) {
//                            System.out.println(threadName + ":" + ++j);
//                            Thread.sleep(2000);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//            newFixedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    System.out.println(threadName);
//                    int j = 0;
//                    try {
//                        while (true) {
//                            System.out.println(threadName + ":" + ++j);
//                            Thread.sleep(2000);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }
}
