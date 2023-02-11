package com.email.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("email")
@AllArgsConstructor
public class EmailController {



    /**
     * Invia email
     */
    @RequestMapping(value = "/send/{email}/{text}", method = RequestMethod.POST)
    public ResponseEntity sendEmail(@PathVariable("email") String email, @PathVariable("text") String text) {
        try {
           // emailSederService.sendEmail(email, "FINANCE CONSULTING APP", text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("SUCCESS!");
    }

}
