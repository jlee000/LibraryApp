import React from 'react';
import AppHeader from './AppHeader';
import './App.css';
import Home from './Home';
import Books from './Books';
import Users from './Users';
import Login from './Login';
import MyBooks from './MyBooks';
import BooksInternal from './BooksInternal';
import Account from './Account';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <div className="App">
        <AppHeader />
        <Routes>
			<Route path="/" element={<Home />} />
			<Route path="/books" element={<Books />} />
			<Route path="/login" element={<Login />} />
			<Route path="/mybooks" element={<MyBooks />} />
			<Route path="/account" element={<Account />} />
			<Route path="/users" element={<Users />} />
			<Route path="/booksinternal" element={<BooksInternal />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
