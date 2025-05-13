import React from 'react';
import AppHeader from './AppHeader';
import './App.css';
import Home from './Home';
import Books from './Books';
import Users from './Users';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <div className="App">
        <AppHeader />
        <Routes>
			<Route path="/" element={<Home />} />
			<Route path="/books" element={<Books />} />
			<Route path="/users" element={<Users />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
