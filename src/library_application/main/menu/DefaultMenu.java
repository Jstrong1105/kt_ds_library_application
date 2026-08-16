package library_application.main.menu;

import library_application.session.Session;

public enum DefaultMenu implements Menu {
	
	로그인("로그인", Session::loginUser)
	, 회원가입("회원가입", Session::addUser)
	, 도서관관리("도서관 관리", Session::loginLibrary)
	, 프로그램종료("프로그램 종료", Session::save)
	;
	
	private final String menu;
	private final Runnable service;
	
	DefaultMenu(String menu, Runnable service){
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
