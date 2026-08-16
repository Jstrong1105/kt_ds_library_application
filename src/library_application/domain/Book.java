package library_application.domain;

import java.time.LocalDate;
import java.util.UUID;

import library_application.common.file.FileIO;

/**
 * 도서 한권
 */
public class Book {
	
	/** 도서명 */
	private final String title;
	
	/** 도서부제 */
	private final String subTitle;
	
	/** 도서 장르 */
	private final BookGenre genre;
	
	/** 출판사 */
	private final String publisher;
	
	/** 저자 */
	private final String writer;
	
	/** 출판일 */
	private final LocalDate pubDate;
	
	/** 인쇄 회차 */
	private final int printing;
	
	/** 입고일 */
	private final LocalDate stockDate;
	
	/** 가격 */
	private final int price;
	
	/** 책 고유번호 */
	private final String isbn;
	
	/** 책 관리번호 */
	private final UUID seed;
	
	/** 대여횟수 */
	private int count;
	
	/** 대여 상태 */
	private boolean rental;
	
	/** 대여일 */
	private LocalDate rentalDate;
	
	/** 반납 상태 */
	private boolean hasReturn;
	
	/** 반납일 */
	private LocalDate returnDate;
	
	/** 대여한 회원 */
	private String userName;
	
	// 저장된 책 불러올 때 사용하는 생성자
	public Book(String title, String subTitle, BookGenre genre, String publisher, String writer
				, LocalDate pubDate, int printing, LocalDate stockDate, int price, String isbn, UUID seed
				, int count, boolean rental, LocalDate rentalDate, boolean hasReturn, LocalDate returnDate, String userName) {
		this.title = title;
		this.subTitle = subTitle;
		this.genre = genre;
		this.publisher = publisher;
		this.writer = writer;
		this.pubDate = pubDate;
		this.printing = printing;
		this.stockDate = stockDate;
		this.price = price;
		this.isbn = isbn;
		this.seed = seed;
		this.count = count;
		this.rental = rental;
		this.rentalDate = rentalDate;
		this.hasReturn = hasReturn;
		this.returnDate = returnDate;
		this.userName = userName;
	}

	// 최초 책 생성 시 사용하는 생성자
	public Book(String title, String subTitle, BookGenre genre, String publisher, String writer
			, LocalDate pubDate, int printing, int price, String isbn) {
		this(title,subTitle,genre,publisher,writer,pubDate,printing,LocalDate.now(),price,isbn,UUID.randomUUID(),0,false,null,false,null,null);
	}
	
	// 대여하기
	public void rental(String rentalUser) {
		if (this.rental) {
			throw new IllegalStateException("이미 대여한 도서입니다.");
		} 
		
		this.rental = true;
		this.rentalDate = LocalDate.now();
		this.count++;
		
		this.hasReturn = false;
		this.returnDate = null;
		this.userName = rentalUser;
	}
	
	// 반납하기
	public void hasReturn(String rentalUser) {
		
		if (!this.rental) {
			throw new IllegalStateException("대여 중인 도서가 아닙니다.");
		}
		
		if (!this.userName.equals(rentalUser)) {
			throw new IllegalArgumentException("대여한 사람이 아닙니다.");
		}
		
		this.hasReturn = true;
		this.returnDate = LocalDate.now();
		
		this.rental = false;
		this.rentalDate = null;
	}
	
	public String getTitle() {
		return this.title;
	}

	public String getSubTitle() {
		return this.subTitle;
	}

	public BookGenre getGenre() {
		return this.genre;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public String getWriter() {
		return this.writer;
	}

	public LocalDate getPubDate() {
		return this.pubDate;
	}

	public int getPrinting() {
		return this.printing;
	}

	public LocalDate getStockDate() {
		return this.stockDate;
	}

	public int getPrice() {
		return this.price;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public UUID getSeed() {
		return this.seed;
	}

	public int getCount() {
		return this.count;
	}

	public boolean isRental() {
		return this.rental;
	}

	public LocalDate getRentalDate() {
		return this.rentalDate;
	}

	public boolean isHasReturn() {
		return this.hasReturn;
	}

	public LocalDate getReturnDate() {
		return this.returnDate;
	}

	public String getRentalUser() {
		return this.userName;
	}

	public String toSaveString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.title).append(FileIO.SEPARATOR);
		buffer.append(this.subTitle).append(FileIO.SEPARATOR);
		buffer.append(this.genre).append(FileIO.SEPARATOR);
		buffer.append(this.publisher).append(FileIO.SEPARATOR);
		buffer.append(this.writer).append(FileIO.SEPARATOR);
		buffer.append(this.pubDate).append(FileIO.SEPARATOR);
		buffer.append(this.printing).append(FileIO.SEPARATOR);
		buffer.append(this.stockDate).append(FileIO.SEPARATOR);
		buffer.append(this.price).append(FileIO.SEPARATOR);
		buffer.append(this.isbn).append(FileIO.SEPARATOR);
		buffer.append(this.seed).append(FileIO.SEPARATOR);
		buffer.append(this.count).append(FileIO.SEPARATOR);
		buffer.append(this.rental).append(FileIO.SEPARATOR);
		buffer.append(this.rentalDate).append(FileIO.SEPARATOR);
		buffer.append(this.hasReturn).append(FileIO.SEPARATOR);
		buffer.append(this.returnDate).append(FileIO.SEPARATOR);
		buffer.append(this.userName);
		
		return buffer.toString();
	}
	
	public void printInfo() {
		System.out.println("title: " + this.title);
		System.out.println("subTitle: " + this.subTitle);
		System.out.println("genre: " + this.genre);
		System.out.println("publisher: " + this.publisher);
		System.out.println("writer: " + this.writer);
		System.out.println("pubDate: " + this.pubDate);
		System.out.println("printing: " + this.printing);
		System.out.println("stockDate: " + this.stockDate);
		System.out.println("price: " + this.price);
		System.out.println("isbn: " + this.isbn);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Book b = (Book) obj;
		return this.isbn.equals(b.isbn);
	}
	
	@Override
	public int hashCode() {
		return isbn.hashCode();
	}
}