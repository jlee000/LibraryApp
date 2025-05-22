import React,{ useEffect,useState  } from 'react';
import './App.css';
import { useNavigate } from 'react-router-dom';
import { Button, Grid, Card, CardContent, Typography, CardMedia } from '@mui/material';
import { DateTime } from 'luxon';

interface User {
	id?: number
	username: string,
	password: string,
    firstname: string;
	lastname: string;
    email: string;
	role: string;
	active: boolean;
}   

interface ApiResponse {
	data: any;
	message: string;
} 

interface Image {
  id?: number;        
  filename: string;
  filetype: string;       
  downloadurl: string;
  book: Book;             
}

interface Author {
	id?: number;
	name: string;
	biography: string;
	books: Book[];
}

interface Book {
	id?: number;
	title: string;
	isbn: string;
    description: string;
	stock: number;
    price: number;
	author: Author;
	images: Image[];
}

interface Cart {
  id?: number;
  user: User;
  cartItems: CartItem[];
}

interface CartItem {
  id?: number;
  book: Book;
  quantity: number;
  cart: Cart;
}

interface BookLoan {
  id: number;                            
  loanDate: DateTime;               
  daysToLoan: number;                    
  dueDate: DateTime;                
  returnDate: DateTime | null;      
  loanStatus: LoanStatus;                
  user: User;                           
  bookLoanItems: BookLoanItem[];         
}

enum LoanStatus {
	LOAN="LOAN", RETURNED="RETURNED", OVERDUE="OVERDUE", LOST="LOST", CANCELLED="CANCELLED",
}

interface BookLoanItem {
  id: number;
  quantity: number; 
  bookLoan: BookLoan;     
  book: Book;              
}
function MyBooks() {
  const navigate = useNavigate();
  const [cart, setCart] = useState<Cart>({ id: undefined, user: {} as User, cartItems: [] });
  const [books, setBooks] = useState<Book[]>([]);
  const [loans, setLoans] = useState<BookLoan[]>([]);
  const [booksLoaned, setBooksLoaned] = useState<Book[]>([]);

  const getLoggedInUser = async () => {
    if (localStorage.getItem('jwt') !== null) {
      const response = await fetch('http://localhost:8080/loggedinuser', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      });
      const jsonResponse: ApiResponse = await response.json();
      return jsonResponse.data;
    }
  };

  const getCart = async () => {
    const loggedInUser = await getLoggedInUser();
    if (loggedInUser) {
      const response = await fetch(`http://localhost:8080/cart/username/${loggedInUser.username}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
      });
      const jsonResponse: ApiResponse = await response.json();
      const c: Cart = jsonResponse.data;
      setCart(c);

      if (c?.cartItems?.length > 0) {
        const booksResponse = await fetch(`http://localhost:8080/cartitem/books/${c.id}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
        });
        const booksResponseData: ApiResponse = await booksResponse.json();
        setBooks(booksResponseData.data);
      } else { setBooks([]); }
    }
  };

  const getLoans = async () => {
	const loggedInUser = await getLoggedInUser();
	if (loggedInUser) {
	  const response = await fetch(`http://localhost:8080/loan/user/${loggedInUser.id}`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
	  });
	  const jsonResponse: ApiResponse = await response.json();
	  setLoans(jsonResponse.data);

	  const booksLoanedList: Book[] = [];
	  for (const loan of jsonResponse.data) {
		const responsee = await fetch(`http://localhost:8080/loanitems/${loan.id}`, {
		  method: 'GET',
		  headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
		});
		const loanedBooksResponse: ApiResponse = await responsee.json();
		booksLoanedList.push(...loanedBooksResponse.data);
	  }
	  setBooksLoaned(booksLoanedList);
	}
  };

  const removeItemFromCart = async (bookid: number, title: string) => {
  if (!cart || !cart.id) {
	console.error('Cart is not defined or missing id');
	return;
  }

	await fetch(`http://localhost:8080/cartitem/${cart.id}/${bookid}`, {
	  method: 'DELETE',
	  headers: {
		'Content-Type': 'application/json',
		Authorization: `Bearer ${localStorage.getItem('jwt')}`,
	  },
	});
	  console.log(`Item removed from cart: ${title}`);
	  await getCart();
  };


  const clearCart = async () => {
    await fetch(`http://localhost:8080/cart/${cart?.id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
    });
    setBooks([]);
    setCart({ id: undefined, user: {} as User, cartItems: [] });
	console.log("Cart cleared");
    await getCart();
  };

  const returnBooks = async () => {
    const loanid = loans.find((loan) => loan.loanStatus !== LoanStatus.RETURNED)?.id;
    if (loanid) {
      await fetch(`http://localhost:8080/loan/return/${loanid}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
      });
      setLoans([]);
      await getCart();
    }
  };

  const createLoan = async () => {
    const loggedInUser = await getLoggedInUser();
    if (loggedInUser?.id) {
      await fetch(`http://localhost:8080/loan/${loggedInUser.id}/5`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.getItem('jwt')}` },
      });
      setBooks([]);
      await getCart();
    }
  };

  useEffect(() => {
    getCart();
    getLoans();
  }, []);

  if (localStorage.getItem('jwt') == null) {
    return (
      <div className="App">
        <h1>Cart</h1>
        <p>Cart is empty, please Log in / Create Account</p>
        <Button variant="contained" color="primary" onClick={() => navigate('/login')}>
          Login / Create Account
        </Button>
      </div>
    );
  } else if (localStorage.getItem('jwt') !== null && !books.length && !loans.length) {
    return (
      <div className="App">
        <h1>My Books</h1>
        <p>Nothing to show here<br />Browse the Books section</p>
      </div>
    );
  } else {
    return (
      <div className="App">
        <h1>My Books</h1>
        <Grid container spacing={3}>
          {books.length > 0 ? (
            books.map((book) => (
              <Grid key={book.id}>
                <Card>
                  <CardMedia
                    component="img"
                    alt={book.title}
                    height="150"
                    image={book.images?.length ? book.images[0].downloadurl : '/logo192.png'}
                    onError={(e) => (e.currentTarget.src = '/logo192.png')}
                  />
                  <CardContent>
                    <Typography>{book.title}</Typography>
                    <Typography variant="body2" color="textSecondary">
                      {book.description}
                    </Typography>
                    <Typography variant="body2">{book.author.name}</Typography>
                    <Typography variant="body2">£{book.price}</Typography>
                    <Button
                      color="primary"
                      onClick={() => removeItemFromCart(book.id!, book.title)}
                    >
                      Remove from cart
                    </Button>
                  </CardContent>
                </Card>
              </Grid>
            ))
          ) : (
            <p>Nothing to show here, browse the Books section</p>
          )}
        </Grid>
        <br />
        {books.length > 0 && (
          <>
            <Button variant="contained" color="primary" onClick={clearCart}>
              Clear cart
            </Button>
            <br />
            <Button variant="contained" color="primary" onClick={createLoan}>
              Loan Books
            </Button>
          </>
        )} 
        <br />
        {loans.length > 0 && booksLoaned.length > 0 && (
          <div> 
            <h2>Books Loaned</h2>
            {booksLoaned.map((book, index) => {
              const loan = loans.find((loan) => loan.bookLoanItems.some((item) => item.book.id === book.id && loan.bookLoanItems.find((i) => i.id = loan.id)));
			  
              return (
                <Grid key={index}>
                  <Card>
                    <CardMedia
                      component="img"
                      alt={book.title}
                      height="150"
                      image={book.images?.length ? book.images[0].downloadurl : '/logo192.png'}
                      onError={(e) => (e.currentTarget.src = '/logo192.png')}
                    />
                    <CardContent>
                      <Typography>{book.title}</Typography>
                      <Typography variant="body2" color="textSecondary">
                        {book.description}
                      </Typography>
                      <Typography variant="body2">{book.author.name}</Typography>
                      <Typography variant="body2">£{book.price}</Typography>
                      {loan && (
                        <>
                          <Typography variant="body2">Loan status: {loan.loanStatus}</Typography>
                          {loan.bookLoanItems.map((item) => item.book.id === book.id && (
                            <Typography variant="body2" key={item.book.id}>
                              Quantity: {item.quantity}
                            </Typography>
                          ))}
                        </>
                      )}
                    </CardContent>
                  </Card>
                </Grid>
              );
            })}
            {loans.some((loan) => loan.loanStatus !== LoanStatus.RETURNED) && (
              <Button variant="contained" color="primary" onClick={returnBooks}>
                Return Books
              </Button>
            )}
          </div>
        )}
      </div>
    );
  }
}
export default MyBooks;