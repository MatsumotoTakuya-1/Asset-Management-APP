import { useParams } from "react-router-dom";
import { Typography, Box } from "@mui/material";
import TransactionInputRow from "../components/TransactionInputRow";

const categories = ["Salary", "Freelance", "Investments", "Rental Income", "Other"];

const TransactionInputPage = () => {
    const { yearMonth } = useParams();

    return (
        <Box>
            <Typography variant="h5" textAlign={"left"} gutterBottom>
                Income & Expenses - {yearMonth}
            </Typography>

            {categories.map((cat) => (
                <TransactionInputRow key={cat} category={cat} yearMonth={yearMonth} />
            ))}
        </Box>
    );
};

export default TransactionInputPage;