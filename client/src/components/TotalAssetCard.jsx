import {useState, useEffect} from "react";
import {Card, CardContent, CircularProgress, Typography} from "@mui/material";
import axios from "axios";


function TotalAssetCard() {
    const [loading, setLoading] = useState(true);
    const [total, setTotal] = useState(0);

    useEffect(() => {
        const fetchTotal = async () => {
            try {
                const yearMonth = new Date().toISOString()
                console.log(yearMonth)
                const res = await axios.get(`api/assets/${yearMonth}`,)
                // console.log(res.data)
                setTotal(res.data.totalAmount)

            } catch (err) {
                console.error("総資産の取得に失敗しました。", err);
            } finally {
                setLoading(false);
            }
        }

        fetchTotal();
    }, [])

    return (
        <Card>
            <CardContent>
                <Typography textAlign={"left"}>総資産額</Typography>
                {loading ? (
                    <CircularProgress/>
                ) : (
                    <Typography variant="h5" color="textSecondary" textAlign={"left"} fontWeight={"bold"}>
                        ￥{total?.toLocaleString() ?? 0}
                    </Typography>
                )}
            </CardContent>
        </Card>
    )

}

export default TotalAssetCard;