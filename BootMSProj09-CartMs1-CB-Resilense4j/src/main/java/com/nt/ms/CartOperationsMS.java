package com.nt.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.client.IBillMSInvokerClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/cart")
public class CartOperationsMS {
	
	@Autowired
	private IBillMSInvokerClient client;
	
	@GetMapping("/sell")
	@CircuitBreaker(name = "CartMS",fallbackMethod = "billingFallback")
	public ResponseEntity<String> doShopping()
	
	{
		System.out.println(client.getClass());
		String sourceMsg="shirt,troser,Hat items are purchased";
		
		//invoke provider MS through client comp
		
		String destMsg=client.invokeDoBilling().getBody();
		
		return new ResponseEntity<String>(sourceMsg+"..."+destMsg,HttpStatus.OK);
	}
	
	
	/*public ResponseEntity<String> billingFallback(Exception e)
	{
		return new ResponseEntity<String>("problem in BillingMS invocation",HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
	// ✅ Correct fallback
    public ResponseEntity<String> billingFallback(Throwable ex) {
        return new ResponseEntity<>(
                "problem in BillingMS invocation - Fallback Executed",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
