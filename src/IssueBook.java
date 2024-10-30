public class IssueBook
{
    private int userId;
    private int bookId;

    public IssueBook(int userId, int bookId)
    {
        this.userId = userId;
        this.bookId = bookId;
    }

    public int getUserId()
    {
        return userId;
    }
    public int getBookId()
    {
        return bookId;
    }
}
