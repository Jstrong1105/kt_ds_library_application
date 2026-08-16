package library_application.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import library_application.domain.Book;
import library_application.domain.BookGenre;
import library_application.domain.User;

/**
 * 저장 및 불러오기
 */
public final class FileIO {
	
	public static final String SEPARATOR = "@SEPARATOR@"; 
	
	private static final String PATH = "C:/library/data";
	private static final String BOOK_FILE = "book.txt";
	private static final String USER_FILE = "user.txt";
	
	private FileIO() {
	}
	
	public static void saveData(List<Book> books, List<User> users) {
		save(PATH, BOOK_FILE, books.stream().map(Book::toSaveString).toList());
		save(PATH, USER_FILE, users.stream().map(User::toSaveString).toList());
	}
	
	/**
	 * @param directory 파일 경로
	 * @param fileName  파일 이름
	 * @param data      저장할 문자열
	 */
	private static void save(String directory, String fileName, List<String> data) {
		
		File file = new File(directory,fileName);
		
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try {
			Files.write(file.toPath(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Book> loadBook(){
		
		return loadData(PATH, BOOK_FILE, FileIO::parseBook);
	}
	
	private static Book parseBook(String saveBook) {
		
		String[] data = saveBook.split(SEPARATOR);
		
		return new Book(data[0], data[1], BookGenre.getGenre(data[2])
				, data[3], data[4], toLocalDate(data[5]), Integer.parseInt(data[6])
				, toLocalDate(data[7]), Integer.parseInt(data[8]), data[9], UUID.fromString(data[10])
				, Integer.parseInt(data[11]), toBoolean(data[12]), toLocalDate(data[13])
				, toBoolean(data[14]), toLocalDate(data[15]), toStringOrNull(data[16]))
				;
	}
	
	private static String toStringOrNull(String str) {
		if ("null".equals(str)) {
			return null;
		} else {
			return str;
		}
	}
	
	private static LocalDate toLocalDate(String str) {
		if ("null".equals(str)) {
			return null;
		} else {
			return LocalDate.parse(str);
		}
	}
	
	private static boolean toBoolean(String str) {
		if (str.equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static List<User> loadUser(){
		
		return loadData(PATH, USER_FILE, FileIO::parseUser);
	}
	
	private static User parseUser(String saveUser) {
		
		String[] data = saveUser.split(SEPARATOR);
		
		List<UUID> rentalBooks = new ArrayList<>();
		
		for(int i = 4; i < data.length; i++) {
			rentalBooks.add(UUID.fromString(data[i]));
		}
		
		return new User(data[0], data[1], Integer.parseInt(data[2])
					, Integer.parseInt(data[3]), rentalBooks);
	}
	
	public static <T> List<T> loadData(String directory, String file, Function<String, T> toData){
		
		try {
			return Files.lines(new File(directory,file).toPath())
						.map(s -> toData.apply(s))
						.collect(Collectors.toList())
						;
		} catch (IOException e) {
			// e.printStackTrace();
			return new ArrayList<>();
		}
	}
}