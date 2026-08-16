package library_application.main.menu;

import library_application.session.Session;

public enum LibraryMenu implements Menu {
	
	도서입고("도서 입고", () -> Session.LIBRARY.addBook())
	, 반납대기유저조회("반납 대기 유저 조회", () -> Session.LIBRARY.hasBookUserPrint())
	, 인기도서조회("대여 횟수 1위 도서 조회", () -> Session.LIBRARY.popularBookPrint())
	, 밴유저조회("대여 금지 유저 조회", () -> Session.LIBRARY.badUserPrint())
	, 로그아웃("로그 아웃", () -> Session.logout())
	;

	private final String menu;
	private final Runnable service;
	
	private LibraryMenu(String menu, Runnable service) {
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
