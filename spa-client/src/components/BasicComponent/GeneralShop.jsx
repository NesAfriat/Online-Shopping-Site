import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import ViewAllProducts from "../ViewAllProducts";
import {useState} from "react";
import {getShopPurchaseHistory} from "../../api";
import ShopHistory from "../MyShops/Histories/ShopHistory";

const GeneralShop = (props) => {
    const [shopHistory, setShopsHistory] = useState([]);
    const [showHistory, setShowHistory] = useState(false);


    const loadShopHistory = async (shopId) => {
        console.log("clicked on shop purhcase histoy");
        await getShopPurchaseHistory(props.visitorId, shopId).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage)
                console.log(res.errorMessage);
                return
            } else {
                if (res.value.length === 0) {
                    setShopsHistory([])
                    alert("No purchase found")
                    return
                }
                setShopsHistory([...res.value])
            }
        })
    };
    const viewShopHistory = (e) => {
        e.preventDefault()
        loadShopHistory(props.id);
        setShowHistory(!showHistory);
    };
    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>{props.name}'s shop</h1>
                    <h3>founded by {props.founder}</h3>
                    <p>located at: {props.location}</p>
                    <p>general description: {props.description}</p>
                    {props.products !== undefined &&
                        <ViewAllProducts products={props.products} shopId={props.id} visitorId={props.visitorId} member={props.member}/>}
                    {props.isAdmin && <button onClick={viewShopHistory}>Get shop purchase history</button>}
                </div>
                {showHistory && <ShopHistory shopId={props.id} visitorId={props.visitorId} history={shopHistory}/>}
            </Card>
        </>
    )
}

export default GeneralShop;