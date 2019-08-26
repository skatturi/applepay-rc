package ch.datatrans.applepay;

import ch.datatrans.applepay.client.ApplePayClient;
import ch.datatrans.applepay.client.DatatransClient;
import ch.datatrans.applepay.request.AuthorizeRequest;
import ch.datatrans.applepay.request.StartSessionRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik.mengelt@gmail.com
 */
@Controller
@RequestMapping("/api")
//@CrossOrigin(origins = "https://www.datatrans.ch") // remove this or adjust it to your needs
public class ApplePayController {

    private static final Logger logger = LoggerFactory.getLogger(ApplePayController.class);

    private DatatransClient datatransClient;
    private ApplePayClient applePayClient;

    @Autowired
    public ApplePayController(DatatransClient datatransClient, ApplePayClient applePayClient) {
        this.datatransClient = datatransClient;
        this.applePayClient = applePayClient;
    }

    @PostMapping("/session/create")
    @ResponseBody
    public ResponseEntity<String> createSession(@RequestBody StartSessionRequest startSessionRequest, HttpServletRequest request) {
        logger.info("/api/session/create called with validationUrl={}", startSessionRequest.getValidationUrl());

        String originHeader = request.getHeader("origin");

        if(StringUtils.isBlank(originHeader)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String response = applePayClient.createSession(startSessionRequest.getValidationUrl(), originHeader);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authorize")
    @ResponseBody
    public String authorize(@RequestBody AuthorizeRequest authorizeRequest) {
        logger.info("/api/authorize called");
        return datatransClient.authorize(authorizeRequest.getPaymentData().toString());
    }

    @PostMapping("/order/save")
    @ResponseBody
    public ResponseEntity<Void> saveOrder(@RequestBody String saveOrderRequest) {
        logger.info("/api/order/save called");
        logger.info(saveOrderRequest);
        // handle the Datatrans POST URL call here and save the needed data to your DB
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    @ResponseBody
    public String testmethod()
    {

        return "hello";
    }

}
