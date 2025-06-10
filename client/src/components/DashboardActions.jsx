import {useNavigate} from 'react-router-dom';
import {Box, Button} from '@mui/material';

const DashboardActions = () => {
    const navigate = useNavigate();
    const currentMonth = new Date().toISOString().slice(0, 7); // "2025-06"

    return (

        <Box mt={2} display="flex" gap={2} textAlign={"left"}>
            <Button
                variant="outlined"
                onClick={() => navigate(`/input/transactions/${currentMonth}`)}
                sx={{
                    fontSize: "0.5rem",
                    borderRadius: 3,
                    textTransform: "none",
                    boxShadow: 3,
                    color: 'black',
                    // backgroundColor: "#007bff",
                    '&:hover': {
                        backgroundColor: "#007bff"
                    }
                }}
            >
                Add Income / Expense
            </Button>

            <Button
                variant="outlined"
                onClick={() => navigate(`/input/assets/${currentMonth}`)}
                sx={{
                    fontSize: "0.5rem",
                    borderRadius: 3,
                    textTransform: "none",
                    boxShadow: 3,
                    color: 'black',
                    // backgroundColor: "#007bff",
                    '&:hover': {
                        backgroundColor: "#007bff"
                    }
                }}
            >
                Add Asset
            </Button>
        </Box>


    );
};

export default DashboardActions;