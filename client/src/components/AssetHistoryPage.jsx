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
    Paper,
} from "@mui/material";

const AssetHistoryPage = () => {
    const {yearMonth, assetId} = useParams();
    const [history, setHistory] = useState([]);

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const assetName = queryParams.get("asset");

    const fetchHistory = async () => {
        try {
            const res = await axios.get(`/api/assets/history/${assetId}`);
            setHistory(res.data);
            // console.log(res.data);
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
                        </TableRow>
                    </TableHead>
                <TableBody>
                    {history.map((item, index) => (
                        <TableRow key={index}>
                            <TableCell>{item.yearMonth}</TableCell>
                            <TableCell>{item.amount}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                </Table>
            </TableContainer>
        </Box>
    )
}

export default AssetHistoryPage