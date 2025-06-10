import { Box, TextField, Button } from "@mui/material";
import { useState } from "react";
import TransactionHistoryModal from "./TransactionHistoryModal";

const TransactionInputRow = ({ category, yearMonth }) => {
    const [amount, setAmount] = useState("");
    const [openModal, setOpenModal] = useState(false);

    return (
        <Box display="flex" alignItems="center" gap={2} mb={2}>
            <Box width={150}>{category}</Box>
            <TextField
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="Amount"
                size="small"
            />
            <Button size="small" variant="outlined" onClick={() => setOpenModal(true)}>
                View History
            </Button>

            <TransactionHistoryModal
                open={openModal}
                onClose={() => setOpenModal(false)}
                category={category}
                yearMonth={yearMonth}
            />
        </Box>
    );
};

export default TransactionInputRow;