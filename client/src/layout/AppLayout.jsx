// src/layout/AppLayout.tsx
import {AppBar, Box, Container, Toolbar, Avatar, Button} from "@mui/material";

const AppLayout = ({children}) => {
    return (
        <Box sx={{bgcolor: 'background.default', minHeight: '100vh', width: '100%'}}>
            <AppBar position="static" color="transparent" elevation={0}>
                <Toolbar>
                    <Button color="inherit">Transactions</Button>
                    <Button color="inherit">Budgets</Button>
                    <Button color="inherit">Reports</Button>
                    <Button color="inherit">Settings</Button>
                    <Avatar alt="User" sx={{ml: 2}}/>
                </Toolbar>
            </AppBar>
            <Container sx={{mt: 4}}>
                {children}
            </Container>
        </Box>
    );
};

export default AppLayout;
