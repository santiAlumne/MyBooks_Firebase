package com.soc.uoc.pqtm.pecs.mybooks_santi.model;

import com.facebook.stetho.Stetho;
import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookContent extends SugarApp {

    public void onCreate(){

        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }



    public static List<BookItem> getBooks(){
        return BookItem.listAll(BookItem.class);

    }
    // Si el titol es troba a
    public static boolean exists(BookItem bookItem){
        List<BookItem> llista=getBooks();


        if(llista.size()>0){
            List <BookItem> books= BookItem.<BookItem>find(BookItem.class, "title = ?", bookItem.getTitle());
            if(!books.isEmpty()){
                return true;
            }else return false;

        }else{
            return false;
        }

    }


    public BookContent(){

    }
    @Table(name="book")

    public static class BookItem extends SugarRecord  {

        @Column(name="_id")
        private Long _id;
        @Column(name="title")
        private String title;
        @Column(name="author")
        private String author;
        @Column(name="publication_date")
        private Date publication_date;
        @Column(name="description")
        private String description;
        @Column(name="url_image")
        private String url_image;



        public BookItem(){

        }

        //Crea objectes BookItem

        public BookItem(String id, String title, String author, String publication_date, String description, String url_image) {

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                this.publication_date = dateFormat.parse(publication_date);
                this._id =Long.parseLong(id);
                this.title = title;
                this.author = author;
                this.description = description;
                this.url_image = url_image;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }



        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return this.author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getPublication_date() {
            return this.publication_date;
        }

        public void setPublication_date(String publication_date) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                this.publication_date = dateFormat.parse(publication_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl_image() {
            return this.url_image;
        }

        public void setUrl_image(String url_image) {
            this.url_image = url_image;
        }
    }


}





