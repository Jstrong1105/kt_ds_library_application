package library_application.service;

import java.util.UUID;

import library_application.domain.Book;
import library_application.domain.User;

/**
 * 도서관 인터페이스
 */
public interface Library
{
	/**
	 * 유저 등록 메소드
	 * @return 등록 성공 여부
	 */
	boolean addUser();
	
	/**
	 * 회원 로그인 메소드
	 * @return 로그인한 유저 / 로그인 실패 시 null 반환
	 */
	User loginUser();
	
	/**
	 * 도서관 로그인 메소드
	 * @return 성공 여부 
	 */
	boolean loginLibrary();
	
	/**
	 * 데이터 저장 메소드
	 */
	void saveData();
	
	/**
	 * seed 를 책으로 바꾸는 메소드
	 * @param seed 찾을 책의 관리 번호
	 * @return 책
	 * @throws IllegalArgumentException seed 가 없을 경우 발생
	 */
	Book getBook(UUID seed);
	
	/**
	 * 책 입고 메소드
	 */
	void addBook();
	
	/**
	 * 대여 일자가 5일 이상인 회원 조회 메소드
	 */
	void hasBookUserPrint();
	
	/**
	 * 대여 횟수가 가장 많은 도서 조회 메소드
	 */
	void popularBookPrint();
	
	/**
	 * 대여 금지 회원 조회 메소드
	 */
	void badUserPrint();
	
	/**
	 * 도서 검색 메소드
	 */
	void searchBook();
	
	/**
	 * 도서 대여 메소드
	 */
	void rentalBook();
	
	/**
	 * 도서 반납 메소드
	 */
	void returnBook();
}
