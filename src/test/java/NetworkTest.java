import com.miiicasa.casa.exception.NetworkException;
import com.miiicasa.casa.network.Network;

import org.junit.Test;

/**
 * Created by showsky on 15/3/2.
 */
public class NetworkTest {

    private final static String BASE_API = "http://api.ihuihe.cn";
    private final static String API_VERSION = BASE_API + "/open/apk";

    @Test
    public void testGet() {
        try {
            String response = Network.getInstance().get(API_VERSION, null);
            System.out.println(response);
        } catch (NetworkException networkExcpetion) {
            networkExcpetion.printStackTrace();
        }
    }

    @Test
    public void testPost() {
        try {
            String response = Network.getInstance().get(API_VERSION, null);
            System.out.println(response);
        } catch (NetworkException networkExcpetion) {
            networkExcpetion.printStackTrace();
        }
    }
}
