package academy.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UserStatusGetThread implements Runnable {
    private final Gson gson;
    private static String userName;

    public UserStatusGetThread() {
        gson = new GsonBuilder().create();
    }

    public static void setUserName(String userName) {
        UserStatusGetThread.userName = userName;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/getstatusto?statusto=" + userName);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                InputStream is = http.getInputStream();
                try {
                    byte[] buf = responseBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    UserStatusJsonMessages status = gson.fromJson(strBuf, UserStatusJsonMessages.class);
                    if (status != null) {
                        System.out.println(status.getStatus());
                    }
                } finally {
                    is.close();
                }
                break;
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
