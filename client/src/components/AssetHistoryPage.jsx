import {useLocation, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {
    Typography,
    Box,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    TextField, Alert, Snackbar,
} from "@mui/material";

const AssetHistoryPage = () => {
    const {assetId} = useParams();
    const [history, setHistory] = useState([]);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState("success");

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const assetName = queryParams.get("asset");

    const showSnackbar = (message, severity = "success") => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };

    const fetchHistory = async () => {
        try {
            const res = await axios.get(`/api/assets/history/${assetId}`);
            setHistory(res.data);
            // console.log(res.data);
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
    const handleUpdate = async  (assetRecordId, amount) => {
        try{
            await axios.put(`/api/assets/history/${assetRecordId}`, {
                amount: amount,
            });
            // alert("Updated successfully.");
            showSnackbar("Successfully updated history", "success");
            fetchHistory();
        }catch (error) {
            console.log(error);
            showSnackbar("Error updated history", "error");

        }
    }

    //削除処理
    const handleDelete = (assetRecordId) => {
        try{
            axios.delete(`/api/assets/history/${assetRecordId}`);
            // alert("Deleted successfully.");
            showSnackbar("Successfully delete asset record", "success");
            fetchHistory();
        }catch (error) {
            console.log(error);
            showSnackbar("Error delete asset record", "error");

        }
    }

    useEffect(() => {
        fetchHistory();
    }, [assetId]);

    return (
        <>
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

    <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
    >
        <Alert onClose={() => setSnackbarOpen(false)} severity={snackbarSeverity} sx={{ width: '100%' }}>
            {snackbarMessage}
        </Alert>
    </Snackbar>
    </>
    )
}

export default AssetHistoryPage