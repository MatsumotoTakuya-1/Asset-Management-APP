import {useParams} from "react-router-dom";
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
    Paper, Snackbar, Alert,
} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionInputRow from "../components/TransactionInputRow";
import AssetInputRow from "./AssetInputRow.jsx";

// const assetCategories = ["銀行", "証券", "仮想通貨", "不動産", "その他"];
// const assetTypes = ["bank", "stock", "crypto", "rent", "other"];

const AssetInputPage = () => {
    const {yearMonth} = useParams();
    const [amounts, setAmounts] = useState({});
    const [summary, setSummary] = useState({});// 先月の資産額
    const [currentSummary, setCurrentSummary] = useState({});// 今月の資産額
    const [assets, setAssets] = useState([]);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState("success");

    const showSnackbar = (message, severity = "success") => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };


    // 金額入力の変更を処理
    const handleAmountChange = (category, value) => {
        setAmounts((prev) => ({
            ...prev,
            [category]: value,
        }));
    };

    const fetchSummary = async () => {
        try {
            const date = new Date();
            const last_month = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate()).toISOString();
            // console.log(last_month) //2025-05-09T15:00:00.000Z
            if (!last_month) return;
            const res = await axios.get(`/api/assets/${last_month}/summary`);
            // console.log(res.data);
            setSummary(res.data);

            const current_month = new Date(date.getFullYear(), date.getMonth(), date.getDate()).toISOString();
            if (!current_month) return;
            const res2 = await axios.get(`/api/assets/${current_month}/summary`);
            setCurrentSummary(res2.data);

        } catch (err) {
            console.error("先月の資産合計取得失敗", err);
        }
    };

    const fetchAssets = async () => {
        try{
            const res = await axios.get("/api/assets/list");
            setAssets(res.data); //[{id,name,..},{}]
            // console.log(res.data);
        } catch (err) {
            console.error("assets取得失敗", err);
        }
    }

    // 資産の値情報API
    useEffect(() => {
        fetchSummary();
        fetchAssets();
    }, [yearMonth]);

    // 保存処理
    const handleSubmit = async () => {
        try {
            const yearMonth = new Date().toISOString()
            const payload = assets
                .filter((asset) => amounts[asset.name])
                .map((asset) => ({
                    name: asset.name,
                    userId: 1,
                    amount: amounts[asset.name],
                    // yearMonth: yearMonth,
                    memo: "下記はあとで実装",
                }));
            // console.log(payload); //[{name:証券, userId:1, amount:100, memo:"test"},{},]

            await axios.post(`/api/assets/${yearMonth}`, payload);
            // alert("今月の資産保存成功");
            showSnackbar("Successfully saved asset", "success");
            setAmounts("")
            //保存後に再取得
            fetchSummary();
        } catch (err) {
            console.error("Save failed", err);
            showSnackbar("Error saved asset", "error");
        }
    };

    return (
        <>
        <Box>
            <Typography variant="h5" fontWeight={"bold"} textAlign={"left"} gutterBottom>
                資産 - {yearMonth}
            </Typography>


            <TableContainer component={Paper} sx={{mb: 2}}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>資産名</TableCell>
                            <TableCell>現在の資産額</TableCell>
                            <TableCell>先月の資産額</TableCell>
                            <TableCell>先月からの変化率</TableCell>
                            <TableCell>History</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {assets.map((asset) => (
                            <AssetInputRow
                                key={asset.id}
                                assetId={asset.id}
                                assetName={asset.name}
                                yearMonth={yearMonth}
                                value={amounts[asset.name] || ""}
                                onChange={(val) => handleAmountChange(asset.name, val)}
                                totalForMonth={summary[asset.name] || "-"}
                                currentTotalForMonth={currentSummary[asset.name] || 0}
                            />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box textAlign="right">
                <Button variant="outlined" color="primary" onClick={handleSubmit}>
                    Save
                </Button>
            </Box>
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

    );
};

export default AssetInputPage;