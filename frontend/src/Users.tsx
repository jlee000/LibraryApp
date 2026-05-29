import React,{ useRef, useEffect } from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import List from '@mui/material/List';
import { Button } from '@mui/material';
import ListItem from '@mui/material/ListItem';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import Switch from '@mui/material/Switch';
import FormControl from '@mui/material/FormControl';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';

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

enum Role {
  STAFF="STAFF", 
  MEMBER="MEMBER"
}

const Users = ()=> {
	const divRef = useRef<HTMLDivElement | null>(null);
    const url: string = "http://localhost:8080/user";
    const urll: string = "http://localhost:8080/user/";

    //Create User  
	const [username, setUsername] = React.useState<string>('');
	const [password, setPassword] = React.useState<string>('');
	const [firstname, setFirstName] = React.useState<string>('');
    const [lastname, setLastName] = React.useState<string>('');
    const [email, setEmail] = React.useState<string>('');
	const [role, setRole] = React.useState<Role>(Role.MEMBER);
	
	//Update User
	const [newUsername, setNewUsername] = React.useState<string>('');
    const [newFirstName, setNewFirstName] = React.useState<string>('');
	const [newLastName, setNewLastName] = React.useState<string>('');
    const [newEmail, setNewEmail] = React.useState<string>('');
	const [newRole, setNewRole] = React.useState<Role>(Role.MEMBER);

    //All
    const [users, setUsers] = React.useState<User[]>([]);
	
	const printUserBeforeAndAfter = async (ub:User, u:User) => {
        console.log("User before: ", "Id: ",ub.id, ", Username: ",ub.username, ", Firstname: ",ub.firstname, ", Lastname: ",ub.lastname, ", Email: ",ub.email, ", Role: ",ub.role, ", Active: ",ub.active,	 "\n\nUser after: ", "Id: ",u.id, ", Username: ",u.username, ", Firstname: ",u.firstname, ", Lastname: ",u.lastname, ", Email: ",u.email, ", Role: ",u.role, ", Active: ",u.active);
    };

    const addUser = async (u: User) => {
        await fetch(url, { method: "POST", 
						headers: { 
							"Content-Type": "application/json", 
							Authorization: `Bearer ${ localStorage.getItem('jwt') }`  
						},
						body: JSON.stringify(u) })
        .then(() => { console.log("New user added: " + u.username); });
		reloadPage();
    };

	const deleteUser = async (u: User) => {
		await fetch(`${urll}${u.id}`, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json", 
				Authorization: `Bearer ${ localStorage.getItem('jwt') }`
					},})
		.then(() => {
			console.log("User Id deleted: " + u.id);
			reloadPage();
		}).catch(err => {
			console.error("Delete failed", err);
		});
	};

    const updateUser = async (u: User) => {
        let oldUserInfo = users.filter(x => x.id === u.id);
        await fetch(url, { method: "PUT", 
						headers: { 
							"Content-Type": "application/json", 
							Authorization: `Bearer ${ localStorage.getItem('jwt') }` 
						}, 
						body: JSON.stringify(u) })
           .then(() => { printUserBeforeAndAfter(oldUserInfo[0], u); });
		   reloadPage();
    };
	
	const updateUserStatus = async (u: User) => {
        let oldUserInfo = users.filter(x => x.id === u.id);
        await fetch(url, { method: "PUT", 
						headers: { 
							"Content-Type": "application/json", 
							Authorization: `Bearer ${ localStorage.getItem('jwt') }`  
						}, 
						body: JSON.stringify(u) })
           .then(() => { printUserBeforeAndAfter(oldUserInfo[0], u); });
		   reloadPage();
    };

    const reloadPage = async () => {
	if (divRef.current) {
        const response = await fetch(url, { method: "GET", credentials: 'include' });
		const jsonResponse: ApiResponse = await response.json();
		const u: User[] = jsonResponse.data;
        setUsers(u);
	}
    };

    useEffect(() => {
        if (divRef.current) { reloadPage(); }
    }, []);

    return (
        <Box ref={divRef}
            component="form"
            noValidate
            autoComplete="off"
        >	
		<h1>Internal User Management</h1>
            <h2><u>All Users</u></h2>
            <List>
                {users && users.map(user => (
                    <ListItem key={user.id}>
					    <IconButton
                            edge="end"
                            aria-label="delete"
                            onClick={() => { deleteUser(user); }}
                        >
                        <DeleteIcon />
                        </IconButton>
						
                        Id: {user.id} <br />
						Username: {user.username} <br />
                        FirstName: {user.firstname} <br />
						LastName: {user.lastname} <br />
                        Email: {user.email} <br />
						Role: {user.role} <br />
						Active: {user.active ? "True" : "False"} <br />
                    </ListItem>
                ))}
            </List>
			
            <h2><u>Add User</u></h2>
            <br /><br />
            <div>
                <TextField
                    required
                    label="Username"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => { setUsername(e.target.value) }}
                /> 
                <TextField
                    required
                    label="Password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => { setPassword(e.target.value) }}
                />    				
                <TextField
                    required
                    label="FirstName"
                    placeholder="FirstName"
                    value={firstname}
                    onChange={(e) => { setFirstName(e.target.value) }}
                />
                <TextField
                    label="LastName"
                    placeholder="LastName"
                    value={lastname}
                    onChange={(e) => setLastName(e.target.value)}
                />
                <TextField
                    label="Email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <TextField
                    label="Role"
                    placeholder="Role"
                    value={role}
                    onChange={(e) => setRole(e.target.value.toUpperCase() as Role)}
                />
                <br /><Button variant="contained" color="primary" onClick={() => { addUser( { username, password, firstname, lastname, email, role, active:true } ); }}>Add User</Button>
            </div>

            <br /><br /><br /><br /><br />

            <h2><u>Modify User</u></h2>
            {users && users.map(user => (
                <div key={user.id}>
                    <TextField
                        disabled
                        placeholder="Id"
                        label="Id"
                        value={user.id}
                    />
                    <TextField
                        placeholder="Username"
                        label={user.username}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewUsername(e.target.value) }}
                    />
                    <TextField
                        placeholder="FirstName"
                        label={user.firstname}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewFirstName(e.target.value) }}
                    />
                    <TextField
                        placeholder="LastName"
                        label={user.lastname}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewLastName(e.target.value) }}
                    />
                    <TextField
                        placeholder="Email"
                        label={user.email}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewEmail(e.target.value) }}
                    />
                    <TextField
                        placeholder="Role"
                        label={user.role}
                        inputProps={{ type: "text" }}
                        onChange={(e) => { setNewRole(e.target.value.toUpperCase() as Role) }}
                    />
                    <IconButton
                        edge="end"
                        aria-label="update"
                        onClick={() => {
                            updateUser({ id:user.id, username:newUsername, password:user.password, firstname:newFirstName, lastname:newLastName, email:newEmail, role:newRole, active:user.active });
                        }}
                    >
                        Update							
                    </IconButton>
					
					<div>
						<FormControl component="fieldset" variant="standard">
						  <FormGroup>
							<FormControlLabel
							  control={
								<Switch 
									checked={user.active} 
									onChange={(event) => {
									updateUserStatus({ id:user.id, username:user.username, password:user.password, firstname:user.firstname, lastname:user.lastname, email:user.email, role:user.role, active:event.target.checked });
									}}
								/>
							  }
							  label="Active?"
							/>
						  </FormGroup>
						  </FormControl>
					</div>
                </div>
            ))}
        </Box>
    );
}
export default Users;