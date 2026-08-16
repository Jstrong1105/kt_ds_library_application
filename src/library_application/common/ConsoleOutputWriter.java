package library_application.common;

/**
 * OutputWriter 를 구현한 클래스
 * 
 * 출력을 처리
 */
public class ConsoleOutputWriter implements OutputWriter {

	private static final String NULL_EXCEPTION = "출력 오류";
	
	@Override
	public void print(String prompt) {
		if (prompt == null) {
			throw new IllegalArgumentException(NULL_EXCEPTION);
		}
		System.out.print(prompt);
	}
	
	@Override
	public void println(String prompt) {
		if (prompt == null) {
			throw new IllegalArgumentException(NULL_EXCEPTION);
		}
		System.out.println(prompt);
	}
}
