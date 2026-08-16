package library_application.main.menu;

import library_application.common.ConsoleInputReader;
import library_application.common.ConsoleOutputWriter;
import library_application.common.InputReader;
import library_application.common.OutputWriter;

public final class MenuRender {
	
	private static final InputReader READER;
	private static final OutputWriter WRITER;
	
	static {
		READER = new ConsoleInputReader();
		WRITER = new ConsoleOutputWriter();
	}
	
	private MenuRender() {
	}
	
	public static final void render(String title, Menu[] menus) {
		WRITER.println(title);
		
		for (int i = 0; i < menus.length; i++) {
			WRITER.println("%d. %s".formatted( (i+1), menus[i].getMenu() ));
		}
		
		int answer = READER.readInt("메뉴 선택: ", 1, menus.length + 1);
		
		menus[answer-1].order();
	}
}
