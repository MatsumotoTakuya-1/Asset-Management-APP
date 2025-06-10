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
    Paper,
} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionInputRow from "../components/TransactionInputRow";
import AssetInputRow from "./AssetInputRow.jsx";

const assetCategories = [{"銀行": "bank", "証券": "stock", "仮想通貨": "crypto", "不動産": "rent", "その他": "other"}];

const AssetInputPage = () => {
    const {yearMonth} = useParams();
    const [amounts, setAmounts] = useState({});
    const [summary, setSummary] = useState({});


    // 金額入力の変更を処理
    const handleAmountChange = (category, value) => {
        setAmounts((prev) => ({
            ...prev,
            [category]: value,
        }));
    };

    // 合計取得API呼び出し
    useEffect(() => {
        const fetchSummary = async () => {
            try {
                const yearMonth = new Date().toISOString()
                if (!yearMonth) return;
                const res = await axios.get(`/api/transactions/${yearMonth}/summary/${tab}`);
                setSummary(res.data);
            } catch (err) {
                console.error("カテゴリ合計取得失敗", err);
            }
        };

        fetchSummary();
    }, [yearMonth]);

    // 保存処理
    // const handleSubmit = async () => {
    //     try {
    //         const yearMonth = new Date().toISOString()
    //         const payload = categories
    //             .filter((cat) => amounts[cat])
    //             .map((cat) => ({
    //                 category: cat,
    //                 type: tab,
    //                 amount: amounts[cat],
    //                 yearMonth: yearMonth,
    //                 memo: "下記はあとで実装",
    //                 is_fixed: false,
    //                 userId: 1
    //
    //             }));
    //         console.log(payload);
    //
    //         await axios.post(`/api/transactions/${yearMonth}`, payload);
    //         alert("Saved successfully");
    //     } catch (err) {
    //         console.error("Save failed", err);
    //     }
    // };

    return (
        <Box>
            <Typography variant="h5" textAlign={"left"} gutterBottom>
                資産 - {yearMonth}
            </Typography>


            <TableContainer component={Paper} sx={{mb: 2}}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>名前</TableCell>
                            <TableCell>種別</TableCell>
                            <TableCell>現在値</TableCell>
                            <TableCell>先月の値</TableCell>
                            <TableCell>先月からの変化率</TableCell>
                            <TableCell>History</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {assetCategories.map((asset) => (
                            <AssetInputRow
                                key={asset}
                                assetName={asset}
                                yearMonth={yearMonth}
                                value={amounts[asset] || ""}
                                onChange={(val) => handleAmountChange(asset, val)}
                                totalForMonth={summary[asset] || 0}
                            />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box textAlign="right">
                {/*<Button variant="contained" color="primary" onClick={handleSubmit}>*/}
                {/*    Save*/}
                {/*</Button>*/}
            </Box>
        </Box>
    );
};

export default AssetInputPage;