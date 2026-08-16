package library_application.domain;

public enum BookGenre {
	FICTION
	, FANTASY
	, MYSTERY
	, ROMANCE
	, THRILLER
	, ADVENTURE
	;
	
	public static BookGenre getGenre(String genre) {
		
		return switch(genre) {
			case "FICTION" -> FICTION;
			case "FANTASY" -> FANTASY;
			case "MYSTERY" -> MYSTERY;
			case "ROMANCE" -> ROMANCE;
			case "THRILLER" -> THRILLER;
			case "ADVENTURE" -> ADVENTURE;
			default -> null;
		};
	}
}