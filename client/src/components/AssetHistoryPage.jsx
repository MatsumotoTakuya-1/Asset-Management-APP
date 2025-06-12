import {useLocation, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {
    Typography,
    Box,
    Tabs,
    Tab,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    Paper, TextField,
} from "@mui/material";

const AssetHistoryPage = () => {
    const {assetId} = useParams();
    const [history, setHistory] = useState([]);

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const assetName = queryParams.get("asset");

    const fetchHistory = async () => {
        try {
            const res = await axios.get(`/api/assets/history/${assetId}`);
            setHistory(res.data);
            console.log(res.data);
        }catch (error) {
            console.log(error);
        }
    }

    //金額の変更
    const handleAmountChange = (index, value) => {
        const updated = [...history];
        updated[index].amount = value;
        setHistory(updated);
    }

    //  更新処理
    const handleUpdate = (assetRecordId, amount) => {
        try{
            axios.put(`/api/assets/history/${assetRecordId}`, {
                amount: amount,
            });
            alert("Updated successfully.");
            fetchHistory();
        }catch (error) {
            console.log(error);
        }
    }

    //削除処理
    const handleDelete = (assetRecordId) => {
        try{
            axios.delete(`/api/assets/history/${assetRecordId}`);
            alert("Deleted successfully.");
            fetchHistory();
        }catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        fetchHistory();
    }, [assetId]);

    return (
        <Box>
            <Typography variant="h5" fontWeight={"bold"} textAlign={"left"} gutterBottom>
                資産履歴 - {assetName}</Typography>

            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>日付</TableCell>
                            <TableCell>金額</TableCell>
                            <TableCell>操作</TableCell>
                        </TableRow>
                    </TableHead>
                <TableBody>
                    {history.map((item, index) => (
                        <TableRow key={index}>
                            <TableCell>{item.yearMonth}</TableCell>
                            <TableCell>
                                <TextField type={"number"} value={item.amount} onChange={(e) => handleAmountChange(index, e.target.value)} size={"small"} />
                            </TableCell>
                            <TableCell>
                                <Button variant={"outlined"} size={"small"} onClick={() => handleUpdate(item.id, item.amount)}>更新</Button>
                                <Button variant={"outlined"} size={"small"} sx={{ml:1}} onClick={() => handleDelete(item.id)}>削除</Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                </Table>
            </TableContainer>
        </Box>
    )
}

export default AssetHistoryPage