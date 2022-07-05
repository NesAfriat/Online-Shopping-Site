import {useState} from "react";
import classes from "../../../styling/list2.module.css";
import DiscountMenu from "./DiscountPolicy/DiscountMenu";
import PurchaseMenu from "./PurchasePolicy/PurchasePMenu";

const ShopPolicy = (props) => {
    const [showDiscountMenu, setShowDiscountMenu] = useState(false);
    const [showPurchaseMenu, setShowPurchaseMenu] = useState(false);

    const ExtendDiscountMemu = () => {
        setShowDiscountMenu(!showDiscountMenu);
        setShowPurchaseMenu(false);
    };
    const ExtendPurchaseMenu = () => {
        setShowDiscountMenu(false);
        setShowPurchaseMenu(!showPurchaseMenu);
    };

    return (
        <div>
            <h3 className={classes.header2}>
                <div className={classes.logo}>
                    <ul className={classes.ul}>
                        <li className={classes.li}>
                            <button onClick={ExtendPurchaseMenu}><h4>Purchase policies</h4></button>
                        </li>
                        <li>
                            <button onClick={ExtendDiscountMemu}><h4>Discount policies</h4></button>
                        </li>
                    </ul>
                </div>
            </h3>
            {showDiscountMenu && <DiscountMenu visitorId={props.visitorId} shopId={props.shopId}/>}
            {showPurchaseMenu && <PurchaseMenu visitorId={props.visitorId} shopId={props.shopId}/>}
        </div>
    );
};

export default ShopPolicy;
