package university.market.start.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectStartController {

    @GetMapping("/")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("hello world!");
    }
}
