package util;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 外部资源加载类，可以从jar中读取内容
 * @author miukoo
 * @see  <a href="https://gitee.com/miukoo/yang-liao-ge-yangy.git">project home</a>
 * @see  <a href="http://gjsm.cn/">athor website</a>
 */
public class ReadResourceUtil {

    static Random random = new Random();
    static File jarFile;

    static {
        try {
            jarFile = new File(java.net.URLDecoder.decode(ReadResourceUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取音频
     */
    public static URL readAudio(String name){
        return getUri("/static/audio/wav" +random.nextInt(2)+"/"+name);
    }

    public static URL getUri(String name){
        URL resource = ReadResourceUtil.class.getResource(name);
        if(resource==null){
            try {
                File file = new File(jarFile.getParentFile(), "static");
                System.out.println(file.getAbsolutePath()+"\t"+file.exists());
                if(file.exists()) {
                    resource = new File(jarFile.getParentFile(), name).toURL();
                }else{
                    resource = new File(jarFile.getParentFile().getParentFile(), name).toURL();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    /**
     * 读取图片
     */
    private static String readSkinPath(boolean isRandom){
        if(isRandom){
            return "/static/skins/a" +random.nextInt(2);
        }else{
            return "/static/skins/a3";
        }
    }

    /**
     * 读取图片
     */
    public static List<String> readSkin(boolean isRandom){
        String path = readSkinPath(isRandom);
        System.out.println(path);
        List<String> list = new ArrayList<>();
        try {
            URL url = getUri("/" + path);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        list.add(path+"/"+app.getName());
                    }
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
