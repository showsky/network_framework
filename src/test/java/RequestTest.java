import com.miiicasa.casa.thread.Request;
import com.miiicasa.casa.thread.ThreadListener;

import org.junit.Test;

/**
 * Created by showsky on 15/3/3.
 */
public class RequestTest {

    @Test
    public void jobTest() {
        Request.CasaTask a = Request.getInstance().submit("ok", new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) throws Exception {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Exception exception) {
                System.err.println(exception.getMessage());
            }

            @Override
            public String call() throws Exception {
                System.out.println("[a start]");
                for (int i = 0; i < 10; i++) {
                    System.out.print(i);
                    Thread.sleep(1000);

                    if (i == 3) {
                        throw new Exception("Error");
                    }
                }
                System.out.println("\n[a end]");
                return "a ok";
            }
        });

        Request.CasaTask b = Request.getInstance().submit("ok", new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) throws Exception {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Exception exception) {
                System.err.println(exception.getMessage());
            }

            @Override
            public String call() throws Exception {
                System.out.println("[b start]");
                for (int i = 0 ; i < 10; i++) {
                    System.out.print(i);
                    Thread.sleep(1000);
                }
                System.out.println("\n[b end]");
                return "b ok";
            }
        });

        Request.CasaTask c = Request.getInstance().submit("ok", new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) throws Exception {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Exception exception) {
                System.err.println(exception.getMessage());
            }

            @Override
            public String call() throws Exception {
                System.out.println("[c start]");
                for (int i = 0 ; i < 10; i++) {
                    System.out.print(i);
                    Thread.sleep(1000);
                }
                System.out.println("\n[c end]");
                return "c ok";
            }
        });


        System.out.println("--- start ---");
        while ( ! a.isDone() || ! b.isDone() || ! c.isDone()) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {}
        }
        System.out.println("--- end ---");
    }
}
