import React,{ useEffect } from 'react';
import './App.css';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';

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

function Account() {
	const navigate = useNavigate();
	const [user, setUser] = React.useState<User>();
	
	const getUserData = async () => {
		if (localStorage.getItem('jwt') !== null){
			const response = await fetch('http://localhost:8080/loggedinuser', {
			method: 'GET',
			headers: {
			  Authorization: `Bearer ${ localStorage.getItem('jwt') }`
			}
			});
			const jsonResponse: ApiResponse = await response.json();
			const u: User = jsonResponse.data;
			setUser(u);
		}
	};

	useEffect(() => {
		getUserData();
	}, []);
	
  return (
	<div className="App">
	 <h1>Account</h1>
		<h2>Details</h2>
		{user ? ( <List>
			<ListItem>		
				Username: {user.username} <br />
				FirstName: {user.firstname} <br />
				LastName: {user.lastname} <br />
				Email: {user.email} <br />
				Role: {user.role} <br />
				Active: {user.active ? "True" : "False"} <br />
			</ListItem>
		</List>) : 
		( <div> <p>You are not logged in</p> 
				<Button variant="contained" color="primary" type="submit" onClick={ () => navigate('/login') }> Login / Create Account </Button> 
		</div>  )}
    </div>
  );
}
export default Account;