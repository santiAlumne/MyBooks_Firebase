package com.soc.uoc.pqtm.pecs.mybooks_santi.model;

import com.orm.SugarRecord;

import java.util.List;

public class BookContent extends SugarRecord {
    //eliminina
    public static void delete(Integer bookPosition) {

        BookItem book = BookItem.findById(BookItem.class, bookPosition);
        book.delete();
    }

    public static List<BookItem> getBooks(){
        // ============ INICI CODI A COMPLETAR ===============
        return BookItem.listAll(BookItem.class);
        // ============ FI CODI A COMPLETAR ===============
    }

    public static boolean exists(BookItem bookItem) {
        // ============ INICI CODI A COMPLETAR ===============
        List<BookItem> bookItem1 = BookItem.find(BookItem.class, "title = ?", bookItem.title);
        return bookItem1 != null && bookItem1.size() != 0;
        // ============ FI CODI A COMPLETAR ===============
    }

    /**
     * A book item representing a piece of content.
     */
    public static class BookItem extends SugarRecord {
        public String title;
        public String author;
        public String publicationDate;
        public String description;
        public String urlImage;

        public BookItem() {

        }

        public BookItem(String title, String author, String publicationDate,
                        String description, String urlImage) {
            this.title = title;
            this.author = author;
            this.publicationDate = publicationDate;
            this.description = description;
            this.urlImage = urlImage;
        }

        @Override
        public String toString() {
            return title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPublication_date() {
            return publicationDate;
        }

        public void setPublication_date(String publicationDate) {
            this.publicationDate = publicationDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl_image() {
            return urlImage;
        }

        public void setUrl_image(String urlImage) {
            this.urlImage = urlImage;
        }
    }
}
