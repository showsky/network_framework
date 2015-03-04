import com.miiicasa.casa.exception.NetworkException;
import com.miiicasa.casa.network.Network;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by showsky on 15/3/2.
 */
public class NetworkTest {

    private final static String BASE_API = "http://api.ihuihe.cn";
    private final static String API_VERSION = BASE_API + "/open/apk";
    private final static String TEST_URL = "http://192.168.79.215/~ting_cheng/test.php";

    @Test
    public void testGet() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "ting_cheng");
        values.put("email", "ting_cheng@miiicasa.com");
        values.put("address", "电 脑");
        try {
            String response = Network.getInstance().get(TEST_URL, values);
            System.out.println(response);
        } catch (NetworkException networkExcpetion) {
            networkExcpetion.printStackTrace();
        }
    }

    @Test
    public void testPost() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "ting_cheng");
        values.put("email", "ting_cheng@miiicasa.com");
        values.put("address", "电 脑");
        try {
            String response = Network.getInstance().post(TEST_URL, values);
            System.out.println(response);
        } catch (NetworkException networkExcpetion) {
            networkExcpetion.printStackTrace();
        }
    }
}
