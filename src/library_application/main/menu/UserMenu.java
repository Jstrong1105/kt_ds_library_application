package library_application.main.menu;

import library_application.session.Session;

public enum UserMenu implements Menu {
	
	책조회("책 조회", () -> Session.LIBRARY.searchBook())
	, 책대여("책 대여", () -> Session.LIBRARY.rentalBook())
	, 책반납("책 반납", () -> Session.LIBRARY.returnBook())
	, 로그아웃("로그 아웃", () -> Session.logout())
	;
	
	private final String menu;
	private final Runnable service;
	
	private UserMenu(String menu, Runnable service) {
		this.menu = menu;
		this.service = service;
	}
	
	@Override
	public String getMenu() {
		return this.menu;
	}
	
	@Override
	public void order() {
		this.service.run();
	}
}
