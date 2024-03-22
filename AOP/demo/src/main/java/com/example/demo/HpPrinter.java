package com.example.demo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class HpPrinter implements Printer {

	@Override
	public void print(String message) {
//		Date start = new Date();
		
		System.out.println("HP印表機: " + message);
		
//		Date end = new Date();
//		long time = end.getTime()-start.getTime();
//		System.out.println("總共執行了 " + time + " ms");
	}
}
