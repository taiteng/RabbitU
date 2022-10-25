package com.example.rabbitu;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookLab {
    private static BookLab sBookLab;
    private List<Book> mBooks;

    public static BookLab get(Context context){
        if(sBookLab==null){
            sBookLab=new BookLab(context);
        }
        return sBookLab;
    }
    private BookLab(Context context){
            mBooks=new ArrayList<>();
            mBooks.add(new Book("Java Book",new File("android.resource://com.example.rabbitu/assets/Java Book.pdf")));
    }
    public Book getBook(UUID id){
        for(Book book:mBooks){
            if (book.getID().equals(id)){
                return book;
            }

        }
        return null;
    }
}
