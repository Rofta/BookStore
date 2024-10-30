    import java.text.*;
    import java.util.Locale;

    public class Book
    {
        private int Id;
        private String title;
        private String author;
        private double price;
        private int quantity;

        private String publisher;
        private String genre;
        private int yearPublished;

        public Book(int Id, String title, String author, double price, int quantity, String publisher, String genre, int yearPublished)
        {
            setId(Id);
            setAuthor(author);
            setTitle(title);
            setPrice(price);
            setQuantity(quantity);
            setPublisher(publisher);
            setGenre(genre);
            setYearPublished(yearPublished);
        }
        public Book(int Id, String title, String author, double price, int quantity)
        {
            setId(Id);
            setAuthor(author);
            setTitle(title);
            setPrice(price);
            setQuantity(quantity);
            this.publisher = "";
            this.genre = "";
            this.yearPublished = 0;
    }

    public int getId()
    {
        return Id;
    }
    public String getTitle()
    {
        return title;
    }
    public String getAuthor()
    {
        return author;
    }
    public double getPrice()
    {
        return price;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public String getPublisher()
    {
        return publisher;
    }
    public String getGenre()
    {
        return genre;
    }
    public int getYearPublished()
    {
        return yearPublished;
    }

    public void setId(int Id)
    {
        this.Id = Id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }
    public void setGenre(String genre)
    {
        this.genre = genre;
    }
    public void setYearPublished(int yearPublished)
    {
        this.yearPublished = yearPublished;
    }

    NumberFormat us = NumberFormat.getCurrencyInstance(Locale.US);
    @Override
    public String toString()
    {
        return "Id: " + Id + ", Title: " + title + ", Author: " + author + ", Price: " + us.format(getPrice()) + ", Quantity: " + quantity + ", Publisher: " + (publisher.equals("") ? "Not specified" : publisher) + ", Genre: " + (genre.equals("") ? "Not specified" : genre) + ", Year Published: " + (yearPublished == 0 ? "Not specified" : yearPublished);
    }
}