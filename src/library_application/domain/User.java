package library_application.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import library_application.common.file.FileIO;
import library_application.session.Session;

/**
 * 회원 1명
 */
public class User {
	
	/** 회원명 */
	private final String userName;
	
	/** 연락처 */
	private final String phone;
	
	/** 벌금 */
	private int fine;
	
	/** 반납기간 초과 횟수 */
	private int miss;
	
	/** 대여한 도서의 목록 */
	private final List<UUID> rentalBooks;
	
	public String toSaveString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(this.userName).append(FileIO.SEPARATOR);
		buffer.append(this.phone).append(FileIO.SEPARATOR);
		buffer.append(this.fine).append(FileIO.SEPARATOR);
		buffer.append(this.miss);
		
		for (UUID seed : this.rentalBooks) {
			buffer.append(FileIO.SEPARATOR).append(seed.toString());
		}
		
		return buffer.toString();
	}
	
	// 최초 등록 시 사용하는 생성자
	public User(String userName, String phone) {
		this(userName,phone,0,0,new ArrayList<>());
	}
	
	// 저장된 파일을 생성할 떄 사용하는 생성자 
	public User(String userName, String phone, int fine, int miss, List<UUID> rentalBooks) {
		this.userName = userName;
		this.phone = phone;
		this.fine = fine;
		this.miss = miss;
		this.rentalBooks = rentalBooks;
	}

	// 대여하기
	public void rentalBook(Book book) {
		
		if (this.miss >= 3) {
			throw new IllegalStateException("반납 기간 미준수 3회로 대여 불가");
		}
		
		book.rental(this.userName);
		this.rentalBooks.add(book.getSeed());
	}
	
	// 반납하기
	public void returnBook(int index) {
		if (index < 0 || index >= this.rentalBooks.size()) {
			throw new IllegalArgumentException("대여하지 않은 책입니다.");
		}
		
		Book book = Session.LIBRARY.getBook(this.rentalBooks.get(index));
		
		long years = ChronoUnit.YEARS.between(book.getPubDate(), LocalDate.now());
		long days = ChronoUnit.DAYS.between(book.getRentalDate(), LocalDate.now());
		
		book.hasReturn(this.userName);
		this.rentalBooks.remove(index);
		
		if (years >= 10) {
			System.out.println("폐기 처리된 책입니다.");
			return;
		}
		
		if (days > 7) {
			System.out.println("반납 기간 초과");
			this.miss++;
			this.fine += (days - 7) * 500;
		}
	}
	
	public String getUserName() {
		return this.userName;
	}

	public String getPhone() {
		return this.phone;
	}

	public int getFine() {
		return this.fine;
	}

	public int getMiss() {
		return this.miss;
	}

	public List<UUID> getRentalBooks() {
		return this.rentalBooks;
	}
	
	public void printInfo() {
		System.out.println("userName: " + this.userName);
		System.out.println("phone: " + this.phone);
		System.out.println("fine: " + this.fine);
		System.out.println("miss: " + this.miss);
		
		for(UUID seed : this.rentalBooks) {
			System.out.println("대여한 책 관리번호: " + seed.toString());
		}
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
		User u = (User) obj;
		
		return this.userName.equals(u.userName) && this.phone.equals(u.phone);
	}
	
	@Override
	public int hashCode() {
		return (this.userName.hashCode() * 31) + this.phone.hashCode();
	}
}