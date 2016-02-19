import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hua on 2016/1/22.
 */
public class HttpMonitor {

    String viewState;
    String imageCode;

    private CloseableHttpClient httpClient;

    public HttpMonitor(String viewState, String imageCode, CloseableHttpClient httpClient) {
        this.viewState = viewState;
        this.imageCode = imageCode;
        this.httpClient = httpClient;
    }

    private boolean getLoginState() throws UnsupportedEncodingException {

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("__VIEWSTATE", this.viewState));
        nvps.add(new BasicNameValuePair("txtUserName", "XXXXXXXXXXXXX"));//在此输入你的学号
        nvps.add(new BasicNameValuePair("TextBox2","XXXXXXX"));//在此输入你的密码
        nvps.add(new BasicNameValuePair("txtSecretCode", imageCode));
        nvps.add(new BasicNameValuePair("RadioButtonList1", "学生"));
        nvps.add(new BasicNameValuePair("Button1", "登录"));



        HttpPost httpost = new HttpPost("http://jw.hzau.edu.cn/default2.aspx");

        httpost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpost.addHeader("Accept-Encoding","gzip, deflate, sdch");
        httpost.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpost.addHeader("Cache-Control","max-age=0");
        httpost.addHeader("Connection","keep-alive");
        httpost.addHeader("Host","jw.hzau.edu.cn");
        httpost.addHeader("Referer","http://jw.hzau.edu.cn/default2.aspx");
        httpost.addHeader("Upgrade-Insecure-Requests","1");
        httpost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36");

        httpost.setEntity(new UrlEncodedFormEntity(nvps,"GB2312"));


        CloseableHttpResponse response;
        try {
            response = this.httpClient.execute(httpost);
		/*登陆成功后会返回302跳转*/
            //System.out.println(EntityUtils.toString(response.getEntity()));
            String result = response.getStatusLine().toString();
            System.out.println(result);
            if(result.equals("HTTP/1.1 302 Found")){
                response.close();
                return true;
            }else{
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void getStuName(){

    }

    public void getSchedule() throws IOException {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("xh","XXXXXXXXXXX"));
        list.add(new BasicNameValuePair("xm","华昕"));//在此填入姓名
        list.add(new BasicNameValuePair("gnmkdm","N121608"));

        String s = URLEncodedUtils.format(list,"GB2312");
        HttpGet httpGet = new HttpGet("http://jw.hzau.edu.cn/xsjccx.aspx?"+s);
        System.out.println(s);
        httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.addHeader("Accept-Encoding","gzip, deflate, sdch");
        httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpGet.addHeader("Cache-Control","max-age=0");
        httpGet.addHeader("Connection","keep-alive");
        httpGet.addHeader("Host","jw.hzau.edu.cn");
        httpGet.addHeader("Referer","http://jw.hzau.edu.cn/xs_main.aspx?xh=XXXXXXXXXXXXX");
        httpGet.addHeader("Upgrade-Insecure-Requests","1");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36");

        CloseableHttpResponse response;
        System.out.println("正在查询教材信息…………");
        response = httpClient.execute(httpGet);
        System.out.println("正在查询教材信息…………");
        int staus = response.getStatusLine().getStatusCode();


        if (staus == 200){
            System.out.println("查询教材信息成功！");
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }


    public void run() {
        try {
            //System.out.println(getLoginState());

            if (getLoginState()){
                System.out.println("登陆成功！！");
                getSchedule();

            }else {
                System.out.println("验证码错误！！");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("查询失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //getUrl("","");
    }
}
