package library_application.main;

import library_application.main.menu.DefaultMenu;
import library_application.main.menu.LibraryMenu;
import library_application.main.menu.MenuRender;
import library_application.main.menu.UserMenu;
import library_application.session.Session;

public class Main {
	public static void main(String[] args) {
		while(true) {
			if (Session.isDefault()) {
				MenuRender.render("====== 도서 관리 어플리케이션 ======", DefaultMenu.values());
			} else if (Session.isUser()) {
				MenuRender.render("====== 도서 관리 어플리케이션(회원) ======", UserMenu.values());
			} else if (Session.isLibrary()) {
				MenuRender.render("====== 도서 관리 어플리케이션(도서관) ======", LibraryMenu.values());
			}
		}
	}
}
