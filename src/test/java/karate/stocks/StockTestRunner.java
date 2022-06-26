package karate.stocks;

import com.intuit.karate.junit5.Karate;

public class StockTestRunner {
    

    /*
     * @Test void contextLoads() { }
     */
    
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }

}
