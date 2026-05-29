import React,{ useEffect } from 'react';
import './App.css';
import { Button, Grid, Card, CardContent, Typography, CardMedia } from '@mui/material';

interface ApiResponse {
	data: any;
	message: string;
}

interface Image {
  id?: number;        
  filename: string;
  filetype: string;
  image: Blob;          
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

function Books() {
	const [books, setBooks] = React.useState<Book[]>([]);
	
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
  
	const getBooks = async () => {
	  const response = await fetch("http://localhost:8080/book", {
		method: "GET", headers: { "Content-Type": "application/json", Authorization: `Bearer ${ localStorage.getItem('jwt') }` }
	  });
		const jsonResponse: ApiResponse = await response.json();
		const b: Book[] = jsonResponse.data;
		setBooks(b);
	};

    const addItemToCart = async (bookid: number, title: string) => {
		const quantity = 1;
		const user = await getLoggedInUser();
		const username = user.username;
        await fetch(`http://localhost:8080/cartitem/${bookid}/${quantity}/${username}`,
					{ method: "POST", 
					headers: { "Content-Type": "application/json", Authorization: `Bearer ${ localStorage.getItem('jwt') }` } })
        .then(() => { console.log("Item added to cart: " + title); });
    };

	useEffect(() => {
		if(localStorage.getItem('jwt') !== null){ 
			getBooks();
		}
	}, []);



  if(!books[0]){
		return (
			<div className="App"> 
			<h1>Books</h1>
			<p>No books / Must be logged in</p> 
			</div>
		);
	}else{
	  return (
		<div className="App"> 
		<h1>Books</h1>
		<Grid container spacing={3}>
		  {books.map((book) => (
			<Grid key={book.id}>
			  <Card>
				<CardMedia
				  component="img"
				  alt={book.title}
				  height="150"
				  image={book.images?.length > 0 && book.images[0]?.downloadurl ?
							book.images[0].downloadurl : "/logo192.png"}
				  onError={(e) => (e.currentTarget.src = "/logo192.png")}
				/>
				<CardContent style={{ padding: '2px', fontSize: '1px' }}>
				  <Typography>
					{book.title}
				  </Typography>
				  <Typography variant="body2" color="textSecondary">
					{book.description}
				  </Typography>						  
				  <Typography variant="body2">
					{book.author.name}
				  </Typography>
				  <Typography variant="body2" >£{book.price}</Typography>
				  
				  <Grid container spacing={1} direction="row">
					{book.images.map((image) => (
					  <Grid key={image.id}>
						<CardMedia
						  component="img"
						  alt={image.filename}
						  height="40"
						  image={image.downloadurl}
						  onError={(e) => (e.currentTarget.src = "/logo192.png")}
						/>
					  </Grid>
					))}
				  </Grid>
				  <Button color="primary" type="submit" onClick={ () => { if(book.id) {addItemToCart(book.id, book.title) } else{ console.error("book.id is undefined") } } }> Add to cart </Button>
				</CardContent>
			  </Card>
			</Grid>
		  ))}
		</Grid>
		</div>
	  );
	}
}
export default Books;