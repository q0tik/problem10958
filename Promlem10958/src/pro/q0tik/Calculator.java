package pro.q0tik;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.math.BigInteger;
import java.math.BigInteger;
import java.util.LinkedList;

public class Calculator {
	
//	File numbers = new File("src/external/numbers.txt");
//	File order = new File("src/external/order.txt");
//	File answers = new File("src/external/answers.txt");
	
	File numbers = new File(System.getProperty("user.dir") + "/numbers.txt");
	File order = new File(System.getProperty("user.dir") + "/order.txt");
	File answers = new File(System.getProperty("user.dir") + "/answers.txt");
	
	BigInteger answ = new BigInteger("10958");
	
	public Calculator() throws IOException {
		//fillList();
		//fillOrder();
		answers.createNewFile();
		
		brN = new BufferedReader(new FileReader(numbers));
		bw = new BufferedWriter(new FileWriter(answers, true));
		
		String exp;
		while ((exp = brN.readLine()) != null) {
			brO = new BufferedReader(new FileReader(order));
			String ord;
			while ((ord = brO.readLine()) != null) {
				if (calculate(exp, ord).equals(answ)) {
					bw.append(exp + "\n" + ord + "\n\n");
				}
			}
		}
		bw.close();
		
//		if (calculate("1*2\u00003+4*5*6\u00007+8*9", "67812345").equals(answ)) {
//			bw.append("lolkek\n");
//		}
		brN.close();
		brO.close();
	}
	
	//TODO: store in file instead of RAM
	//public static LinkedList<String> listOfExpressions = new LinkedList<String>();
	
	BufferedWriter bw;
	BufferedReader brN;
	BufferedReader brO;
	
	public BigInteger calculate(String expression, String order) {
		String answ = expression;
		String[] opsA = answ.split("[0-9]");
		String[] numA = answ.split("[^0-9]");
		
		LinkedList<BigInteger> num = new LinkedList<BigInteger>();
		LinkedList<String> ops = new LinkedList<String>();
		LinkedList<Integer> iOrder = new LinkedList<Integer>();

		for (int i = 1; i < opsA.length; i++) ops.add(opsA[i]);
		for (String s : numA) num.add(new BigInteger(s));
		
		for (int i = 0; i < 8; i++) {
			iOrder.add(Character.getNumericValue(order.charAt(i)));
		}
		
//		System.out.println(num.toString());
//		System.out.println(ops.toString());
//		System.out.println(iOrder.toString());
		
		for (int i = 0; i < 8; i++) {
			int o = 0;
			
			for (int j = 0; j < iOrder.size(); j++) {
				if (iOrder.get(j) == (i + 1)) {
					o = j;
					break;
				}
			}
			
			String opp = ops.get(o);
			
			if (opp.equals("^")) {
				if (num.get(o).compareTo(new BigInteger("999")) > 0
					&& num.get(o + 1).compareTo(new BigInteger("999")) > 0) {
						num.set(o, pow(num.get(o), num.get(o + 1)));
				} else {
					return BigInteger.ZERO;
				}
			}
			
			if (opp.equals("+")) {
				num.set(o, num.get(o).add(num.get(o + 1)));
			}
			
			if (opp.equals("-")) {
				num.set(o, num.get(o).subtract(num.get(o + 1)));
			}

			if (opp.equals("*")) {
				num.set(o, num.get(o).multiply(num.get(o + 1)));
			}
			
			if (opp.equals("/")) {
				try {
					num.set(o, num.get(o).divide(num.get(o + 1)));
				} catch (Exception e) {
					return BigInteger.ZERO;
				}
			}
			
			if (opp.equals("\u0000")) {
				num.set(o, concat(num.get(o), num.get(o + 1)));
			}
			
			num.remove(o + 1);
			ops.remove(o);
			iOrder.remove(o);
					
//			System.out.println();
//			System.out.println(num.toString());
//			System.out.println(ops.toString());
//			System.out.println(iOrder.toString());
			
		}
		
		System.out.println(num.get(0));
		return num.get(0);
	}
	
	private BigInteger concat(BigInteger first, BigInteger second) {
		StringBuilder sFirst = new StringBuilder(first.toString());
		StringBuilder sSecond = new StringBuilder(second.toString());
		
		if (sFirst.charAt(0) == '-' && sSecond.charAt(0) == '-') {
			sFirst.deleteCharAt(0);
			sSecond.deleteCharAt(0);
			
			String s1 = sFirst.toString();
			String s2 = sSecond.toString();
			return new BigInteger(s1 + s2);
		} else if (sFirst.charAt(0) != '-' && sSecond.charAt(0) != '-') {
			String s1 = sFirst.toString();
			String s2 = sSecond.toString();
			
			return new BigInteger(s1 + s2);
		} else {
			String s1 = removeChar(sFirst.toString(), '-');
			String s2 = removeChar(sSecond.toString(), '-');
			
			return new BigInteger("-" + s1 + s2);
		}
	}
	
	private BigInteger pow(BigInteger base, BigInteger exponent) {
		BigInteger result = BigInteger.ONE;
			
		while (exponent.signum() > 0) {
			if (exponent.testBit(0)) result = result.multiply(base);
		    	base = base.multiply(base);
		    	exponent = exponent.shiftRight(1);
		}
		
		return result;
	}
	
	private void fillOrder() throws IOException {
		order.createNewFile();
		bw = new BufferedWriter(new FileWriter(order, true));
		
		for (int i = 12345678; i <= 87654321; i++) {
			String s = i + "\n";
			if (!searchCoincidences(s) && !search9n0(s)) {
				bw.append(s);
			}
		}
		
		bw.close();
	}
	
	private void fillList() throws IOException {
		numbers.createNewFile();
		bw = new BufferedWriter(new FileWriter(numbers, true));
		
		String operands = "\u0000+-*/^";
		String bare = "123456789";
		String counter = "00000000";
		
		while (true) {
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < 9; j++) {
				sb.append(bare.charAt(j));
				
				if (j < 8) {
					sb.append(operands.charAt(Character.getNumericValue(counter.charAt(j))));
				}
			}
			
			//String builded = sb.toString();
			//builded = removeChar(builded, '\u0000');
			
			bw.append(sb.toString() + '\n');

			if (counter.equals("55555555")) {
				break;
			} else {
				counter = fnsAppend(counter);
			}

		}
		
		bw.close();
	}
	
	private boolean searchCoincidences(String s) {
		for (int i = 0; i < s.length() - 1; i++) {
			char a = s.charAt(i);
			for (int j = i + 1; j < s.length(); j++) {
				if (a == s.charAt(j)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean search9n0(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '9' || s.charAt(i) == '0') {
				return true;
			}
		}
		
		return false;
	}
	
	public static String fnsAppend(String f) {
		boolean apended = true;
		String result = "";
		
		for (int i = f.length() - 1; i >= 0; i--) {
			if (apended) {
				if (f.charAt(i) == '5') {
					result += '0';
				} else {
					apended = false;
					
					if (f.charAt(i) == '0') { result += '1'; }
					if (f.charAt(i) == '1') { result += '2'; }
					if (f.charAt(i) == '2') { result += '3'; }
					if (f.charAt(i) == '3') { result += '4'; }
					if (f.charAt(i) == '4') { result += '5'; }
				}
			} else {
				result += f.charAt(i);
			}
		}
		
		return (new StringBuilder(result)).reverse().toString();
	}
	
	public static String removeChar(String s, char c) {
	       String r = "";
	       for (int i = 0; i < s.length(); i ++) {
	          if (s.charAt(i) != c) r += s.charAt(i);
	       }
	       return r;
	}
}
