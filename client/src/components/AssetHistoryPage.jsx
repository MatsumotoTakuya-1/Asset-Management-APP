import { useParams } from "react-router-dom";
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

    const fetchHistory = async () => {
        try {
            const res = await axios.get(`/api/assets/history/${assetId}`);
            setHistory(res.data);
        }catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        fetchHistory();
    }, [yearMonth, assetId]);

    return (
        <Box>
            <Typography variant="body2" color="textSecondary">資産履歴 - {yearMonth}</Typography>

            <TableContainer>
                <Table>
                    <TableHead>
                        <TableCell>日付</TableCell>
                        <TableCell>金額</TableCell>
                    </TableHead>
                </Table>
                <TableBody>
                    {history.map((item, index) => (
                        <TableRow key={index}>
                            <TableCell>{item.yearMonth}</TableCell>
                            <TableCell>{item.amount}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </TableContainer>
        </Box>
    )
}

export default AssetHistoryPage