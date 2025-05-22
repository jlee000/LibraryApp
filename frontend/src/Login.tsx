import React, { useState } from 'react';
import { TextField, Button } from "@mui/material";
import { useNavigate } from 'react-router-dom';

enum Role {
  STAFF="STAFF", 
  MEMBER="MEMBER"
} 

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = React.useState<string>('');
  const [password, setPassword] = React.useState<string>('');
  
  const [newUsername, setNewUsername] = React.useState<string>('');
  const [newPassword, setNewPassword] = React.useState<string>('');
  const [newFirstName, setNewFirstName] = React.useState<string>('');
  const [newLastName, setNewLastName] = React.useState<string>('');
  const [newEmail, setNewEmail] = React.useState<string>('');
  const [newRole, setNewRole] = React.useState<Role>(Role.MEMBER);
	
  const [error, setError] = useState('');

  const signInSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST', 
		headers: {'Content-Type': 'application/json',},
        body: JSON.stringify({username, password }), 
		credentials: 'include'
      });

      const result = await response.json();

      if (response.ok) {
        console.log('Login success');
		localStorage.setItem('jwt', result.data);
		navigate('/');
      } else { throw new Error(result.message || 'Login failed'); }
    } catch (err) {
      console.error('Login failed:', err);
      setError('Invalid username or password');
    }
  };
 
  const signUpSubmit = async (e: React.FormEvent) => {	
	e.preventDefault();
	await fetch("http://localhost:8080/register", { method: "POST", 
					headers: {"Content-Type": "application/json"},
					body: JSON.stringify( { username:newUsername, password:newPassword, firstName:newFirstName, lastName:newLastName, email:newEmail, role:newRole, active:true } ) })
	.then(() => { console.log("New user added: " + newUsername) });	
	navigate('/');
  };

  if (localStorage.getItem('jwt') !== null) {
    return (
      <div>
        <h2>Welcome!</h2>
        <p>You are logged in.</p>
      </div>
    );
  } else {
  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={signInSubmit}>
        <TextField
          required
          label="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          required
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          fullWidth
          margin="normal"
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
        <Button variant="contained" color="primary" type="submit">
          Login
        </Button>
      </form>
	  
	  <h2>Create Account</h2>
      <form onSubmit={signUpSubmit} style={{ marginBottom: "30px" }}>
        <TextField
          required
          label="Username"
          value={newUsername}
          onChange={(e) => setNewUsername(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          required
          label="Password"
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          required
          label="FirstName"
          type="firstname"
          value={newFirstName}
          onChange={(e) => setNewFirstName(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          required
          label="LastName"
          type="lastname"
          value={newLastName}
          onChange={(e) => setNewLastName(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          required
          label="Email"
          type="email"
          value={newEmail}
          onChange={(e) => setNewEmail(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          disabled
          label="Role"
          type="role"
          value={newRole}
          onChange={(e) => setNewRole(e.target.value.toUpperCase() as Role)}
          fullWidth
          margin="normal"
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
        <Button variant="contained" color="primary" type="submit">
          Create Account
        </Button>
      </form>
	  
    </div>
  );
}
};

export default Login;
