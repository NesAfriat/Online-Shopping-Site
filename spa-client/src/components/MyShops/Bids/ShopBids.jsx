import {useState} from "react";
import classes from "../../../styling/list2.module.css";
import IncrementBid from "./Actions/IncrementBid";
import ApproveBid from "./Actions/ApproveBid";
import LoadShopBids from "./LoadShopBids";

const ShopBids = (props) =>{
    const [showShopBids, setShowShopBids] = useState(false)
    const [showApproveBid, setApproveBid] = useState(false)
    const [showIncrementBid, setIncrementBid] = useState(false)

    const viewBids = (e) => {
        e.preventDefault();
        setShowShopBids(!showShopBids)
        setApproveBid(false)
        setIncrementBid(false)
    };
    const viewApproveBid = (e) => {
        e.preventDefault();
        setShowShopBids(false)
        setApproveBid(!showApproveBid)
        setIncrementBid(false)
    };
    const viewIncremetBid = (e) => {
        e.preventDefault();
        setShowShopBids(false)
        setApproveBid(false)
        setIncrementBid(!showIncrementBid)
    };
    return(
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.list}>
                        <li>
                            <button onClick={viewBids}>
                                <h4>Shop bids</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewApproveBid}>
                                <h4>approve bid</h4>
                            </button>
                        </li>
                        <li>
                            <button onClick={viewIncremetBid}>
                                <h4>increment bid</h4>
                            </button>
                        </li>
                    </ul>
                </div>
            </header>
            {showShopBids && (
                <LoadShopBids
                    shopId={props.shopId}
                    visitorId={props.visitorId}
                    bids={props.bids}
                    setter={props.reload}
                />
            )}
            {showApproveBid && (
                <ApproveBid
                    shopId={props.shopId}
                    visitorId={props.visitorId}
                    setter={props.reload}
                />
            )}
            {showIncrementBid && (
                <IncrementBid
                    shopId={props.shopId}
                    visitorId={props.visitorId}
                    setter={props.reload}
                />
            )}
        </div>
    )
}
export default ShopBids