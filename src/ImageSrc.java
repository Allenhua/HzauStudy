import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Hua on 2016/1/22.
 */
public class ImageSrc {

    private CloseableHttpClient httpClient ;

    private String viewState;

    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36";
    //private static String s= "";


    public ImageSrc() {

        this.httpClient = HttpClientBuilder.create().build();
    }

    public  void excuteTask() throws IOException {
        getViewState();
        newImageFrame(getImage());
    }

    private void getViewState() throws IOException {
        HttpGet httpGet = new HttpGet("http://jw.hzau.edu.cn/default2.aspx");
        httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.addHeader("Accept-Encoding","gzip, deflate, sdch");
        httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpGet.addHeader("Cache-Control","max-age=0");
        httpGet.addHeader("Connection","keep-alive");
        httpGet.addHeader("Upgrade-Insecure-Requests","1");
        httpGet.addHeader("User-Agent",userAgent);
        httpGet.addHeader("Host","jw.hzau.edu.cn");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String html = EntityUtils.toString(response.getEntity());

        response.close();
        Document doc = Jsoup.parse(html);
        viewState = doc.getElementsByTag("input").get(0).val();
    }


    private BufferedImage getImage() throws IOException {

        HttpGet httpGet = new HttpGet("http://jw.hzau.edu.cn/CheckCode.aspx");
        httpGet.addHeader("Host","jw.hzau.edu.cn");
        httpGet.addHeader("Referer","http://jw.hzau.edu.cn/default2.aspx");
        httpGet.addHeader("User-Agent",userAgent);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        BufferedImage image = ImageIO.read(response.getEntity().getContent());
        response.close();
        return image;
    }

    private void newImageFrame(BufferedImage image) throws IOException {
        /*JFrame jFrame = new JFrame("验证码");
        JPanel panel = new JPanel();


        JButton login = new JButton("登陆");

        JTextField field = new JTextField(5);
        JLabel label = new JLabel(new ImageIcon(image));
        label.setSize(100,100);
        panel.add(label);
        panel.add(field);
        panel.add(login);

        jFrame.add(panel);
        jFrame.pack();
        jFrame.setSize(300,400);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
*/
        /*login.addActionListener(e -> {
            String imageCode = field.getText().trim();
            System.out.println(imageCode);
            new Thread(new HttpMonitor(viewState,imageCode,httpClient)).start();
        });*/

        String imageCode = "";
        Map<BufferedImage,String> map = OcrTest.loadTrainOcr();

        imageCode = OcrTest.getAllOcr(OcrTest.getImgBinary(image),map);
        System.out.println(imageCode);
        new HttpMonitor(viewState,imageCode,httpClient).run();
    }

    public static void main(String[] args) {
        try {
            ImageSrc is = new ImageSrc();
            is.excuteTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
