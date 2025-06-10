import {TableRow, TableCell, TextField, Button} from "@mui/material";
import {useState} from "react";
import TransactionHistoryModal from "./TransactionHistoryModal";

const AssetInputRow = ({assetName, yearMonth, value, onChange, totalForMonth}) => {
    const [openModal, setOpenModal] = useState(false);

    return (
        <>
            <TableRow>
                <TableCell>{assetName}</TableCell>
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
                    {totalForMonth !== undefined
                        ? `￥${Number(totalForMonth).toLocaleString()}`
                        : "-"}
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
                category={assetName}
                yearMonth={yearMonth}
            />
        </>
    );
};

export default AssetInputRow;