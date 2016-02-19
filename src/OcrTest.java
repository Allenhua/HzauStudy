

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Allen on 2016/1/27.
 */
public class OcrTest {

    private static int index = 0;

    public static void downloadImage(int i) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://jw.hzau.edu.cn/CheckCode.aspx");

        CloseableHttpResponse response;

        response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200){
            BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
            FileOutputStream fos = new FileOutputStream(new File
                    (System.getProperty("user.dir")+File.separator+"test"+File.separator+i+".gif"));
            byte[] buff = new byte[1024];

            int length;
            while ((length = bis.read(buff,0,1024)) > -1){
                fos.write(buff);
                fos.flush();
            }

            System.out.println(i+".gif "+"已保存！！");
            fos.close();
            bis.close();
        }

        response.close();
        return;
    }

   /* public static int whiteOrBlack(int colorRgb){
        Color color = new Color(colorRgb);
        if (color.getRed()+color.getBlue()+color.getGreen() >153){
            return 1;//white
        }else
            return 0;//black
    }*/

    public static int isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
            return 1;
        }
        return 0;
    }

    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 600) {
            return 1;
        }
        return 0;
    }
    public static int isBlue(int colorInt) {
        Color color = new Color(colorInt);
        int rgb = color.getRed() + color.getGreen() + color.getBlue();
        if (rgb == 153) {
            return 1;
        }
        return 0;
    }

    public static BufferedImage getImgBinary(BufferedImage bi) throws IOException {

        //BufferedImage bi = ImageIO.read(file);
        bi = bi.getSubimage(5,1,bi.getWidth()-5,bi.getHeight()-2);
        bi = bi.getSubimage(0,0,50,bi.getHeight());
        int width = bi.getWidth();
        int height = bi.getHeight();
        System.out.println(width+" "+height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j <height ; j++) {
                int rgb = bi.getRGB(i,j);
                if (isBlue(rgb) == 1){
                    bi.setRGB(i,j,Color.BLACK.getRGB());
                }else {
                  bi.setRGB(i,j,Color.WHITE.getRGB());
                }
                //nbi.setRGB(i,j,rgb);

            }
        }

        return bi;
    }

    public static List getCharSplit(BufferedImage image) throws IOException {

        List<BufferedImage> biList = new ArrayList<>();

        /*BufferedImage image = ImageIO.read(
                new File(System.getProperty("user.dir")+File.separator+"binary"+File.separator+s+".gif"));*/
        //List<Integer> list = new ArrayList<>();
        int width = image.getWidth()/4;
        int height = image.getHeight();

        biList.add(image.getSubimage(0,0,width,height));
        biList.add(image.getSubimage(width,0,width,height));
        biList.add(image.getSubimage(width*2,0,width,height));
        biList.add(image.getSubimage(width*3,0,width,height));

        System.out.println(biList.size());
        return biList;
    }

    public static Map loadTrainOcr() throws IOException {
        Map<BufferedImage,String> map = new HashMap<>();
        File file = new File(System.getProperty("user.dir") + File.separator + "train\\");
        File[] files = file.listFiles();
        for (File f:files) {
            map.put(ImageIO.read(f),f.getName().charAt(0)+"");
        }
        System.out.println("训练完毕！！！");
        return map;
    }

    private static String charOcr(BufferedImage image,Map<BufferedImage,String> map){

        String s = "";
        int width = image.getWidth();
        int height = image.getHeight();
        int min = width*height;
        for (BufferedImage bi:map.keySet()) {
            int count = 0;
            if (Math.abs(bi.getWidth()-width) > 2)
                continue;
            int widthmin = width < bi.getWidth() ? width : bi.getWidth();
            int heightmin = height < bi.getHeight() ? height : bi.getHeight();
            loop:for (int i = 0; i < widthmin; i++) {
                for (int j = 0; j < heightmin; j++) {
                    if (isBlack(bi.getRGB(i,j)) != isBlack(image.getRGB(i,j))){
                        count++;
                    }
                    if (count >= min)
                        break loop;
                }
            }

            if (count < min){
                min = count;
                s = map.get(bi);
            }
        }
        return s;
    }

    public static void trainOcr() throws IOException {
        File file = new File(System.getProperty("user.dir")+File.separator+"temp\\");
        File[] files = file.listFiles();
        for (File f:files) {
            List<BufferedImage> list = getCharSplit(getImgBinary(ImageIO.read(f)));
            if (list.size() == 4){
                for (int i = 0; i < list.size(); i++) {
                    ImageIO.write(list.get(i),"gif",new File(System.getProperty("user.dir")+File.separator+"train\\"
                            +f.getName().charAt(i)+"-"+(index++)+".gif"));
                }
            }
        }
        System.out.println("success trained!!");
    }

    public static String getAllOcr(BufferedImage imgBinary,Map<BufferedImage,String> map) throws IOException {

        String result = "";
        List<BufferedImage> splitList = getCharSplit(imgBinary);
        for (BufferedImage img:splitList) {
            result += charOcr(img,map);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        /*for (int i = 0; i < 20; i++) {
            downloadImage(i);
        }
        System.out.println("下载完毕！！！");*/

        /*for (int i = 0; i < 50; i++) {
            getCharSplit(i);
        }*/
        //trainOcr();

       /* BufferedImage imgBinary = getImgBinary(new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator + "test.gif"));
        List<BufferedImage> splitList = getCharSplit(imgBinary);
        Map<BufferedImage,String> map = loadTrainOcr();
        for (BufferedImage img:splitList) {
            System.out.println(charOcr(img,map));
        }*/
        File fil = new File(System.getProperty("user.dir") + File.separator + "test\\" );
        Map<BufferedImage,String> map = loadTrainOcr();
        File[] fiels = fil.listFiles();

        for (File f:fiels) {
            BufferedImage imgBinary = getImgBinary(ImageIO.read(f));
            System.out.println(getAllOcr(imgBinary,map));;
        }

    }
}
