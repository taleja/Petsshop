package com.petsshop.service;

public class CSVParserTest {
	
	public static void main(String...strings) {
		String csv = "hello,kitty,6,nice,kity,77,purr,purr,90";
		 
		StringBuilder sb = new StringBuilder();
		 for(int i = 0; i < csv.length(); i++) {
			 if(csv.charAt(i)==',') {
				 System.out.println(sb.toString());
				 sb.setLength(0);
				 continue;
			 } else {
				 sb.append(csv.charAt(i));
			 }
		 }

	}

}
