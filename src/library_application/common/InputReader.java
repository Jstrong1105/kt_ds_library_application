package library_application.common;

/**
 * 입력을 받는 인터페이스
 */
public interface InputReader
{
	/***
	 * 사용자가 엔터를 누를때 까지 프로그램을 정지하는 메소드
	 * @param prompt 출력할 메시지
	 */
	void pause(String prompt);
	
	/**
	 * 사용자에게 문자열를 입력받는 메소드
	 * @param prompt 출력할 메시지
	 * @return 입력한 문자열
	 */
	String readString(String prompt);
	
	/**
	 * 사용자에게 숫자를 입력받는 메소드
	 * 숫자를 입력할 때 까지 무한 반복함
	 * @param prompt 출력할 메시지
	 * @return 입력한 숫자
	 */
	int readInt(String prompt);
	
	/**
	 * 사용자에게 숫자를 범위내로 입력받는 메소드
	 * 범위 내의 숫자를 입력할 때 까지 무한 반복함
	 * @param prompt 출력할 메시지
	 * @param min 최소 값 (min 포함)
	 * @param max 최대 값 (max 포함)
	 * @return 입력한 숫자
	 */
	int readInt(String prompt, int min, int max);
}
