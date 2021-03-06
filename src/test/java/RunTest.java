import com.miiicasa.casa.thread.Run;
import com.miiicasa.casa.thread.ThreadListener;

import org.junit.Test;

/**
 * Created by showsky on 15/3/3.
 */
public class RunTest {

    @Test
    public void jobTest() {
        Run.CasaTask a = Run.getInstance().submit(new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Throwable e) {
                System.err.println(e.getMessage());
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

        Run.CasaTask b = Run.getInstance().submit(new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Throwable e) {
                System.err.println(e.getMessage());
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

        Run.CasaTask c = Run.getInstance().submit(new ThreadListener<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("Result: " + result);
            }

            @Override
            public void onFail(Throwable e) {
                System.err.println(e.getMessage());
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
