import React,{ useEffect } from 'react';
import './App.css';
import { Button, Grid, Card, CardContent, Typography, CardMedia } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';

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
	images?: Image[];
}

function BooksInternal() {
	const [books, setBooks] = React.useState<Book[]>([]);
	
	
    //Create Book  
	const [title, setTitle] = React.useState<string>('');
	const [isbn, setIsbn] = React.useState<string>('');
	const [desc, setDesc] = React.useState<string>('');
    const [stock, setStock] = React.useState<number>(0);
    const [price, setPrice] = React.useState<number>(0);
	const [authorName, setAuthorName] = React.useState<string>('');
	const [authorBio, setAuthorBio] = React.useState<string>('');
	
	//Update Book
	const [newTitle, setNewTitle] = React.useState<string>('');
	const [newIsbn, setNewIsbn] = React.useState<string>('');
	const [newDesc, setNewDesc] = React.useState<string>('');
    const [newStock, setNewStock] = React.useState<number>();
    const [newPrice, setNewPrice] = React.useState<number>();
	const [newAuthorName, setNewAuthorName] = React.useState<string>('');
	const [newAuthorBio, setNewAuthorBio] = React.useState<string>('');

	const printBookBeforeAndAfter = async (bb:Book, b:Book) => { 
        console.log("Book before: ", "Id: ",bb.id, ", Title: ",bb.title, ", ISBN: ",bb.isbn, ", Desc: ",bb.description, ", Stock: ",bb.stock, ", Price: ",bb.price, ", Author: ",bb.author.name,	 "\nBook after: ", "Id: ",b.id, ", Title: ",b.title, ", ISBN: ",b.isbn, ", Desc: ",b.description, ", Stock: ",b.stock, ", Price: ",b.price, ", Author: ",b.author.name);
    };	

	const deleteBook = async (b: Book) => {
		await fetch(`http://localhost:8080/book/${b.id}`, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json", 
				Authorization: `Bearer ${ localStorage.getItem('jwt') }`
					},})
		.then(() => {
			console.log("Book deleted: " + b.title);
			getBooks();
		}).catch(err => {
			console.error("Delete failed", err);
		});
	};
	
    const addBook = async (b: Book) => {
        await fetch("http://localhost:8080/book", { 
						method: "POST", 
						headers: { 
							"Content-Type": "application/json", 
							Authorization: `Bearer ${ localStorage.getItem('jwt') }`  
						},
						body: JSON.stringify(b) })
        .then(() => { console.log("New book added: " + b.title); });
		getBooks();
    };
	
    const updateBook = async (b: Book) => {
        let oldBookInfo = books.filter(x => x.id === b.id);
        await fetch("http://localhost:8080/book", { 
						method: "PUT", 
						headers: { 
							"Content-Type": "application/json", 
							Authorization: `Bearer ${ localStorage.getItem('jwt') }` 
						}, 
						body: JSON.stringify(b) })
           .then(() => { printBookBeforeAndAfter(oldBookInfo[0], b); });
		   getBooks();
    };

	const getBooks = async () => {
	  const response = await fetch("http://localhost:8080/book", {
		method: "GET", headers: { "Content-Type": "application/json", Authorization: `Bearer ${ localStorage.getItem('jwt') }` }
	  });
		const jsonResponse: ApiResponse = await response.json();
		const b: Book[] = jsonResponse.data;
		setBooks(b);
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
		<h1>Internal Book Management</h1>
		<Grid container spacing={3}>
		  {books.map((book) => (
			<Grid key={book.id}>
			  <Card>
				<CardMedia
				  component="img"
				  alt={book.title}
				  height="150"
				  image={book?.images && book.images?.length > 0 && book.images[0]?.downloadurl ?
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
					{book?.images && book.images.map((image) => (
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
				</CardContent>
				<IconButton
					edge="end"
					aria-label="delete"
					onClick={() => { deleteBook(book); }}
				>
				<DeleteIcon />
				</IconButton>
			  </Card>
			</Grid>
		  ))}
		</Grid>
		
	
            <h2><u>Add Book</u></h2>
            <br /><br />
            <div>
                <TextField
                    required
                    label="Title"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => { setTitle(e.target.value) }}
                /> 
                <TextField
                    required
                    label="ISBN"
                    placeholder="ISBN"
                    value={isbn}
                    onChange={(e) => { setIsbn(e.target.value) }}
                />    				
                <TextField
                    required
                    label="Description"
                    placeholder="Description"
                    value={desc}
                    onChange={(e) => { setDesc(e.target.value) }}
                />
                <TextField
                    label="Stock"
                    placeholder="Stock"
                    value={stock}
                    onChange={(e) => setStock(Number(e.target.value))}
                />
                <TextField
                    label="Price"
                    placeholder="Price"
                    value={price}
                    onChange={(e) => setPrice(Number(e.target.value))}
                />
                <TextField
                    label="Author Name"
                    placeholder="Author Name"
                    value={authorName}
                    onChange={(e) => setAuthorName(e.target.value)}
                />
                <TextField
                    label="Author biography"
                    placeholder="Author biography"
                    value={authorBio}
                    onChange={(e) => setAuthorBio(e.target.value)}
                />
                <br /><Button variant="contained" color="primary" onClick={() => { addBook({ title: title, isbn: isbn, description: desc, stock: stock, price: price, author: { name: authorName, biography: authorBio, books: [] } }); }} > Add Book</Button>

				

            </div>

            <br /><br /><br /><br /><br />
			
            <h2><u>Modify Book</u></h2>
            {books && books.map(book => (
                <div key={book.id}>
                    <TextField
                        disabled
                        placeholder="Id"
                        label="Id"
                        value={book.id}
                    />
                    <TextField
                        placeholder="Title"
                        label={book.title}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewTitle(e.target.value) }}
                    />
                    <TextField
                        placeholder="ISBN"
                        label={book.isbn}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewIsbn(e.target.value) }}
                    />
                    <TextField
                        placeholder="Description"
                        label={book.description}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewDesc(e.target.value) }}
                    />
                    <TextField
                        placeholder="Stock"
                        label={book.stock}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewStock(Number(e.target.value)) }}
                    />
                    <TextField
                        placeholder="Price"
                        label={book.price}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewPrice(Number(e.target.value)) }}
                    />
                    <TextField
                        placeholder="Author Name"
                        label={book.author.name}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewAuthorName(e.target.value) }}
                    />
                    <TextField
                        placeholder="Author Bio"
                        label={book.author.biography}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewAuthorBio(e.target.value) }}
                    />
                    <IconButton
                        edge="end"
                        aria-label="update"
					onClick={async () => {
						await updateBook({ id:book.id, title: newTitle, isbn: newIsbn, description: newDesc, stock: Number(newStock), price: Number(newPrice), author: { name: newAuthorName, biography: newAuthorBio, books: [] } });
					}}
                    >
                        Update							
                    </IconButton>
                </div>
            ))}
			
			
		</div>
	  );
	}
}
export default BooksInternal;