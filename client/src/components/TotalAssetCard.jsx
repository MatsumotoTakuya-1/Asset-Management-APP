import {useState, useEffect} from "react";
import {Card, CardContent, CircularProgress, Typography} from "@mui/material";
import axios from "axios";


function TotalAssetCard() {
    const [loading, setLoading] = useState(true);
    const [total, setTotal] = useState(0);

    useEffect(() =>{
        const fetchTotal = async () => {
            try{
                const res = await  axios.get('api/assets/total')
                setTotal(res.data.totalAmount)

            }catch (err){
                console.error("総資産の取得に失敗しました。",err);
            }finally {
                setLoading(false);
            }
        }

        fetchTotal();
    }, [])

    return (
        <Card>
            <CardContent>
                <Typography>総資産額</Typography>
                {loading ? (
                    <CircularProgress />
                ) : (
                    <Typography variant="body2" color="textSecondary">
                      ￥{total?.toLocaleString() ?? 0}
                    </Typography>
                )}
            </CardContent>
        </Card>
    )

}

export default TotalAssetCard;