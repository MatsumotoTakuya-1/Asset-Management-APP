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

const assetCategories = ["銀行", "証券", "仮想通貨", "不動産", "その他"];
// const assetTypes = ["bank", "stock", "crypto", "rent", "other"];

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

    // 先月API呼び出し
    useEffect(() => {
        const fetchSummary = async () => {
            try {
                const date = new Date();
                const last_month = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate()).toISOString();
                // console.log(last_month) //2025-05-09T15:00:00.000Z
                if (!last_month) return;
                const res = await axios.get(`/api/assets/${last_month}/summary`);
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
                            <TableCell>現在値</TableCell>
                            <TableCell>先月の値</TableCell>
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