import classes from "../../../../styling/list2.module.css";
import {useState} from "react";
import AddAtMost from "./AddAtMost";
import AddAtLeast from "./AddAtLeast";
import AddCompose from "./AddCompose";
import ViewAllPolicies from "./ViewAllPolicies";
import {getPurchasePolicies} from "../../../../api";

const PurchaseMenu = (props) => {
    const [viewAllPolicies, setViewAllPolicies] = useState(false);
    const [viewAddAtMost, setViewAddAtMost] = useState(false);
    const [viewAddAtLeast, setViewAddAtLeast] = useState(false);
    const [viewCompose, setViewCompose] = useState(false);
    const [shopPurchasePolicy, setShopPurchasePolicy] = useState([])

    const loadShopPolicies = async () => {
        console.log("clicked on shop policies");
        await getPurchasePolicies(props.visitorId, props.shopId).then(res => {
            console.log(res)
                if (res.errorOccurred) {
                    alert(res.errorMessage)
                    console.log(res.errorMessage)
                    return
                } else {
                    if (res.value.length === 0) {
                        setShopPurchasePolicy([])
                        console.log(res)
                        return
                    }
                    setShopPurchasePolicy([...res.value])
                }
            }
        )
    }
    const handleAllPolicies = (e) => {
        e.preventDefault()
        loadShopPolicies()
        setViewAllPolicies(!viewAllPolicies);
        setViewAddAtMost(false);
        setViewAddAtLeast(false);
        setViewCompose(false);
    };
    const handleAtMost = (e) => {
        e.preventDefault()
        setViewAllPolicies(false);
        setViewAddAtMost(!viewAddAtMost);
        setViewAddAtLeast(false);
        setViewCompose(false);
    };
    const handleAtLeast = (e) => {
        e.preventDefault()
        setViewAllPolicies(false);
        setViewAddAtMost(false);
        setViewAddAtLeast(!viewAddAtLeast);
        setViewCompose(false);
    };
    const handleCompose = (e) => {
        e.preventDefault()
        setViewAllPolicies(false);
        setViewAddAtMost(false);
        setViewAddAtLeast(false);
        setViewCompose(!viewCompose);
    };

    return (
        <div>
            <header className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.ul}>
                        <li>
                            <button onClick={handleAllPolicies}>Show all policies</button>
                        </li>
                        <li>
                            <button onClick={handleAtMost}>Add 'at most' policy</button>
                        </li>
                        <li>
                            <button onClick={handleAtLeast}>Add 'at least' policy</button>
                        </li>
                        <li>
                            <button onClick={handleCompose}>Compose two policies</button>
                        </li>
                    </ul>
                </div>
            </header>
            <div>
                {viewAllPolicies && (
                    <ViewAllPolicies
                        policies={shopPurchasePolicy}
                    />
                )}
                {viewAddAtMost && (
                    <AddAtMost
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setViewAddAtMost}
                    />
                )}
                {viewAddAtLeast && (
                    <AddAtLeast
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setViewAddAtLeast}
                    />
                )}
                {viewCompose && (
                    <AddCompose
                        shopId={props.shopId}
                        visitorId={props.visitorId}
                        setter={setViewCompose}
                    />
                )}
            </div>
        </div>
    );
};

export default PurchaseMenu;
