package per.yan.email.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import per.yan.email.EmailApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author gaoyan
 * @date 2018/10/26 17:55
 */
@Slf4j
public class NetFileUtils {

    /**
     * 默认下载路径
     */
    private static String SLASH = "/";
    public static String DOWNLOAD_PATH = EmailApplication.class.getResource(SLASH).getPath();
    private static final String TEMP_PATH = DOWNLOAD_PATH + "temp";
    private static String POINT = ".";

    public static File downloadFile(String urlPath, String fileName) {
        File file = null;
        URL url;
        try {
            url = new URL(urlPath);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            String filePathUrl = httpConn.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
            //下载时使用用户设置的文件名
            if (!StringUtils.isEmpty(fileName) && fileFullName.lastIndexOf(POINT) > fileFullName.lastIndexOf(SLASH)) {
                fileFullName = fileFullName.substring(0, fileFullName.lastIndexOf(SLASH) + 1) + fileName + fileFullName.substring(fileFullName.lastIndexOf(POINT));
            }
            BufferedInputStream bin = new BufferedInputStream(httpConn.getInputStream());
            String path = assemblePath() + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            bin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void cleanFileFinally(String path) {
        clean(new File(path));
    }

    private static void clean(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File child : files) {
                        if (child.isDirectory()) {
                            clean(child);
                        } else {
                            child.delete();
                        }
                    }
                }
            }
            file.delete();
        }
    }

    public static String assemblePath() {
        return TEMP_PATH + SLASH + String.valueOf(Thread.currentThread().getId()) + SLASH;
    }
}
