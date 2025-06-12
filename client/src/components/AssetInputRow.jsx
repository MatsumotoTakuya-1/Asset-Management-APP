import {TableRow, TableCell, TextField, Button} from "@mui/material";
// import {useState} from "react";
import {useNavigate} from "react-router";


const AssetInputRow = ({assetName, value, onChange, assetId, yearMonth, totalForMonth, currentTotalForMonth}) => {
    const navigate = useNavigate();


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
                        placeholder="今月の資産額"
                    />
                </TableCell>
                <TableCell>
                    {totalForMonth !== undefined
                        ? `￥${Number(totalForMonth).toLocaleString()}`
                        : "-"}
                </TableCell>
                <TableCell>
                    {currentTotalForMonth !== undefined
                        ? `${(((Number(currentTotalForMonth) - Number(totalForMonth)) / Number(totalForMonth)) * 100).toFixed(1)}%`
                        : "-"}
                </TableCell>
                <TableCell>
                    <Button size="small" variant="outlined" onClick={() => navigate(`/input/assets/${assetId}/history?asset=${assetName}`)}>
                        View History
                    </Button>
                    {/*/input/assets/:yearMonth/:assetId/historyに遷移予定*/}
                </TableCell>
            </TableRow>


        </>
    );
};

export default AssetInputRow;