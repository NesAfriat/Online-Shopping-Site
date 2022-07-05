import {useState} from "react";
import classes2 from "../../styling/item.module.css";
import classes3 from "../../styling/list.module.css";
import Card from "../../styling/Card";
import classes from "../../styling/mainNavigation.module.css";
import Shop from "../BasicComponent/Shop";
import ShopRoles from "./Roles/ShopRoles";
import ShopHistory from "./Histories/ShopHistory";
import ShopPolicy from "./Policies/ShopPolicy";
import {getShopBids, getShopInfo, getShopPurchaseHistory, postCloseShop, postReOpenShop} from "../../api";
import ShopProducts from "./Products/ShopProducts";
import ShopBids from "./Bids/ShopBids";

const ViewMyShops = (props) => {
    const [showShop, setShowShop] = useState(null);
    const [showShopProducts, setShowShopProducts] = useState(false);
    const [showShopRoles, setShowShopRoles] = useState(false);
    const [showShopPolicy, setShowShopPolicy] = useState(false);
    const [showShopHistory, setShowShopHistory] = useState(false);
    const [showBids, setShowBids] = useState(false)
    const [shopsHistory, setShopsHistory] = useState([])
    const [shopBids, setShopBids] = useState([])

    const updateShop = (shop) => {
        setShowShop(shop);
        setShowShopProducts(false);
        setShowShopRoles(false);
        setShowShopPolicy(false);
        setShowShopHistory(false);
        setShowBids(false)
    };

    const handleReloadShop = async (shopId) => {
        console.log("reloading shop");
        await getShopInfo(props.visitorId, shopId).then(res => {
                if (res.errorOccurred) {
                    alert(res.errorMessage)
                    console.log(res.errorMessage)
                    return
                } else {
                    updateShop(res.value)
                }
            }
        )
    };

    const viewShopProducts = (e) => {
        e.preventDefault()
        setShowShopProducts(!showShopProducts);
        setShowShopRoles(false);
        setShowShopPolicy(false);
        setShowShopHistory(false);
        setShowBids(false)
    };


    const viewShopRoles = (e) => {
        e.preventDefault()
        setShowShopProducts(false);
        setShowShopRoles(!showShopRoles);
        setShowShopPolicy(false);
        setShowShopHistory(false);
        setShowBids(false)
    };


    const viewShopPolicy = (e) => {
        e.preventDefault()
        setShowShopProducts(false);
        setShowShopRoles(false);
        setShowShopPolicy(!showShopPolicy);
        setShowShopHistory(false);
        setShowBids(false)
    };

    const loadShopHistory = async () => {
        console.log("clicked on shop histoy");
        await getShopPurchaseHistory(props.visitorId, showShop.id).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage)
                console.log(res.errorMessage)
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

    const loadShopBids = async () => {
        console.log("clicked on shop bids");
        await getShopBids(props.visitorId, showShop.id).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage)
                console.log(res.errorMessage)
                return
            } else {
                if (res.value.length === 0) {
                    setShopBids([])
                    alert("No bids found")
                    return
                }
                console.log(res.value)
                setShopBids([...res.value])
            }
        })
    };
    const viewShopHistory = (e) => {
        e.preventDefault()
        loadShopHistory();
        setShowShopProducts(false);
        setShowShopRoles(false);
        setShowShopPolicy(false);
        setShowShopHistory(!showShopHistory);
        setShowBids(false)
    };

    const viewShopBids = (e) => {
        e.preventDefault()
        loadShopBids();
        setShowShopProducts(false);
        setShowShopRoles(false);
        setShowShopPolicy(false);
        setShowShopHistory(false);
        setShowBids(!showBids)
    };

    const closeShop = async (shopId) => {
        console.log("closing shop");
        await postCloseShop(props.visitorId, shopId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Shop close succesfuly");
                updateShop(res.value);
            }
        });
    };

    const reOpenShop = async (shopId) => {
        console.log("re-opening shop");
        await postReOpenShop(props.visitorId, shopId).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
            } else {
                alert("Shop opened again succesfuly");
                updateShop(res.value);
            }
        });
    };

    return (
        <div>
            <h1 className={classes2.content2}>My shops:</h1>
            <ul className={classes3.list}>
                {props.shops.map((shop, i) => {
                    return (
                        <li key={i}>
                            <button
                                className={classes3.actions}
                                onClick={() => updateShop(shop)}
                            >
                                <Card>
                                    <div className={classes2.content}>
                                        <h1>{shop.name}'s shop</h1>
                                        <h3>{shop.description}</h3>
                                    </div>
                                </Card>
                            </button>
                        </li>
                    );
                })}
            </ul>
            <div>
                {showShop !== null && (
                    <>
                        <Shop
                            visitorId={props.visitorId}
                            id={showShop.id}
                            name={showShop.name}
                            founder={showShop.Founder}
                            location={showShop.location}
                            description={showShop.description}
                            open={showShop.open}
                        />
                        <header className={classes.header}>
                            <div className={classes.logo}>
                                <ul>
                                    <li>
                                        <button onClick={viewShopProducts}>
                                            <h3>Products</h3>
                                        </button>
                                    </li>
                                    <li>
                                        <button onClick={viewShopBids}>
                                            <h3>Bids</h3>
                                        </button>
                                    </li>
                                    <li>
                                        <button onClick={viewShopRoles}>
                                            <h3>Roles</h3>
                                        </button>
                                    </li>
                                    <li>
                                        <button onClick={viewShopPolicy}>
                                            <h3>Policies</h3>
                                        </button>
                                    </li>
                                    <li>
                                        <button onClick={viewShopHistory}>
                                            <h3>History</h3>
                                        </button>
                                    </li>
                                    {showShop.open && showShop.founder === props.memberUserName && (
                                        <li>
                                            <button onClick={() => closeShop(showShop.id)}>
                                                <h3>Close shop</h3>
                                            </button>
                                        </li>
                                    )}
                                    {!showShop.open && showShop.founder === props.memberUserName && (
                                        <li>
                                            <button onClick={() => reOpenShop(showShop.id)}>
                                                <h3>Re-open shop</h3>
                                            </button>
                                        </li>
                                    )}
                                </ul>
                            </div>
                        </header>
                        {showShopProducts && (
                            <ShopProducts
                                shopId={showShop.id}
                                visitorId={props.visitorId}
                                products={showShop.products}
                                setter={handleReloadShop}
                            />
                        )}
                        {showShopRoles && (
                            <ShopRoles shopId={showShop.id} visitorId={props.visitorId} founder={showShop.founder}/>
                        )}
                        {showShopPolicy && (
                            <ShopPolicy shopId={showShop.id} visitorId={props.visitorId}/>
                        )}
                        {showShopHistory && (
                            <ShopHistory shopId={showShop.id} visitorId={props.visitorId} history={shopsHistory}/>
                        )}
                        {showBids && (
                            <ShopBids shopId={showShop.id} visitorId={props.visitorId} bids={shopBids} reload={handleReloadShop}/>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default ViewMyShops;
