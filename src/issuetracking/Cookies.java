package issuetracking;

import javax.servlet.http.Cookie;
//test
public class Cookies {
	public static String getValue(Cookie[] cookies, String name) {
		for (Cookie c1 : cookies) {
			if (c1.getName().equals(name))
				return c1.getValue();
		}
		return null;
	};

	public static Cookie getCookie(Cookie[] cookies, String name) {
		for (Cookie c1 : cookies) {
			if (c1.getName().equals(name))
				return c1;
		}
		return null;
	};
}