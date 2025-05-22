import React, { useEffect, useState, useCallback } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

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

const pages = ['Home', 'Books', 'Internal User Management', 'Internal Book Management'];
const settings = ['Log in', 'Account', 'My Books', 'Logout'];
const pageRoutes: { [key: string]: string } = {
      Home: '/',
      Books: '/books',
      'Internal User Management': '/users',
	  'Internal Book Management': '/booksinternal'
};
const userRoutes: { [key: string]: string } = {
      'Log in': '/login',
      Account: '/account',
      'My Books': '/mybooks',
	  Logout: '/logout'
};

const AppHeader = ()=> {
  const navigate = useNavigate();
  const [showInternalUserPages, setShowInternalUserPages] = useState(true);
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

  const handleMenuItemClick = (page: string) => {
    handleCloseNavMenu();
    const route = pageRoutes[page];
    if (route) {
      navigate(route);
    }
  };

  const handleUserMenuClick = (setting: string) => {
    handleCloseUserMenu();
    const route = userRoutes[setting];
    if (route) {
      if (setting === 'Logout') {
		Cookies.remove('JSESSIONID', { path: '/' });
		Cookies.remove('JWT', { path: '/', domain: 'localhost', secure: true });
		localStorage.removeItem('jwt');
        navigate('/');
      } else {
        navigate(route);
      }
    }
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleOpenNavMenu = async(event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
	updateNavOptionsGetLoggedInUser();
  };

  const handleCloseNavMenu = async() => {
    setAnchorElNav(null);
	updateNavOptionsGetLoggedInUser();
  };
  
  const updateNavOptionsGetLoggedInUser = useCallback(async () => {
    if (!localStorage.getItem('jwt')) {setShowInternalUserPages(false); return; }
    const response = await fetch('http://localhost:8080/loggedinuser', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwt')}`,
      },
    });

    if (response.ok) {
      const jsonResponse: ApiResponse = await response.json();
      const u: User = jsonResponse.data;
      if (u && u.role === Role.STAFF) { setShowInternalUserPages(true); } 
	  else { setShowInternalUserPages(false); }
    }
  }, []);
  
  const visiblePages = pages.filter((page) => {
    if (page === 'Internal User Management' || page === 'Internal Book Management') {
      return showInternalUserPages;
    }
    return true; 
  });
  
  useEffect(() => {
    updateNavOptionsGetLoggedInUser();
  }, [updateNavOptionsGetLoggedInUser]);
  
  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <AdbIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="#app-bar-with-responsive-menu"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            LOGO
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{ display: { xs: 'block', md: 'none' } }}
            >
			{visiblePages.map((page) => (
			  <MenuItem key={page} onClick={() => handleMenuItemClick(page)}>
				<Typography textAlign="center">{page}</Typography>
			  </MenuItem>
              ))}
            </Menu>
          </Box>
          <AdbIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
		  
			<Typography
			  variant="h5"
			  noWrap
			  component="div"
			  onClick={() => navigate('/')}
			  sx={{
				mr: 2,
				display: { xs: 'flex', md: 'none' },
				flexGrow: 1,
				fontFamily: 'monospace',
				fontWeight: 700,
				letterSpacing: '.3rem',
				color: 'inherit',
				textDecoration: 'none',
				cursor: 'pointer'
			  }}
			>LIBRARY
			</Typography>
			
			<Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
			  {visiblePages.map((page) => (
				<Button
				  key={page}
				  onClick={() => {
					handleCloseNavMenu();
					const route = pageRoutes[page] || '/';
					navigate(route);
				  }}
				  sx={{ my: 2, color: 'white', display: 'block' }}
				>
				  {page}
				</Button>
			  ))}
			</Box>
           <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar alt="User Avatar" />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {settings.map((setting) => (
                <MenuItem key={setting} onClick={() => handleUserMenuClick(setting)}>
                  <Typography sx={{ textAlign: 'center' }}>{setting}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default AppHeader;