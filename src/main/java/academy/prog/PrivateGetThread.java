package academy.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PrivateGetThread implements Runnable {
    private Gson gson;
    private int n; // /get?from=n
    private static String to;
    private static String from;

    public PrivateGetThread() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static void setTo(String nameTo) {
        to = nameTo;
    }

    public static void setFrom(String nameFrom) {
        from = nameFrom;
    }

    @Override
    public void run() { // WebSockets
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/getprivate?fromprivate=" + n + "&from=" + from);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                InputStream is = http.getInputStream();
                try {
                    byte[] buf = responseBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    PrivateJsonMessages list = gson.fromJson(strBuf, PrivateJsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            n++;
                        }
                    }
                } finally {
                    is.close();
                }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
              ex.printStackTrace();
        }
    }


    private byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
