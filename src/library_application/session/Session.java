package library_application.session;

import library_application.common.ConsoleInputReader;
import library_application.common.ConsoleOutputWriter;
import library_application.domain.User;
import library_application.service.DefaultLibrary;
import library_application.service.Library;

/**
 * 로그인 처리
 */
public final class Session
{
	private Session() {
	}
	
	public static SessionStatus status;
	public static final Library LIBRARY;
	public static User user;
	
	static {
		status = SessionStatus.DEFAULT;
		LIBRARY = new DefaultLibrary(new ConsoleInputReader(), new ConsoleOutputWriter());
		user = null;
	}
	
	public static boolean isDefault() {
		return status == SessionStatus.DEFAULT;
	}
	
	public static boolean isUser() {
		return status == SessionStatus.USER;
	}
	
	public static boolean isLibrary() {
		return status == SessionStatus.LIBRARY;
	}
	
	public static boolean isExit() {
		return status == SessionStatus.EXIT;
	}
	
	public static void addUser() {
		LIBRARY.addUser();
	}
	
	public static void logout() {
		status = SessionStatus.DEFAULT;
	}
	
	public static void loginUser() {
		User u = LIBRARY.loginUser();
		
		if (u != null) {
			user = u;
			status = SessionStatus.USER;
		}
	}
	
	public static void loginLibrary() {
		if (LIBRARY.loginLibrary()) {
			status = SessionStatus.LIBRARY;
		}
	}
	
	public static void save() {
		LIBRARY.saveData();
		status = SessionStatus.EXIT;
	}
}
