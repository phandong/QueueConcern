package com.common;

import rx.subjects.ReplaySubject;

public class Library {
	private final ReplaySubject<ObservableBook> onAdd = ReplaySubject.create();

	public void addBook(ObservableBook book) {
		onAdd.onNext(book);
	}

	public rx.Observable<ObservableBook> getBooks() {
		return onAdd;
	}
}
