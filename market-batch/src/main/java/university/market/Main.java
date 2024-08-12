package university.market;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}