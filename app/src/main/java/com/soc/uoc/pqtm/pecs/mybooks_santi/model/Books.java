package com.soc.uoc.pqtm.pecs.mybooks_santi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

    public class Books implements Serializable{


    public Books(){

    }


     public static class BookItem implements Serializable {

        private Integer bookId;
        private String title;
        private String author;
        private Date publication_date;
        private String description;
        private String url_image;



        public BookItem(){

        }

        public BookItem(String bookId, String title, String author, String publication_date, String description, String url_image) {

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                this.publication_date = dateFormat.parse(publication_date);
                this.bookId =Integer.parseInt(bookId);
                this.title = title;
                this.author = author;
                this.description = description;
                this.url_image = url_image;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        public Integer getBookId() {
            return this.bookId;
        }

        public void setBookId(Integer bookId) {
            this.bookId = bookId;
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





