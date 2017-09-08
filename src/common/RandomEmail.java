package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomEmail {

	public static String email() {
		SimpleDateFormat sd = new SimpleDateFormat("ddMMMyy_HHmmss");

		String a = "test" + sd.format(new Date()) + "@frescano.se";
		System.out.println(a);
		return a;

	}

	public static void main(String[] args) {
		for (int i = 0; i <= 5; i++) {
			email();
		}
	}

}