package library_application.common;

/**
 * 출력 기능
 */
public interface OutputWriter
{
	/**
	 * 메시지 출력 기능
	 * 개행 X
	 * @param prompt 출력할 메시지
	 * @throws IllegalArgumentException prompt 가 null 인 경우 발생
	 */
	void print(String prompt);
	
	/**
	 * 메시지 출력 기능
	 * 개행 O
	 * @param prompt 출력할 메시지
	 * @throws IllegalArgumentException prompt 가 null 인 경우 발생
	 */
	void println(String prompt);
}
