import { TableRow, TableCell, TextField, Button } from "@mui/material";
import { useState } from "react";
import TransactionHistoryModal from "./TransactionHistoryModal";

const TransactionInputRow = ({ category, yearMonth, value, onChange }) => {
    const [openModal, setOpenModal] = useState(false);

    return (
        <>
            <TableRow>
                <TableCell>{category}</TableCell>
                <TableCell>
                    <TextField
                        size="small"
                        type="number"
                        value={value}
                        onChange={(e) => onChange(e.target.value)}
                        placeholder="Amount"
                    />
                </TableCell>
                <TableCell>
                    {value ? `￥${parseFloat(value).toLocaleString()}` : "-"}
                </TableCell>
                <TableCell>
                    <Button size="small" variant="outlined" onClick={() => setOpenModal(true)}>
                        View History
                    </Button>
                </TableCell>
            </TableRow>

            {/* モーダルコンポーネント（非表示時はレンダリングしない） */}
            <TransactionHistoryModal
                open={openModal}
                onClose={() => setOpenModal(false)}
                category={category}
                yearMonth={yearMonth}
            />
        </>
    );
};

export default TransactionInputRow;