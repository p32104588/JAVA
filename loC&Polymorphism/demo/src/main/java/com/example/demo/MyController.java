package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/API", method = RequestMethod.GET)
public class MyController {

	@Autowired
	@Qualifier("hpPrinter")
	private Printer printer;

	@GetMapping(value = "/test")
	public String test() {
//		System.out.println("Hi!");
		printer.print("Hellow World");
		return "Hellow World";
	}
}
