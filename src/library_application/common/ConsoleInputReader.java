package library_application.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * InputReader 인터페이스를 구현한 클래스
 * 
 * 입력을 처리
 */
public class ConsoleInputReader implements InputReader {

	private static final String NUMBER_FORMAT_PROMPT = "숫자만 입력하세요.";
	private static final String NUMBER_RANGE_FORMAT = "%d ~ %d 사이로 입력하세요.";
	
	private final BufferedReader reader;
	private final OutputWriter writer;
	
	// 실 사용 생성자
	public ConsoleInputReader() {
		this(new BufferedReader(new InputStreamReader(System.in)), new ConsoleOutputWriter());
	}
	
	// 테스트 객체 주입 용 생성자
	public ConsoleInputReader(BufferedReader reader, OutputWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	private String readLine() {
		try {
			return this.reader.readLine();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public void pause(String prompt) {
		this.writer.print(prompt);
		this.readLine();
	}

	@Override
	public String readString(String prompt) {
		this.writer.print(prompt);
		return this.readLine();
	}

	@Override
	public int readInt(String prompt) {
		while(true) {
			try {
				return Integer.parseInt(this.readString(prompt));
			} catch (NumberFormatException e) {
				this.writer.println(NUMBER_FORMAT_PROMPT);
			}
		}
	}

	@Override
	public int readInt(String prompt, int min, int max) {
		while(true) {
			int number = this.readInt(prompt);
			
			if (number < min || number > max) {
				this.writer.println(NUMBER_RANGE_FORMAT.formatted(min,max));
			} else {
				return number;
			}
		}
	}
}
