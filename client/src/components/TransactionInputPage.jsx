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

const incomeCategories = ["給与", "副業", "投資収益", "不動産収入", "その他"];
const expenseCategories = ["家賃", "外食", "スーパー", "交通費", "娯楽費"];

const TransactionInputPage = () => {
    const {yearMonth} = useParams();
    const [tab, setTab] = useState("income");
    const [amounts, setAmounts] = useState({});
    const [summary, setSummary] = useState({});

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState("success");

    const showSnackbar = (message, severity = "success") => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };

    const categories = tab === "income" ? incomeCategories : expenseCategories;

    // 金額入力の変更を処理
    const handleAmountChange = (category, value) => {
        setAmounts((prev) => ({
            ...prev,
            [category]: value,
        }));
    };
    const fetchSummary = async () => {
        try {
            const yearMonth = new Date().toISOString()
            if (!yearMonth) return;
            const res = await axios.get(`/api/transactions/${yearMonth}/summary/${tab}`);
            // console.log(res.data);//{家賃: 170000, 外食: 4000}
            setSummary(res.data);
        } catch (err) {
            console.error("カテゴリ合計取得失敗", err);
        }
    };
    // 合計取得API呼び出し
    useEffect(() => {

        fetchSummary();
    }, [yearMonth, tab]);

    // 保存処理
    const handleSubmit = async () => {
        try {
            const yearMonth = new Date().toISOString()
            const payload = categories
                .filter((cat) => amounts[cat])
                .map((cat) => ({
                    category: cat,
                    type: tab,
                    amount: amounts[cat],
                    yearMonth: yearMonth,
                    memo: "下記はあとで実装",
                    is_fixed: false,
                    userId: 1 //固定

                }));
            // console.log(payload);//{給与: 250000, 副業: 1000,...}
            await axios.post(`/api/transactions/${yearMonth}`, payload);
            showSnackbar("Successfully saved", "success");
            setAmounts("")
            fetchSummary();

        } catch (err) {
            console.error("Save failed", err);
            showSnackbar("Error saved", "error");

        }
    };

    return (
        <>
        <Box>
            <Typography variant="h5" fontWeight={"bold"} textAlign={"left"} gutterBottom>
                収入 & 支出 - {yearMonth}
            </Typography>

            <Tabs value={tab} onChange={(_, newValue) => setTab(newValue)} sx={{mb: 2}}>
                <Tab label="収入" value="income"/>
                <Tab label="支出" value="expense"/>
            </Tabs>

            <TableContainer component={Paper} sx={{mb: 2}}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>カテゴリ</TableCell>
                            <TableCell>金額</TableCell>
                            <TableCell>月の合計</TableCell>
                            <TableCell>History</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {categories.map((cat) => (
                            <TransactionInputRow
                                key={cat}
                                category={cat}
                                yearMonth={yearMonth}
                                value={amounts[cat] || ""}
                                onChange={(val) => handleAmountChange(cat, val)}
                                totalForMonth={summary[cat] || 0}
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

export default TransactionInputPage;