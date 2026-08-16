package library_application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import library_application.common.InputReader;
import library_application.common.OutputWriter;
import library_application.common.file.FileIO;
import library_application.domain.Book;
import library_application.domain.BookGenre;
import library_application.domain.User;
import library_application.session.Session;

public class DefaultLibrary implements Library {
	
	private static final String USER_NAME_INPUT_PROMPT = "회원명: ";
	private static final String PHONE_INPUT_PROMPT = "연락처: ";
	private static final String INVALID_USER_INFO = "이미 존재하는 회원입니다.";
	private static final String VALID_USER_INFO = "회원가입 완료";
	
	private static final String INVALID_LOGIN_INFO = "존재하지 않는 회원입니다.";
	private static final String VALID_LOGIN_INFO = "로그인 성공";
	
	private static final String INVALID_BOOK_SEED = "존재하지 않는 책입니다.";
	
	private static final String BOOK_TITLE_INPUT_PROMPT = "책 제목을 입력하세요: ";
	private static final String BOOK_SUB_TITLE_INPUT_PROMPT = "책 부제를 입력하세요: ";
	private static final String BOOK_GENRE_INPUT_PROMPT = "책 장르 번호를 입력하세요: ";
	private static final String BOOK_WRITER_INPUT_PROMPT = "작가를 입력하세요: ";
	private static final String BOOK_PUBLISHER_INPUT_PROMPT = "출판사를 입력하세요: ";
	private static final String BOOK_PUB_DATE_YEAR_INPUT_PROMPT = "출판년도를 입력하세요: ";
	private static final String BOOK_PUB_DATE_MONTH_INPUT_PROMPT = "출판월을 입력하세요: ";
	private static final String BOOK_PUB_DATE_DAY_INPUT_PROMPT = "출판일을 입력하세요: ";
	private static final String BOOK_PRINTING_INPUT_PROMPT = "인쇄 회차를 입력하세요: ";
	private static final String BOOK_PRICE_INPUT_PROMPT = "가격을 입력하세요: ";
	private static final String BOOK_ISBN_INPUT_PROMPT = "책 고유번호를 입력하세요: ";
	private static final String BOOK_ADD_PROMPT = "책 등록이 완료되었습니다.";
	
	private final List<Book> books;
	private final List<User> users;
	
	private final InputReader reader;
	private final OutputWriter writer;
	
	public DefaultLibrary(InputReader reader, OutputWriter writer) {
		this.books = FileIO.loadBook();
		this.users = FileIO.loadUser();
		this.reader = reader;
		this.writer = writer;
	}
	
	@Override
	public boolean addUser() {
		String userName = this.reader.readString(USER_NAME_INPUT_PROMPT);
		String phone = this.reader.readString(PHONE_INPUT_PROMPT);
		
		if (this.users.contains(new User(userName, phone))) {
			this.reader.pause(INVALID_USER_INFO);
			return false;
		} else {
			this.users.add(new User(userName, phone));
			this.reader.pause(VALID_USER_INFO);
			return true;
		}
	}

	@Override
	public User loginUser() {
		String userName = this.reader.readString(USER_NAME_INPUT_PROMPT);
		String phone = this.reader.readString(PHONE_INPUT_PROMPT);
		
		Optional<User> user = this.users.stream()
									     .filter(u -> u.getUserName().equals(userName))
									     .filter(u -> u.getPhone().equals(phone))
									     .findFirst()
									     ;
		if (user.isEmpty()) {
			this.reader.pause(INVALID_LOGIN_INFO);
			return null;
		} 
		
		this.reader.pause(VALID_LOGIN_INFO);
		return user.get();
	}

	@Override
	public boolean loginLibrary() {
		return true;
		// 추후에 비밀번호나 그런 개념이 생기면 검사 가능 
	}

	@Override
	public void saveData() {
		FileIO.saveData(books, users);
	}

	@Override
	public Book getBook(UUID seed) {
		return this.books.stream()
						  .filter(b -> b.getSeed().equals(seed))
						  .findFirst()
						  .orElseThrow(() -> new IllegalArgumentException(INVALID_BOOK_SEED))
						  ;
	}

	@Override
	public void addBook() {
		String title = this.reader.readString(BOOK_TITLE_INPUT_PROMPT);
		String subTitle = this.reader.readString(BOOK_SUB_TITLE_INPUT_PROMPT);
		BookGenre genre = this.getGenre();
		String publisher = this.reader.readString(BOOK_PUBLISHER_INPUT_PROMPT);
		String writer = this.reader.readString(BOOK_WRITER_INPUT_PROMPT);
		LocalDate pubDate = this.getDate();
		int printing = this.reader.readInt(BOOK_PRINTING_INPUT_PROMPT,1,Integer.MAX_VALUE);
		int price = this.reader.readInt(BOOK_PRICE_INPUT_PROMPT, 0, Integer.MAX_VALUE);
		String isbn = this.reader.readString(BOOK_ISBN_INPUT_PROMPT);
		
		this.books.add(new Book(title, subTitle, genre, publisher, writer
								, pubDate, printing, price, isbn));
		
		this.reader.pause(BOOK_ADD_PROMPT);
	}

	/**
	 * 사용자가 입력한 문자열을 BookGenre 로 바꾸는 메소드
	 * @return BookGenre
	 */
	private BookGenre getGenre() {
		
		BookGenre[] genres = BookGenre.values();
		
		for (int i = 0; i < genres.length; i++) {
			this.writer.println("%d. %s".formatted( (i+1), genres[i].toString() ));
		}
		
		int answer = this.reader.readInt(BOOK_GENRE_INPUT_PROMPT, 1, genres.length);
		return genres[answer-1];
	}
	
	/**
	 * 사용자가 입력한 문자열을 LocalDate 로 바꾸는 메소드
	 * @return LocalDate
	 */
	private LocalDate getDate() {
		int year = this.reader.readInt(BOOK_PUB_DATE_YEAR_INPUT_PROMPT, 1, LocalDate.now().getYear());
		int month = this.reader.readInt(BOOK_PUB_DATE_MONTH_INPUT_PROMPT,1,12);
		int day = this.reader.readInt(BOOK_PUB_DATE_DAY_INPUT_PROMPT,1,31);
		return LocalDate.of(year, month, day);
	}
	
	@Override
	public void hasBookUserPrint() {
		/*
		 * 각각의 멤버가 가진 UUID 리스트를 책으로 바꾼 후
		 * 출판일이 10년 이상인 책을 제외하고
		 * 대여일로 바꾸고 나서
		 * 그 날짜가 하나라도 현재일의 5일전보다 이전 날짜라면 출력 
		 */
		this.users.stream()
				  .filter(u -> u.getRentalBooks().stream()
						  						 .map(seed -> this.getBook(seed))
						  						 .filter(book -> book.getPubDate().isBefore(LocalDate.now().plusYears(10)))
						  						 .map(book -> book.getRentalDate())
						  					     .anyMatch(rentalDate -> rentalDate.isBefore(LocalDate.now().minusDays(5)))
						  )
				  .forEach(User::printInfo)
				  ;
	}

	// 대여 횟수가 가장 많은 1개만 출력
	// Book 에 equals 메소드 재정의로 isbn 이 같으면 중복 처리
	@Override
	public void popularBookPrint() {
		this.books.stream()
				  .distinct()
				  .sorted((a,b) -> {
					  int rentalA = this.totalRentCount(a.getIsbn());
					  int rentalB = this.totalRentCount(b.getIsbn());
					  return Integer.compare(rentalB, rentalA);
				  })
				  .limit(1)
				  .forEach(Book::printInfo)
				  ;
				
	}
	
	/**
	 * 동일한 isbn 을 가진 책의 총 대여 횟수를 계산하는 메소드
	 * @param isbn 찾을 isbn
	 * @return 총 대여 횟수
	 */ 
	private int totalRentCount(String isbn) {
		return this.books.stream()
						  .filter(book -> book.getIsbn().equals(isbn))
						  .mapToInt(Book::getCount)
						  .sum()
						  ;
	}
	
	@Override
	public void badUserPrint() {
		this.users.stream()
				  .filter(user -> user.getMiss() >= 3)
				  .forEach(User::printInfo)
				  ;
	}

	@Override
	public void searchBook() {
		this.writer.println("1. 출판사 검색");
		this.writer.println("2. 저자 검색");
		this.writer.println("3. 장르 검색");
		
		int answer = this.reader.readInt("검색 선택: ", 1, 3);
		
		if (answer == 1) {
			String publisher = this.reader.readString("검색할 출판사명: ");
			this.printBook(this.getBookByIsbnGroup((b) -> b.getPublisher().equals(publisher)));
		} else if (answer == 2){
			String writer = this.reader.readString("검색할 저자명: ");
			this.printBook(this.getBookByIsbnGroup((b) -> b.getWriter().equals(writer)));
		} else if (answer == 3) {
			BookGenre genre = this.getGenre();
			this.printBook(this.getBookByIsbnGroup((b) -> b.getGenre() == genre));
		}
	}

	/**
	 * 도서를 isbn 으로 그룹해 반환하는 메소드
	 * @param filter 검색 조건
	 * @return 도서 목록
	 */
	private Map<String, List<Book>> getBookByIsbnGroup(Predicate<Book> filter){
		return this.books.stream()
						  .filter(book -> book.getPubDate().isBefore(LocalDate.now().plusYears(10)))
						  .filter(book -> filter.test(book))
						  .collect(Collectors.groupingBy(Book::getIsbn))
						  ;
	}

	/**
	 * 도서 목록을 출력하는 메소드
	 * @param books 도서 목록
	 */
	private void printBook(Map<String, List<Book>> books) {
		books.forEach((isbn, bookList) -> {
			this.writer.println("\n======================");
			this.writer.println("책 고유번호: " + isbn);
			Book book = bookList.get(0);
			this.writer.println("책 제목: " + book.getTitle());
			this.writer.println("책 부제: " + book.getSubTitle());
			this.writer.println("책 장르: " + book.getGenre().toString());
			this.writer.println("책 출판사: " + book.getPublisher());
			this.writer.println("책 작가: " + book.getWriter());
			this.writer.println("출판일: " + book.getPubDate());
			this.writer.println("인쇄회차: " + book.getPrinting());
			this.writer.println("책 가격: " + book.getPrice());
			
			int totalRentalCount = bookList.stream()
										   .mapToInt(Book::getCount)
										   .sum();
			
			long rentalBookCount = bookList.stream()
										  .filter(Book::isRental)
										  .count();
			
			this.writer.println("대여횟수: " + totalRentalCount);
			this.writer.println("보유한 총 책 개수: " + bookList.size());
			this.writer.println("현재 대여된 책 개수: " + rentalBookCount);
		});
	}
	
	@Override
	public void rentalBook() {
		String isbn = this.reader.readString("대여할 책의 고유번호: ");
		
		List<Book> findBook = this.books.stream()
									    .filter(book -> book.getIsbn().equals(isbn))
									    .filter(book -> !book.isRental())
									    .toList();
		
		for (int i = 0; i < findBook.size(); i++) {
			this.writer.println("%d. 입고일: %s".formatted( (i+1), findBook.get(i).getStockDate().toString() ));
		}
		
		int answer = this.reader.readInt("대여할 번호: ",1,findBook.size());
		
		Book rentalBook = findBook.get(answer-1);
		
		Session.user.rentalBook(rentalBook);
	}

	@Override
	public void returnBook() {
		List<Book> rentalBooks = Session.user.getRentalBooks().stream()
															  .map(seed -> this.getBook(seed))
															  .toList();
		
		for (int i = 0; i < rentalBooks.size(); i++) {
			this.writer.println("%d. 책제목: %s, 대여일: %s".formatted( (i+1)
					, rentalBooks.get(i).getTitle(), rentalBooks.get(i).getRentalDate().toString() ));
		}
		
		int answer = this.reader.readInt("반납할 책 번호: ", 1, rentalBooks.size());
		
		Session.user.returnBook(answer-1);
	}
	
}
